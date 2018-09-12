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
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAddPC;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.bean.pipecollections.ReqDeletePc;
import com.barisetech.www.workmanage.bean.pipework.PipeWork;
import com.barisetech.www.workmanage.bean.pipework.ReqAddPW;
import com.barisetech.www.workmanage.bean.pipework.ReqAllPW;
import com.barisetech.www.workmanage.bean.pipework.ReqDeletePW;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.PipeCollectionModel;
import com.barisetech.www.workmanage.model.PipeWorkModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class PipeWorkViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "PipeWorkViewModel";

    private PipeWorkModel pipeWorkModel;
    private Handler mDelivery;

    private MutableLiveData<List<PipeWork>> mObservableAllPW;
    private MutableLiveData<Integer> mObservableNum;
    private MutableLiveData<String> mObservableAdd;

    public PipeWorkViewModel(@NonNull Application application) {
        super(application);
        pipeWorkModel = new PipeWorkModel(this);
        mDelivery = new Handler(Looper.getMainLooper());

        mObservableAllPW = new MutableLiveData<>();
        mObservableAllPW.setValue(null);
        mObservableNum = new MutableLiveData<>();
        mObservableNum.setValue(null);
        mObservableAdd = new MutableLiveData<>();
        mObservableAdd.setValue(null);
    }

    /**
     * 获取管线工况数量
     * @return
     */
    public Disposable reqPwNum() {
        Disposable disposable = pipeWorkModel.reqPipeWorkNum();
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 添加或修改管线工况集合
     * @param reqAddPW
     * @return
     */
    public Disposable reqAddOrModifyPw(ReqAddPW reqAddPW) {
        Disposable disposable = pipeWorkModel.reqAddOrModifyPipeWork(reqAddPW);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 删除管线工况集合
     * @param reqDeletePW
     * @return
     */
    public Disposable reqDeletePw(ReqDeletePW reqDeletePW) {
        Disposable disposable = pipeWorkModel.reqDeletePipeWork(reqDeletePW);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 获取所有管线工况集合
     * @param reqAllPW
     * @return
     */
    public Disposable reqAllPw(ReqAllPW reqAllPW) {
        Disposable disposable = pipeWorkModel.reqAllPipeWork(reqAllPW);
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
                    case PipeWorkModel.TYPE_ALL:
                        mObservableAllPW.setValue((List<PipeWork>) typeResponse.data);
                        break;
                    case PipeWorkModel.TYPE_NUM:
                        mObservableNum.setValue((Integer) typeResponse.data);
                        break;
                    case PipeWorkModel.TYPE_ADD:
                        mObservableAdd.setValue((String) typeResponse.data);
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
                    case PipeWorkModel.TYPE_ALL:
                        mObservableAllPW.setValue(null);
                        break;
                    case PipeWorkModel.TYPE_NUM:
                        mObservableNum.setValue(null);
                        break;
                    case PipeWorkModel.TYPE_ADD:
                        mObservableAdd.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<Integer> getmObservableNum() {
        return mObservableNum;
    }

    public MutableLiveData<List<PipeWork>> getmObservableAllPW() {
        return mObservableAllPW;
    }

    public MutableLiveData<String> getmObservableAdd() {
        return mObservableAdd;
    }
}
