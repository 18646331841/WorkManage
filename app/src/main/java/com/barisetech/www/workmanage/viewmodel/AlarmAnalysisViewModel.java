package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAddAlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAllAlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqDeleteAlarmAnalysis;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAddPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqDeletePipe;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.AlarmAnalysisModel;
import com.barisetech.www.workmanage.model.PipeModel;
import com.barisetech.www.workmanage.model.SiteModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class AlarmAnalysisViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "AlarmAnalysisViewModel";

    private Handler mDelivery;
    private AlarmAnalysisModel alarmAnalysisModel;

    private MutableLiveData<Integer> mObservableAdd;
    private MutableLiveData<Integer> mObservableNum;
    private MutableLiveData<List<AlarmAnalysis>> mObservableAll;

    public AlarmAnalysisViewModel(@NonNull Application application) {
        super(application);
        mDelivery = new Handler(Looper.getMainLooper());

        alarmAnalysisModel = new AlarmAnalysisModel(this);

        mObservableAdd = new MutableLiveData<>();
        mObservableAdd.setValue(null);

        mObservableNum = new MutableLiveData<>();
        mObservableNum.setValue(null);

        mObservableAll = new MutableLiveData<>();
        mObservableAll.setValue(null);
    }

    /**
     * 获取警报分析数量
     *
     * @return
     */
    public Disposable reqAnalysisNum() {
        Disposable disposable = alarmAnalysisModel.reqAnalysisNum();
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 添加或修改警报分析
     *
     * @param reqAddAlarmAnalysis
     * @return
     */
    public Disposable reqAddOrModifyAnalysis(ReqAddAlarmAnalysis reqAddAlarmAnalysis) {
        Disposable disposable = alarmAnalysisModel.reqAddOrModifyAnalysis(reqAddAlarmAnalysis);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 删除警报分析
     *
     * @param reqDeleteAlarmAnalysis
     * @return
     */
    public Disposable reqDeleteAnalysis(ReqDeleteAlarmAnalysis reqDeleteAlarmAnalysis) {
        Disposable disposable = alarmAnalysisModel.reqDeleteAnalysis(reqDeleteAlarmAnalysis);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 获取所有警报分析
     *
     * @param reqAllAlarmAnalysis
     * @return
     */
    public Disposable reqAllAnalysis(ReqAllAlarmAnalysis reqAllAlarmAnalysis) {
        Disposable disposable = alarmAnalysisModel.reqAllAnalysis(reqAllAlarmAnalysis);
        addDisposable(disposable);
        return disposable;
    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof TypeResponse) {
            TypeResponse typeResponse = (TypeResponse) object;
            mDelivery.post(() -> {
                switch (typeResponse.type) {
                    case AlarmAnalysisModel.TYPE_ADD:
                        mObservableAdd.setValue((Integer) typeResponse.data);
                        break;
                    case AlarmAnalysisModel.TYPE_NUM:
                        mObservableNum.setValue((Integer) typeResponse.data);
                        break;
                    case AlarmAnalysisModel.TYPE_ALL:
                        mObservableAll.setValue((List<AlarmAnalysis>) typeResponse.data);
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
                return;
            }
            mDelivery.post(() -> {
                if (failResponse.type == AlarmAnalysisModel.TYPE_ALL) {
                    mObservableAll.setValue(null);
                }
            });

        }
    }

    public MutableLiveData<Integer> getmObservableAdd() {
        return mObservableAdd;
    }

    public MutableLiveData<Integer> getmObservableNum() {
        return mObservableNum;
    }

    public MutableLiveData<List<AlarmAnalysis>> getmObservableAll() {
        return mObservableAll;
    }
}
