package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.incident.ReqAllIncident;
import com.barisetech.www.workmanage.bean.incident.ReqIncidentSelectItem;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.IncidentModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/15.
 */
public class IncidentViewModel extends BaseViewModel implements ModelCallBack{
    private static final String TAG = "IncidentViewModel";

    private AppDatabase appDatabase;
    private IncidentModel incidentModel;
    private Handler mDelivery;

    private MutableLiveData<Integer> mObservableIncidentNum;
    private MutableLiveData<List<IncidentInfo>> mObservableIncidentsList;

    private LiveData<List<IncidentInfo>> mObservableAllIncidentByRead;

    public IncidentViewModel(@NonNull Application application) {
        super(application);
        appDatabase = BaseApplication.getInstance().getDatabase();
        incidentModel = new IncidentModel(appDatabase, this);
        mDelivery = new android.os.Handler(Looper.getMainLooper());

        mObservableAllIncidentByRead = incidentModel.getIncidentInfosByRead(false);

        mObservableIncidentNum = new MutableLiveData<>();
        mObservableIncidentNum.setValue(null);
        mObservableIncidentsList = new MutableLiveData<>();
        mObservableIncidentsList.setValue(null);
    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof TypeResponse) {
            TypeResponse typeResponse = (TypeResponse) object;
            mDelivery.post(() -> {
                switch (typeResponse.type) {
                    case IncidentModel.TYPE_NUM:
                        mObservableIncidentNum.setValue((Integer) typeResponse.data);
                        break;
                    case IncidentModel.TYPE_ALL:
                        mObservableIncidentsList.setValue((List<IncidentInfo>) typeResponse.data);
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

            mDelivery.post(() -> {
                switch (failResponse.type) {
                    case IncidentModel.TYPE_ALL:
                        mObservableIncidentsList.setValue(null);
                        break;
                    case IncidentModel.TYPE_NUM:
                        mObservableIncidentNum.setValue(null);
                        break;
                }
            });
        }
    }

    /**
     * 获取事件列表并保存到数据库
     */
    public void reqAllIncidentToDB() {
        addDisposable(incidentModel.reqAllIncidentToDB());
    }

    /**
     * 获取事件列表
     * @param reqAllIncident
     * @return
     */
    public Disposable reqAllIncident(ReqAllIncident reqAllIncident) {
        if (null != reqAllIncident) {
            Disposable disposable = incidentModel.reqAllIncident(reqAllIncident);
            addDisposable(disposable);
            return disposable;
        }
        return null;
    }

    /**
     * 获取时间数量
     * @param reqIncidentSelectItem
     * @return
     */
    public Disposable reqIncidentNum(ReqIncidentSelectItem reqIncidentSelectItem) {
        if (null != reqIncidentSelectItem) {
            Disposable disposable = incidentModel.reqIncidentNum(reqIncidentSelectItem);
            addDisposable(disposable);
            return disposable;
        }
        return null;
    }

    public void reqLiftIncident(String incidentId) {
        addDisposable(incidentModel.reqLiftIncident(incidentId));
    }

    public LiveData<List<IncidentInfo>> getmObservableAllIncidentByRead() {
        return mObservableAllIncidentByRead;
    }

    public MutableLiveData<Integer> getmObservableIncidentNum() {
        return mObservableIncidentNum;
    }

    public MutableLiveData<List<IncidentInfo>> getmObservableIncidentsList() {
        return mObservableIncidentsList;
    }
}
