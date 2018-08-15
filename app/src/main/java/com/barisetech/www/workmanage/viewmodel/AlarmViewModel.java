package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.AlarmModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class AlarmViewModel extends AndroidViewModel implements ModelCallBack {
    private static final String TAG = "AlarmViewModel";

    private AppDatabase appDatabase;
    private AlarmModel alarmModel;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    private LiveData<List<AlarmInfo>> mObservableAllAlarmInfos;
    private LiveData<List<AlarmInfo>> mObservableNotReadAlarmInfos;

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        appDatabase = BaseApplication.getInstance().getDatabase();
        alarmModel = new AlarmModel(appDatabase, this);

        mObservableAllAlarmInfos = alarmModel.getAllAlarmInfo();
        mObservableNotReadAlarmInfos = alarmModel.getAlarmInfosByRead(false);
    }

    public void getAlarmNum() {
        mDisposable.add(alarmModel.reqAlarmNum());
    }

    public void getAllAlarm() {
        mDisposable.add(alarmModel.reqAllAlarm());
    }

    public LiveData<List<AlarmInfo>> getNotReadAlarmInfos() {
        return mObservableNotReadAlarmInfos;
    }

    @Override
    public void netResult(Object object) {

    }

    @Override
    public void fail(int errorCode) {
        if (errorCode == Config.ERROR_UNAUTHORIZED) {
            EventBus.getDefault().post(new EventBusMessage(LoginActivity.TAG));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public LiveData<List<AlarmInfo>> getmObservableAllAlarmInfos() {
        return mObservableAllAlarmInfos;
    }
}
