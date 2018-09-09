package com.barisetech.www.workmanage.model;

import android.arch.lifecycle.LiveData;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfoNewest;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.alarm.ReqLiftAlarm;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.AlarmService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/10.
 */
public class AlarmModel extends BaseModel {
    public static final String TAG = "AlarmModel";

    private AppDatabase appDatabase;
    private ModelCallBack modelCallBack;

    public static final int TYPE_NUM = 1;
    public static final int TYPE_NEW_ALARM = 2;
    public static final int TYPE_UNLIFT_ALARM = 3;
    public static final int TYPE_All_ALARM = 4;
    public static final int TYPE_LIFT_ALARM = 5;
    public static final int TYPE_All_ALARM_DB = 6;

    public AlarmModel(AppDatabase appDatabase, ModelCallBack callBack) {
        super(callBack);
        this.appDatabase = appDatabase;
        modelCallBack = callBack;
    }

    /**
     * 获取警报数量
     *
     * @return
     */
    public Disposable reqAlarmNum() {
        AlarmService alarmService = HttpService.getInstance().buildUrlencodedRetrofit().create(AlarmService.class);
        Disposable disposable = alarmService.getAlarmNum(mToken)
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
                        LogUtil.d(TAG, "AlarmNum = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_NUM, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    /**
     * 获取所有未解除警报
     *
     * @return
     */
    public Disposable reqAllAlarm() {
        ReqAllAlarm reqAllAlarm = new ReqAllAlarm();
        reqAllAlarm.setMachineCode(mToken);
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
                        FailResponse failResponse = new FailResponse(TYPE_UNLIFT_ALARM, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_UNLIFT_ALARM, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_UNLIFT_ALARM, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(List<AlarmInfo> response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_UNLIFT_ALARM, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    /**
     * 通过参数筛选获取警报
     *
     * @return
     */
    public Disposable reqAllAlarm(ReqAllAlarm reqAllAlarm) {
        reqAllAlarm.setMachineCode(mToken);
        AlarmService alarmService = HttpService.getInstance().buildJsonRetrofit().create(AlarmService.class);
        Disposable disposable = alarmService.getAllAlarm(reqAllAlarm)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<AlarmInfo>>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_All_ALARM, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_All_ALARM, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_All_ALARM, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(List<AlarmInfo> response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_All_ALARM, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    /**
     * 通过参数筛选获取警报并存储在数据库中
     *
     * @return
     */
    public Disposable reqAllAlarmAndToDB(ReqAllAlarm reqAllAlarm) {
        reqAllAlarm.setMachineCode(mToken);
        AlarmService alarmService = HttpService.getInstance().buildJsonRetrofit().create(AlarmService.class);
        Disposable disposable = alarmService.getAllAlarm(reqAllAlarm)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<AlarmInfo>>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_All_ALARM_DB, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_All_ALARM_DB, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_All_ALARM_DB, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(List<AlarmInfo> response) {
                        for (int i = 0; i < response.size(); i++) {
                            AlarmInfo alarmInfo = appDatabase.alarmInfoDao().getAlarmInfoSync(response.get(i).getKey());
                            if (null != alarmInfo) {
                                response.get(i).setRead(alarmInfo.isRead());
                                LogUtil.d(TAG, "response is read = " + alarmInfo.toString());
                            }
                        }
                        appDatabase.alarmInfoDao().insertAll(response);
                    }
                });
        return disposable;
    }

    /**
     * 获取最新警报信息
     *
     * @return
     */
    public Disposable reqAlarmNewest() {
        AlarmService alarmService = HttpService.getInstance().buildUrlencodedRetrofit().create(AlarmService.class);
        Disposable disposable = alarmService.getAlarmInfoNewest(mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<AlarmInfoNewest>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_NEW_ALARM, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_NEW_ALARM, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_NEW_ALARM, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(AlarmInfoNewest response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_NEW_ALARM, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    public Disposable reqLiftAlarm(String alarmId, String user) {
        ReqLiftAlarm reqLiftAlarm = new ReqLiftAlarm();
        reqLiftAlarm.setAlarmId(alarmId);
        reqLiftAlarm.setLiftUser(user);

        AlarmService alarmService = HttpService.getInstance().buildJsonRetrofit().create(AlarmService.class);
        Disposable disposable = alarmService.reqLiftAlarm(mToken, reqLiftAlarm)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Boolean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_LIFT_ALARM, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_LIFT_ALARM, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_LIFT_ALARM, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(Boolean response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_LIFT_ALARM, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });

        return disposable;
    }

    public LiveData<List<AlarmInfo>> getAllAlarmInfo() {
        return appDatabase.alarmInfoDao().getAllAlarmInfo();
    }

    public LiveData<List<AlarmInfo>> getAlarmInfosByRead(boolean isRead) {
        return appDatabase.alarmInfoDao().getAlarmInfosByRead(isRead);
    }

    /**
     * 解除警报
     *
     * @param alarmInfo
     */
    public void liftAlarm(AlarmInfo alarmInfo) {
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                alarmInfo.setLifted(true);
                appDatabase.alarmInfoDao().updateAlarmLift(alarmInfo);
                LogUtil.d(TAG, "alarmInfo = " + alarmInfo.getKey() + " is lifted");

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<AlarmInfo> getAlarmInfo(int key) {
        return appDatabase.alarmInfoDao().getAlarmInfo(key);
    }

    public void readedAlarm(int key) {
        Disposable subscribe = appDatabase.alarmInfoDao().getAlarmInfoRxjava(key)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(alarmInfo -> {
                    if (alarmInfo != null) {
                        alarmInfo.setRead(true);
                        LogUtil.d(TAG, "read alarm = " + key);
                        appDatabase.alarmInfoDao().updateAlarmLift(alarmInfo);
                    } else {
                        LogUtil.d(TAG, "read alarminfo is null");
                    }
                });
    }
}
