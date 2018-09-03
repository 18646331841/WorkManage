package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.AlarmModel;
import com.barisetech.www.workmanage.model.SiteModel;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class AlarmViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "AlarmViewModel";

    private AppDatabase appDatabase;
    private AlarmModel alarmModel;
    private Handler mDelivery;

    private LiveData<AlarmInfo> mObservableAlarmInfo;
    private LiveData<List<AlarmInfo>> mObservableAllAlarmInfos;
    private LiveData<List<AlarmInfo>> mObservableNotReadAlarmInfos;

    private MutableLiveData<Boolean> mObservableLiftAlarm;

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        appDatabase = BaseApplication.getInstance().getDatabase();
        alarmModel = new AlarmModel(appDatabase, this);
        mDelivery = new android.os.Handler(Looper.getMainLooper());

        mObservableLiftAlarm = new MutableLiveData<>();
        mObservableLiftAlarm.setValue(null);

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
        addDisposable(alarmModel.reqAlarmNum());
    }

    /**
     * 获取所有未解除警报
     */
    public void getAllUnliftAlarm() {
        addDisposable(alarmModel.reqAllAlarm());
    }

    /**
     * 通过条件筛选获取，并保存入数据库
     * @param reqAllAlarm
     */
    public void getAllAlarmByCondition(ReqAllAlarm reqAllAlarm) {
        if (null != reqAllAlarm) {
            addDisposable(alarmModel.reqAllAlarm(reqAllAlarm));
        }
    }

    /**
     * 通过条件筛选获取警报，并保存入数据库
     * @param reqAllAlarm
     */
    public void getAllAlarmByConditionToDB(ReqAllAlarm reqAllAlarm) {
        if (null != reqAllAlarm) {
            addDisposable(alarmModel.reqAllAlarmAndToDB(reqAllAlarm));
        }
    }

    public void reqLiftAlarm(int alarmId) {
        String user = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
        if (!TextUtils.isEmpty(user)) {
            addDisposable(alarmModel.reqLiftAlarm(String.valueOf(alarmId), user));
        }
    }

    /**
     * 设置警报解除
     * @param alarmId
     */
    public void setLiftAlarm(int alarmId) {
        alarmModel.liftAlarm(alarmId);
    }

    public LiveData<List<AlarmInfo>> getNotReadAlarmInfos() {
        return mObservableNotReadAlarmInfos;
    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof TypeResponse) {
            TypeResponse typeResponse = (TypeResponse) object;
            mDelivery.post(() -> {
                switch (typeResponse.type) {
                    case AlarmModel.TYPE_LIFT_ALARM:
                        mObservableLiftAlarm.setValue((Boolean) typeResponse.data);
                        break;
                }
            });
        }
    }

    @Override
    public void fail(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof FailResponse) {
            FailResponse failResponse = (FailResponse) object;
            if (failResponse.code == Config.ERROR_UNAUTHORIZED) {
                EventBus.getDefault().post(new EventBusMessage(LoginActivity.TAG));
            }
        }
    }

    public MutableLiveData<Boolean> getmObservableLiftAlarm() {
        return mObservableLiftAlarm;
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
