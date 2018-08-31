package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAddAlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAllAlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqDeleteAlarmAnalysis;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.AlarmAnalysisService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/28.
 */
public class AlarmAnalysisModel extends BaseModel {
    public static final String TAG = "AlarmAnalysisModel";

    private ModelCallBack modelCallBack;
    private AlarmAnalysisService AlarmAnalysisService;

    public static final int TYPE_NUM = 1;
    public static final int TYPE_DELETE = 2;
    public static final int TYPE_ADD = 3;
    public static final int TYPE_ALL = 4;

    public AlarmAnalysisModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        AlarmAnalysisService = HttpService.getInstance().buildJsonRetrofit().create(AlarmAnalysisService.class);
    }

    /**
     * 获取警报分析数量
     * @return
     */
    public Disposable reqAnalysisNum() {
        Disposable disposable = AlarmAnalysisService.getAnalysisNum(mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Integer>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_NUM, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_NUM, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_NUM, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(Integer response) {
                        LogUtil.d(TAG, "Analysis = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_NUM, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    /**
     * 添加或修改警报分析
     * @param reqAddAlarmAnalysis
     * @return
     */
    public Disposable reqAddOrModifyAnalysis(ReqAddAlarmAnalysis reqAddAlarmAnalysis) {
        if (reqAddAlarmAnalysis == null) {
            return null;
        }
        Disposable disposable = AlarmAnalysisService.addOrModifyAnalysis(mToken, reqAddAlarmAnalysis)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Integer>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_ADD, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_ADD, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_ADD, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(Integer response) {
                        LogUtil.d(TAG, "reqAddOrModifyAnalysis result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_ADD, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    /**
     * 删除警报分析
     * @param reqDeleteAlarmAnalysis
     * @return
     */
    public Disposable reqDeleteAnalysis(ReqDeleteAlarmAnalysis reqDeleteAlarmAnalysis) {
        if (null == reqDeleteAlarmAnalysis) {
            return null;
        }

        Disposable disposable = AlarmAnalysisService.deleteAnalysis(mToken, reqDeleteAlarmAnalysis)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Boolean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_DELETE, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_DELETE, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_DELETE, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(Boolean response) {
                        LogUtil.d(TAG, "reqDeleteAnalysis result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_DELETE, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    /**
     * 获取所有警报分析
     * @param reqAllAlarmAnalysis
     * @return
     */
    public Disposable reqAllAnalysis(ReqAllAlarmAnalysis reqAllAlarmAnalysis) {
        if (null == reqAllAlarmAnalysis) {
            return null;
        }
//        reqAllAlarmAnalysis.setMachineCode(mToken);
        Disposable disposable = AlarmAnalysisService.getAllAnalysis(reqAllAlarmAnalysis)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<AlarmAnalysis>>() {
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
                    protected void onSuccess(List<AlarmAnalysis> response) {
                        LogUtil.d(TAG, "AllPipe result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_ALL, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }
}
