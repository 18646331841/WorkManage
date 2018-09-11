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
public class PipeViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "PipeViewModel";

    private Handler mDelivery;

    private PipeModel pipeModel;
    private MutableLiveData<List<PipeInfo>> mObservableAllPipe;
    private MutableLiveData<Integer> mObservablePipeNum;
    private MutableLiveData<String> mObservableAdd;

    public PipeViewModel(@NonNull Application application) {
        super(application);
        mDelivery = new Handler(Looper.getMainLooper());

        pipeModel = new PipeModel(this);

        mObservableAllPipe = new MutableLiveData<>();
        mObservableAllPipe.setValue(null);

        mObservablePipeNum = new MutableLiveData<>();
        mObservablePipeNum.setValue(null);
        mObservableAdd = new MutableLiveData<>();
        mObservableAdd.setValue(null);
    }

    /**
     * 获取管线数量
     *
     * @return
     */
    public Disposable reqPipeNum() {
        Disposable disposable = pipeModel.reqPipeNum();
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 添加或修改管线
     *
     * @param reqAddPipe
     * @return
     */
    public Disposable reqAddOrModifyPipe(ReqAddPipe reqAddPipe) {
        Disposable disposable = pipeModel.reqAddOrModifyPipe(reqAddPipe);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 删除管线
     *
     * @param reqDeletePipe
     * @return
     */
    public Disposable reqDeletePipe(ReqDeletePipe reqDeletePipe) {
        Disposable disposable = pipeModel.reqDeletePipe(reqDeletePipe);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 获取所有管线
     *
     * @param reqAllPipe
     * @return
     */
    public Disposable reqAllPipe(ReqAllPipe reqAllPipe) {
        Disposable disposable = pipeModel.reqAllPipe(reqAllPipe);
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
                    case PipeModel.TYPE_ALL:
                        mObservableAllPipe.setValue((List<PipeInfo>) typeResponse.data);
                        break;
                    case PipeModel.TYPE_NUM:
                        mObservablePipeNum.setValue((Integer) typeResponse.data);
                        break;
                    case PipeModel.TYPE_ADD:
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
                    case PipeModel.TYPE_ALL:
                        mObservableAllPipe.setValue(null);
                        break;
                    case PipeModel.TYPE_NUM:
                        mObservablePipeNum.setValue(0);
                        break;
                    case PipeModel.TYPE_ADD:
                        mObservableAdd.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<List<PipeInfo>> getmObservableAllPipe() {
        return mObservableAllPipe;
    }

    public MutableLiveData<Integer> getmObservablePipeNum() {
        return mObservablePipeNum;
    }

    public MutableLiveData<String> getmObservableAdd() {
        return mObservableAdd;
    }
}
