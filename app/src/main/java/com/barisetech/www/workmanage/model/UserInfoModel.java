package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.ReqModifyPwd;
import com.barisetech.www.workmanage.bean.ReqModifyUser;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.UserInfoService;
import com.barisetech.www.workmanage.utils.LogUtil;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserInfoModel extends BaseModel {


    public static final String TAG = "UserInfoModel";

    private UserInfoService userInfoService;
    private ModelCallBack modelCallBack;

    public static final int TYPE_USER = 1;
    public static final int TYPE_PWD = 2;


    public UserInfoModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        userInfoService = HttpService.getInstance().buildJsonRetrofit().create(UserInfoService.class);
    }


    /**
     * 修改密码
     * @param reqModifyPwd
     * @return
     */
    public Disposable reqModifyPwd(ReqModifyPwd reqModifyPwd) {
        if (null == reqModifyPwd) {
            return null;
        }
        reqModifyPwd.setMachineCode(mToken);
        Disposable disposable = userInfoService.modifyPwd(reqModifyPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Boolean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_PWD, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_PWD, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_PWD, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(Boolean response) {
                        LogUtil.d(TAG, "AllPipe result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_PWD, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }


    /**
     * 修改账号
     * @param reqModifyUser
     * @return
     */
    public Disposable reqModifyUser(ReqModifyUser reqModifyUser) {
        if (null == reqModifyUser) {
            return null;
        }
        reqModifyUser.setMachineCode(mToken);
        Disposable disposable = userInfoService.modifyUser(reqModifyUser)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Boolean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_USER, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_USER, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_USER, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(Boolean response) {
                        LogUtil.d(TAG, "AllPipe result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_USER, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }
}
