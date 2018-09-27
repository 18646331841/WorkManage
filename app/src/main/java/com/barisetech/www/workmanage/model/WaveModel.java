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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
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
     * 获取波形
     *
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

    private String siteId;
    private int type;

    /**
     * 获取所有波形
     *
     * @param reqWaves
     * @return
     */
    public Disposable reqAll(List<ReqWave> reqWaves) {
        if (null == reqWaves || reqWaves.size() <= 0) {
            return null;
        }

        List<WaveBean> waveBeanList = new ArrayList<>();

        Disposable disposable = Observable.fromArray(reqWaves)
                .flatMap((Function<List<ReqWave>, ObservableSource<ReqWave>>) reqWaves1 -> {
                    siteId = "default";
                    type = 0;
                    return Observable.fromIterable(reqWaves1);
                })
                .flatMap((Function<ReqWave, ObservableSource<BaseResponse<WaveBean>>>) reqWave -> {
                    siteId = reqWave.siteId;
                    type = reqWave.type;
                    return waveService.getAll(reqWave).onErrorReturnItem(new
                            BaseResponse<>(-1, "", null));
                })
                .map(new Function<BaseResponse<WaveBean>, Integer>() {
                    @Override
                    public Integer apply(BaseResponse<WaveBean> waveBeanBaseResponse) throws Exception {

                        if (waveBeanBaseResponse != null) {
                            int code = waveBeanBaseResponse.Code;
                            if (code == 200) {
                                WaveBean response = waveBeanBaseResponse.Response;
                                if (response != null) {
                                    response.siteId = siteId;
                                    response.type = type;
                                    waveBeanList.add(response);
                                    return code;
                                } else {
                                    LogUtil.d(TAG, "siteId : " + siteId + " type : " + type + " Response = null");
                                }
                            } else {
                                LogUtil.d(TAG, "siteId : " + siteId + " type : " + type + " Response Code = " + code);
                                if (code == 401) {
                                    return Config.ERROR_UNAUTHORIZED;
                                }
                            }
                        } else {
                            LogUtil.d(TAG, "siteId : " + siteId + " type : " + type + " BaseResponse = null");
                        }
                        return Config.ERROR_FAIL;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(lists -> {
                    if (waveBeanList.size() <= 0) {
                        FailResponse failResponse = new FailResponse(TYPE_ALL, Config.ERROR_FAIL);
                        for (int i = 0; i < lists.size(); i++) {
                            if (lists.get(i) == Config.ERROR_UNAUTHORIZED) {
                                failResponse = new FailResponse(TYPE_ALL, Config.ERROR_UNAUTHORIZED);
                                break;
                            }
                        }
                        modelCallBack.fail(failResponse);
                    } else {
                        LogUtil.d(TAG, "wave all load");
                        TypeResponse typeResponse = new TypeResponse(TYPE_ALL, waveBeanList);
                        modelCallBack.netResult(typeResponse);
                    }
                }, throwable -> {
                    LogUtil.d(TAG, throwable.getMessage());
                });
        return disposable;
    }
}
