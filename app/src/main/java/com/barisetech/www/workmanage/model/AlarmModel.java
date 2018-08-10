package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.AlarmService;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/10.
 */
public class AlarmModel {
    public static final String TAG = "AlarmModel";

    private AppDatabase appDatabase;


    public AlarmModel(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

//    public int getAlarmNum() {
//        AlarmService alarmService = HttpService.getInstance().buildUrlencodedRetrofit().create(AlarmService.class);
//        alarmService.getAlarmNum(BaseApplication.getInstance().curTokenInfo.getToken())
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe();
//    }
}
