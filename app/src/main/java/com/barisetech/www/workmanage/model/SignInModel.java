package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.ImageInfo;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.signin.ReqGetSite;
import com.barisetech.www.workmanage.bean.signin.ReqSignIn;
import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.SignInService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
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
        Disposable disposable = signInService.getSite(getBearer(), reqGetSite)
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
        Disposable disposable = signInService.checkIn(getBearer(), mToken, reqSignIn)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<TaskSiteBean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        //网络等原因导致打卡失败，保存在本地，有网时再上传
                        LogUtil.d(TAG, "网络原因打卡失败");

                        List<ReqSignIn> signs = appDatabase.reqSignInDao().getSigns();
                        int index = 0;
                        if (signs != null) {
                            LogUtil.d(TAG, "size = " + signs.size());
                            index = signs.size();
                        }
                        reqSignIn.id = index;

                        for (ImageInfo imageInfo : reqSignIn.WorkImageList) {
                            imageInfo.signId = index;
                        }
                        appDatabase.reqSignInDao().insetSign(reqSignIn);
                        appDatabase.imageInfoDao().insetImages(reqSignIn.WorkImageList);

                        List<ImageInfo> imageInfos = appDatabase.imageInfoDao().getImageInfos(index);
                        LogUtil.d(TAG, "image size = " + imageInfos.size());

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

    private int id;
    List<ReqSignIn> reqSignIns = new ArrayList<>();

    /**
     * 上传因为网络原因没有上传成功的打卡信息
     */
    public Disposable uploadSignInFail() {
        reqSignIns.clear();
        Observable<List<ReqSignIn>> listObservable = Observable.create((ObservableOnSubscribe<List
                <ReqSignIn>>) e -> {
            List<ReqSignIn> signs = getReqSignIns();
            if (signs != null && signs.size() > 0) {
                for (ReqSignIn reqSignIn : signs) {
                    List<ImageInfo> imageInfos = getImageInfos(reqSignIn.id);
                    if (imageInfos != null && imageInfos.size() > 0) {
                        reqSignIn.WorkImageList = imageInfos;
                    }
                }
                reqSignIns.addAll(signs);
                e.onComplete();
            } else {
                e.onNext(null);
            }
        });

        Observable<List<ReqSignIn>> signObservable = Observable.fromIterable(reqSignIns)
                .flatMap((Function<ReqSignIn, ObservableSource<BaseResponse<TaskSiteBean>>>) reqSignIn -> {
                    id = reqSignIn.id;
                    return signInService.checkIn(getBearer(), mToken, reqSignIn);
                }).map(new Function<BaseResponse<TaskSiteBean>, ReqSignIn>() {
                    @Override
                    public ReqSignIn apply(BaseResponse<TaskSiteBean> taskSiteBeanBaseResponse) throws Exception {
                        if (taskSiteBeanBaseResponse != null) {
                            int code = taskSiteBeanBaseResponse.Code;
                            if (code == 200) {
                                LogUtil.d(TAG, "Sign in 网络上传成功 " + id);
                                TaskSiteBean taskSiteBean = taskSiteBeanBaseResponse.Response;
                                ReqSignIn reqSignIn = new ReqSignIn();
                                reqSignIn.toTaskSite(taskSiteBean);
                                ReqSignIn sign = appDatabase.reqSignInDao().getSign(id);
                                if (sign != null) {
                                    LogUtil.d(TAG, "delete sign = " + id);
                                    appDatabase.reqSignInDao().deleteSign(sign);
                                }
                                List<ImageInfo> imageInfos = appDatabase.imageInfoDao().getImageInfos(id);
                                if (imageInfos != null && imageInfos.size() > 0) {
                                    LogUtil.d(TAG, "delete images num = " + imageInfos.size());
                                    appDatabase.imageInfoDao().deleteImageInfos(imageInfos);
                                }
                                return sign;
                            }
                        }
                        ReqSignIn nullReq = new ReqSignIn();
                        nullReq.SiteId = "-1";
                        return nullReq;
                    }
                })
                .toList().toObservable();


        Disposable disposable = Observable.concat(listObservable, signObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(lists -> {
                    if (lists != null && lists.size() > 0) {
                        for (ReqSignIn reqSignIn : lists) {
                            if (reqSignIn != null && !reqSignIn.SiteId.equals("-1")) {
                                LogUtil.d(TAG, reqSignIn.Name + " sign in completed");
                            } else {
                                LogUtil.d(TAG, reqSignIn.Name + " sign in fail");
                            }
                        }
                    }
                }, throwable -> {
                    LogUtil.d(TAG, throwable.getMessage());
                });

        return disposable;
    }

    private List<ReqSignIn> getReqSignIns() {
        return appDatabase.reqSignInDao().getSigns();
    }

    private List<ImageInfo> getImageInfos(int signid) {
        return appDatabase.imageInfoDao().getImageInfos(signid);
    }
}
