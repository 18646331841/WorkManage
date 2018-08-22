package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.model.IncidentModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by LJH on 2018/8/15.
 */
public class IncidentViewModel extends AndroidViewModel implements ModelCallBack{
    private static final String TAG = "IncidentViewModel";

    private AppDatabase appDatabase;
    private IncidentModel incidentModel;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    private LiveData<List<IncidentInfo>> mObservableAllIncidentByRead;

    public IncidentViewModel(@NonNull Application application) {
        super(application);
        appDatabase = BaseApplication.getInstance().getDatabase();
        incidentModel = new IncidentModel(appDatabase, this);

        mObservableAllIncidentByRead = incidentModel.getIncidentInfosByRead(false);
    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));

    }

    @Override
    public void fail(int errorCode) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public void reqAllIncident() {
        mDisposable.add(incidentModel.reqAllIncident());
    }

    public void reqLiftIncident(String incidentId) {
        mDisposable.add(incidentModel.reqLiftIncident(incidentId));
    }

    public LiveData<List<IncidentInfo>> getmObservableAllIncidentByRead() {
        return mObservableAllIncidentByRead;
    }
}
