package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.auth.AuthInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAllAuth;
import com.barisetech.www.workmanage.bean.auth.ReqModifyAuth;
import com.barisetech.www.workmanage.bean.onlineuser.ReqOffline;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.AuthService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH authService 2018/9/12.
 */
public class AuthModel extends BaseModel {
    public static final String TAG = "AuthModel";

    private ModelCallBack modelCallBack;
    private AuthService authService;

    public static final int TYPE_ALL = 1;
    public static final int TYPE_MODIFY = 2;

    public AuthModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        authService = HttpService.getInstance().buildJsonRetrofit().create(AuthService.class);
    }

    public Disposable getAll(ReqAllAuth reqAllAuth) {
        reqAllAuth.MachineCode = mToken;
        ObserverCallBack<List<AuthInfo>> disposable = authService.getAll(reqAllAuth)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<AuthInfo>>() {

                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_ALL, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_ALL, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_ALL, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(List<AuthInfo> response) {
                        LogUtil.d(TAG, "AuthInfos = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_ALL, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    public Disposable modify(ReqModifyAuth reqModifyAuth) {
        ObserverCallBack<String> disposable = authService.modify(mToken, reqModifyAuth)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<String>() {

                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_MODIFY, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_MODIFY, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_MODIFY, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(String response) {
                        LogUtil.d(TAG, "modify auth = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_MODIFY, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }
}
