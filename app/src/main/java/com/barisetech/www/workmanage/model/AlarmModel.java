package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.AlarmService;
import com.barisetech.www.workmanage.utils.LogUtil;

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
        this.appDatabase = appDatabase;
        modelCallBack = callBack;
    }

    public Disposable getAlarmNum() {
        if (null == mTokenInfo) {
            modelCallBack.unauthorized();
        }
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
                            modelCallBack.unauthorized();
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

//    public Disposable getAllAlarm(ReqAllAlarm reqAllAlarm) {
//        AlarmService alarmService = HttpService.getInstance().buildJsonRetrofit().create(AlarmService.class);
//
//    }
}
