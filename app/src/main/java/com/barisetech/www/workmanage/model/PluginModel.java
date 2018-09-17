package com.barisetech.www.workmanage.model;

import android.text.TextUtils;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.plugin.PluginInfo;
import com.barisetech.www.workmanage.bean.plugin.ReqAllPlugin;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.PlugService;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PluginModel extends BaseModel {


    public static final String TAG = "PluginModel";
    private ModelCallBack modelCallBack;
    private PlugService plugService;
    public static final int TYPE_ALL = 4;


    public PluginModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        plugService = HttpService.getInstance().buildJsonRetrofit().create(PlugService.class);
    }


    public Disposable reqAllPlugin(ReqAllPlugin reqAllPlugin) {
        if (null == reqAllPlugin) {
            return null;
        }
        String company = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_COMPANY, "");
        if (TextUtils.isEmpty(company)) {
            return null;
        }
        reqAllPlugin.setMachineCode(mToken);
        reqAllPlugin.setCompanyName(company);
        Disposable disposable = plugService.getAllPlugin(reqAllPlugin)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<PluginInfo>>() {
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
                    protected void onSuccess(List<PluginInfo> response) {
                        LogUtil.d(TAG, "AllPlugin result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_ALL, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }


}
