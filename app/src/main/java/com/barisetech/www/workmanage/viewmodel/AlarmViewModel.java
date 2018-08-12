package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.model.AlarmModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class AlarmViewModel extends AndroidViewModel implements ModelCallBack{
    private static final String TAG = "AlarmViewModel";

    private AppDatabase appDatabase;
    private AlarmModel alarmModel;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        appDatabase = BaseApplication.getInstance().getDatabase();
        alarmModel = new AlarmModel(appDatabase, this);
    }

    public void getAlarmNum() {
        mDisposable.add(alarmModel.getAlarmNum());
    }

    public void getAllAlarm() {
        mDisposable.add(alarmModel.getAllAlarm());
    }

    @Override
    public void unauthorized() {
        EventBus.getDefault().post(new EventBusMessage(LoginActivity.TAG));
    }

    @Override
    public void netResult(Object object) {

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
