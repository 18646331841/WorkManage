package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.auth.AuthInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAllAuth;
import com.barisetech.www.workmanage.bean.auth.ReqModifyAuth;
import com.barisetech.www.workmanage.bean.pipetap.PipeTapInfo;
import com.barisetech.www.workmanage.bean.pipetap.ReqAllPipeTap;
import com.barisetech.www.workmanage.bean.pipetap.ReqModifyPipeTap;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.AuthService;
import com.barisetech.www.workmanage.http.api.PipeTapService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH pipeTapService 2018/9/12.
 */
public class PipeTapModel extends BaseModel {
    public static final String TAG = "PipeTapModel";

    private ModelCallBack modelCallBack;
    private PipeTapService pipeTapService;

    public static final int TYPE_ALL = 1;
    public static final int TYPE_MODIFY = 2;

    public PipeTapModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        pipeTapService = HttpService.getInstance().buildJsonRetrofit().create(PipeTapService.class);
    }

    public Disposable getAll(ReqAllPipeTap reqAllPipeTap) {
        reqAllPipeTap.MachineCode = mToken;
        ObserverCallBack<List<PipeTapInfo>> disposable = pipeTapService.getAll(reqAllPipeTap)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<PipeTapInfo>>() {

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
                    protected void onSuccess(List<PipeTapInfo> response) {
                        LogUtil.d(TAG, "PipeTapInfos = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_ALL, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    public Disposable modify(ReqModifyPipeTap reqModifyPipeTap) {
        ObserverCallBack<String> disposable = pipeTapService.modify(mToken, reqModifyPipeTap)
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
                        LogUtil.d(TAG, "modify pipeTap = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_MODIFY, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }
}
