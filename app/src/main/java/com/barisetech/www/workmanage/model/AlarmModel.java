package com.barisetech.www.workmanage.model;

import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfoNewest;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.AlarmService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/10.
 */
public class AlarmModel extends BaseModel{
    public static final String TAG = "AlarmModel";

    private AppDatabase appDatabase;
    private ModelCallBack modelCallBack;

    public AlarmModel(AppDatabase appDatabase, ModelCallBack callBack) {
        super(callBack);
        this.appDatabase = appDatabase;
        modelCallBack = callBack;
    }

    /**
     * 获取警报数量
     * @return
     */
    public Disposable getAlarmNum() {
        AlarmService alarmService = HttpService.getInstance().buildUrlencodedRetrofit().create(AlarmService.class);
        Disposable disposable = alarmService.getAlarmNum(mTokenInfo.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Integer>(){
                    @Override
                    protected void onThrowable(Throwable e) {

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        if (response.Code == 401) {
                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
                        }
                    }

                    @Override
                    protected void onSuccess(Integer response) {
                        LogUtil.d(TAG, "AlarmNum = " + response);
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }

    /**
     * 获取所有警报
     * @return
     */
    public Disposable getAllAlarm() {
        ReqAllAlarm reqAllAlarm = new ReqAllAlarm();
        reqAllAlarm.setMachineCode(mTokenInfo.getToken());
        reqAllAlarm.setIsAllAlarm("false");
        reqAllAlarm.setStartIndex("1");
        reqAllAlarm.setNumberOfRecords("1");

        AlarmService alarmService = HttpService.getInstance().buildJsonRetrofit().create(AlarmService.class);
        Disposable disposable = alarmService.getAllAlarm(reqAllAlarm)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<AlarmInfo>>() {
                    @Override
                    protected void onThrowable(Throwable e) {

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        if (response.Code == 401) {
                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
                        }
                    }

                    @Override
                    protected void onSuccess(List<AlarmInfo> response) {
                        modelCallBack.netResult(response);
                        appDatabase.alarmInfoDao().insertAll(response);
                    }
                });
        return disposable;
    }

    /**
     * 获取最新警报信息
     * @return
     */
    public Disposable getAlarmNewest() {
        AlarmService alarmService = HttpService.getInstance().buildUrlencodedRetrofit().create(AlarmService.class);
        Disposable disposable = alarmService.getAlarmInfoNewest(mTokenInfo.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<AlarmInfoNewest>() {
                    @Override
                    protected void onThrowable(Throwable e) {

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        if (response.Code == 401) {
                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
                        }
                    }

                    @Override
                    protected void onSuccess(AlarmInfoNewest response) {
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }
}
