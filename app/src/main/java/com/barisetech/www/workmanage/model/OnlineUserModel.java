package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.map.pipe.PipeTrackInfo;
import com.barisetech.www.workmanage.bean.map.pipe.ReqPipeTrack;
import com.barisetech.www.workmanage.bean.onlineuser.ReqOffline;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.MapService;
import com.barisetech.www.workmanage.http.api.OnlineUserService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH onlineUserService 2018/9/12.
 */
public class OnlineUserModel extends BaseModel {
    public static final String TAG = "OnlineUserModel";

    private ModelCallBack modelCallBack;
    private OnlineUserService onlineUserService;

    public static final int TYPE_ALL = 1;
    public static final int TYPE_OFFLINE = 2;

    public OnlineUserModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        onlineUserService = HttpService.getInstance().buildJsonRetrofit().create(OnlineUserService.class);
    }

    public Disposable getAll() {
        ObserverCallBack<List<String>> disposable = onlineUserService.getOnlineUser(getBearer(), mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<String>>() {

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
                    protected void onSuccess(List<String> response) {
                        LogUtil.d(TAG, "onlineUser = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_ALL, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    public Disposable offlineUser(ReqOffline reqOffline) {
        ObserverCallBack<Boolean> disposable = onlineUserService.offlineUser(getBearer(), mToken, reqOffline)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Boolean>() {

                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_OFFLINE, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_OFFLINE, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_OFFLINE, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(Boolean response) {
                        LogUtil.d(TAG, "offlineUser = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_OFFLINE, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }
}
