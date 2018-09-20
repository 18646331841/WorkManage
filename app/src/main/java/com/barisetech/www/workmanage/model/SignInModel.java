package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.signin.ReqGetSite;
import com.barisetech.www.workmanage.bean.signin.ReqSignIn;
import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.SignInService;
import com.barisetech.www.workmanage.utils.LogUtil;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/28.
 */
public class SignInModel extends BaseModel {
    public static final String TAG = "SignInModel";

    private ModelCallBack modelCallBack;
    private SignInService signInService;

    public static final int TYPE_GET = 3;
    public static final int TYPE_CHECK = 4;

    public SignInModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        signInService = HttpService.getInstance().buildJsonRetrofit().create(SignInService.class);
    }

    /**
     * 得到站点
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
}
