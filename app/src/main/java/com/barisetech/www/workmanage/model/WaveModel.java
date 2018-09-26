package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.wave.ReqWave;
import com.barisetech.www.workmanage.bean.wave.WaveBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.WaveService;
import com.barisetech.www.workmanage.utils.LogUtil;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/28.
 */
public class WaveModel extends BaseModel {
    public static final String TAG = "WaveModel";

    private ModelCallBack modelCallBack;
    private WaveService waveService;

    public static final int TYPE_ALL = 4;

    public WaveModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        waveService = HttpService.getInstance().buildJsonRetrofit().create(WaveService.class);
    }

    /**
     * 获取所有数字化仪
     * @param reqWave
     * @return
     */
    public Disposable reqAll(ReqWave reqWave) {
        if (null == reqWave) {
            return null;
        }
        reqWave.MachineCode = mToken;
        Disposable disposable = waveService.getAll(reqWave)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<WaveBean>() {
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
                    protected void onSuccess(WaveBean response) {
                        LogUtil.d(TAG, "wave result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_ALL, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }
}
