package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.pipelindarea.PipeLindAreaInfo;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqAddPipelindArea;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqAllPipelindArea;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqDeletePipeLindArea;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.PipeLindAreaModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class PipeblindAreaViewModel extends BaseViewModel implements ModelCallBack {

    private static final String TAG = "PipeViewModel";

    private Handler mDelivery;

    private PipeLindAreaModel pipeLindAreaModel;
    private MutableLiveData<List<PipeLindAreaInfo>> mObservableAllPipeLindArea;
    private MutableLiveData<Integer> mObservablePipeLindAreaNum;
    private MediatorLiveData<String> meObservableAddOrModifyLindArea;
    private MediatorLiveData<Boolean> mObservableLindAreaDel;

    public PipeblindAreaViewModel(@NonNull Application application) {
        super(application);
        mDelivery = new Handler(Looper.getMainLooper());

        pipeLindAreaModel = new PipeLindAreaModel(this);

        mObservableAllPipeLindArea = new MutableLiveData<>();
        mObservableAllPipeLindArea.setValue(null);

        mObservablePipeLindAreaNum = new MutableLiveData<>();
        mObservablePipeLindAreaNum.setValue(null);

        meObservableAddOrModifyLindArea = new MediatorLiveData<>();
        meObservableAddOrModifyLindArea.setValue(null);

        mObservableLindAreaDel = new MediatorLiveData<>();
        mObservableLindAreaDel.setValue(null);
    }

    /**
     * 获取管线数量
     *
     * @return
     */
    public Disposable reqPipeLindAreaNum() {
        Disposable disposable = pipeLindAreaModel.reqPipeLindAreaNum();
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 添加或修改管线
     *
     * @param reqAddPipelindArea
     * @return
     */
    public Disposable reqAddOrModifyPipeLindArea(ReqAddPipelindArea reqAddPipelindArea) {
        Disposable disposable = pipeLindAreaModel.reqAddOrModifyPipeLindArea(reqAddPipelindArea);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 删除管线
     *
     * @param reqDeletePipeLindArea
     * @return
     */
    public Disposable reqDeletePipeLindArea(ReqDeletePipeLindArea reqDeletePipeLindArea) {
        Disposable disposable = pipeLindAreaModel.reqDeletePipeLindArea(reqDeletePipeLindArea);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 获取所有管线
     *
     * @param reqAllPipelindArea
     * @return
     */
    public Disposable reqAllPipeLindArea(ReqAllPipelindArea reqAllPipelindArea) {
        Disposable disposable = pipeLindAreaModel.reqAllPipeLindArea(reqAllPipelindArea);
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
                    case PipeLindAreaModel.TYPE_ALL:
                        mObservableAllPipeLindArea.setValue((List<PipeLindAreaInfo>) typeResponse.data);
                        break;
                    case PipeLindAreaModel.TYPE_NUM:
                        mObservablePipeLindAreaNum.setValue((Integer) typeResponse.data);
                        break;
                    case PipeLindAreaModel.TYPE_ADD:
                        meObservableAddOrModifyLindArea.setValue((String)typeResponse.data);
                        break;
                    case PipeLindAreaModel.TYPE_DELETE:
                        mObservableLindAreaDel.setValue((boolean) typeResponse.data);
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

    public MutableLiveData<List<PipeLindAreaInfo>> getmObservableAllPipeLindArea() {
        return mObservableAllPipeLindArea;
    }

    public MutableLiveData<Integer> getmObservablePipeLindAreaNum() {
        return mObservablePipeLindAreaNum;
    }

    public MediatorLiveData<Boolean> getmObservableLindAreaDel() {
        return mObservableLindAreaDel;
    }

    public MediatorLiveData<String> getMeObservableAddOrModifyLindArea() {
        return meObservableAddOrModifyLindArea;
    }
}
