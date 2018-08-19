package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.AlarmModel;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
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

    private LiveData<AlarmInfo> mObservableAlarmInfo;
    private LiveData<List<AlarmInfo>> mObservableAllAlarmInfos;
    private LiveData<List<AlarmInfo>> mObservableNotReadAlarmInfos;

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        appDatabase = BaseApplication.getInstance().getDatabase();
        alarmModel = new AlarmModel(appDatabase, this);

        mObservableAllAlarmInfos = alarmModel.getAllAlarmInfo();
        mObservableNotReadAlarmInfos = alarmModel.getAlarmInfosByRead(false);
    }

    public AlarmViewModel(@NonNull Application application, int alarmId) {
        super(application);
        appDatabase = BaseApplication.getInstance().getDatabase();
        alarmModel = new AlarmModel(appDatabase, this);

        mObservableAlarmInfo = alarmModel.getAlarmInfo(alarmId);
//        mObservableAllAlarmInfos = alarmModel.getAllAlarmInfo();
//        mObservableNotReadAlarmInfos = alarmModel.getAlarmInfosByRead(false);
    }

    public void getAlarmNum() {
        mDisposable.add(alarmModel.reqAlarmNum());
    }

    public void getAllAlarm() {
        mDisposable.add(alarmModel.reqAllAlarm());
    }

    public void reqLiftAlarm(int alarmId) {
        String user = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
        if (!TextUtils.isEmpty(user)) {
            mDisposable.add(alarmModel.reqLiftAlarm(String.valueOf(alarmId), user));
        }
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

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mAlarmId;

        public Factory(@NonNull Application application, int alarmId) {
            mApplication = application;
            mAlarmId = alarmId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AlarmViewModel(mApplication, mAlarmId);
        }
    }

    public LiveData<List<AlarmInfo>> getmObservableAllAlarmInfos() {
        return mObservableAllAlarmInfos;
    }

    public LiveData<AlarmInfo> getmObservableAlarmInfo() {
        return mObservableAlarmInfo;
    }
}
