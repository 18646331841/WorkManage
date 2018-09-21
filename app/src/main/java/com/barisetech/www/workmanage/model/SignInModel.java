package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.signin.ReqGetSite;
import com.barisetech.www.workmanage.bean.signin.ReqSignIn;
import com.barisetech.www.workmanage.bean.signin.SignInAndImages;
import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.SignInService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/28.
 */
public class SignInModel extends BaseModel {
    public static final String TAG = "SignInModel";

    private ModelCallBack modelCallBack;
    private SignInService signInService;
    private AppDatabase appDatabase;

    public static final int TYPE_GET = 3;
    public static final int TYPE_CHECK = 4;

    public SignInModel(ModelCallBack modelCallBack, AppDatabase appDatabase) {
        super(modelCallBack);
        this.appDatabase = appDatabase;
        this.modelCallBack = modelCallBack;
        signInService = HttpService.getInstance().buildJsonRetrofit().create(SignInService.class);
    }

    /**
     * 得到站点
     *
     * @param reqGetSite
     * @return
     */
    public Disposable reqGetSite(ReqGetSite reqGetSite) {
        if (reqGetSite == null) {
            return null;
        }
        reqGetSite.setMachineCode(mToken);
        Disposable disposable = signInService.getSite(reqGetSite)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<TaskSiteBean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_GET, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_GET, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_GET, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(TaskSiteBean response) {
                        LogUtil.d(TAG, "getSite result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_GET, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    /**
     * 打卡
     *
     * @param reqSignIn
     * @return
     */
    public Disposable reqSignIn(ReqSignIn reqSignIn) {
        if (null == reqSignIn) {
            return null;
        }
        Disposable disposable = signInService.checkIn(mToken, reqSignIn)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<TaskSiteBean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        //网络等原因导致打卡失败，保存在本地，有网时再上传
                        LogUtil.d(TAG, "网络原因打卡失败");
                        SignInAndImages signInAndImages = new SignInAndImages();
                        signInAndImages.signInEntity = reqSignIn;
                        signInAndImages.imageInfos = reqSignIn.WorkImageList;
                        appDatabase.signInAndImagesDao().insert(signInAndImages);

                        List<SignInAndImages> signInAndImages1 = appDatabase.signInAndImagesDao().getSignInAndImages();
                        if (signInAndImages1 != null) {
                            LogUtil.d(TAG, "size = " + signInAndImages1.size());
                        }
                        FailResponse failResponse = new FailResponse(TYPE_CHECK, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_CHECK, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_CHECK, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(TaskSiteBean response) {
                        LogUtil.d(TAG, "signIn result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_CHECK, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    List<SignInAndImages> signInAndImages;
    /**
     * 上传因为网络原因没有上传成功的打卡信息
     */
    public Disposable uploadSignInFail() {
        Disposable subscribe = getSignAndImages()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(signInAndImages12 -> {
                    if (signInAndImages12 != null && signInAndImages12.size() > 0) {
                        signInAndImages = signInAndImages12;
                    } else {
                        signInAndImages = null;
                    }
                }, throwable -> {
                    signInAndImages = null;
                });

        if (signInAndImages == null || signInAndImages.size() <= 0) {
            return null;
        }

        Disposable disposable = Observable.fromIterable(signInAndImages)
                .flatMap((Function<SignInAndImages, ObservableSource<BaseResponse<TaskSiteBean>>>) signInAndImages1 -> {
                    ReqSignIn reqSignIn = signInAndImages1.signInEntity;
                    reqSignIn.WorkImageList = signInAndImages1.imageInfos;
                    return signInService.checkIn(mToken, reqSignIn);
                }).map(new Function<BaseResponse<TaskSiteBean>, String>() {
                    @Override
                    public String apply(BaseResponse<TaskSiteBean> taskSiteBeanBaseResponse) throws Exception {
                        if (taskSiteBeanBaseResponse != null) {
                            int code = taskSiteBeanBaseResponse.Code;
                            if (code == 200) {
                                TaskSiteBean taskSiteBean = taskSiteBeanBaseResponse.Response;
                                ReqSignIn reqSignIn = new ReqSignIn();
                                reqSignIn.toTaskSite(taskSiteBean);
                                SignInAndImages signInAndImages1 = new SignInAndImages();
                                signInAndImages1.signInEntity = reqSignIn;
                                signInAndImages1.imageInfos = reqSignIn.WorkImageList;
                                appDatabase.signInAndImagesDao().delete(signInAndImages1);
                                return reqSignIn.Name;
                            }
                        }
                        return "";
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(lists -> {
                    if (lists.size() > 0) {
                        for (String siteName : lists) {
                            LogUtil.d(TAG, siteName + " sign in completed");
                        }
                    }
                }, throwable -> {
                    LogUtil.d(TAG, throwable.getMessage());
                });
        return disposable;
    }

//    private Maybe<List<SignInAndImages>> getSignAndImages() {
//        return appDatabase.signInAndImagesDao().getSignInAndImagesRx();
//    }
    private Maybe<List<SignInAndImages>> getSignAndImages() {
        return appDatabase.signInAndImagesDao().getSignInAndImagesRx();
    }
}
