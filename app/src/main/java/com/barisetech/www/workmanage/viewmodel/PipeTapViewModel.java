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
import com.barisetech.www.workmanage.bean.auth.AuthInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAllAuth;
import com.barisetech.www.workmanage.bean.auth.ReqModifyAuth;
import com.barisetech.www.workmanage.bean.pipetap.PipeTapInfo;
import com.barisetech.www.workmanage.bean.pipetap.ReqAllPipeTap;
import com.barisetech.www.workmanage.bean.pipetap.ReqModifyPipeTap;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.AuthModel;
import com.barisetech.www.workmanage.model.PipeTapModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/9/12.
 */
public class PipeTapViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "PipeTapViewModel";

    private PipeTapModel pipeTapModel;
    private Handler mDelivery;
    private MutableLiveData<List<PipeTapInfo>> mObservableAll;
    private MutableLiveData<String> mObservableModify;

    public PipeTapViewModel(@NonNull Application application) {
        super(application);

        pipeTapModel = new PipeTapModel(this);
        mDelivery = new Handler(Looper.getMainLooper());

        mObservableAll = new MutableLiveData<>();
        mObservableAll.setValue(null);
        mObservableModify = new MutableLiveData<>();
        mObservableModify.setValue(null);
    }

    public Disposable getAll(ReqAllPipeTap reqAllPipeTap) {
        Disposable disposable = pipeTapModel.getAll(reqAllPipeTap);
        addDisposable(disposable);
        return disposable;
    }

    public Disposable modify(ReqModifyPipeTap reqModifyPipeTap) {
        if (reqModifyPipeTap != null) {
            Disposable disposable = pipeTapModel.modify(reqModifyPipeTap);
            addDisposable(disposable);
            return disposable;
        }
        return null;
    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof TypeResponse) {
            TypeResponse typeResponse = (TypeResponse) object;
            mDelivery.post(() -> {
                switch (typeResponse.type) {
                    case PipeTapModel.TYPE_ALL:
                        mObservableAll.setValue((List<PipeTapInfo>) typeResponse.data);
                        break;
                    case PipeTapModel.TYPE_MODIFY:
                        mObservableModify.setValue((String) typeResponse.data);
                        break;
                }
            });
        }
    }

    @Override
    public void fail(Object errorObject) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (errorObject instanceof FailResponse) {
            FailResponse failResponse = (FailResponse) errorObject;
            if (failResponse.code == Config.ERROR_UNAUTHORIZED) {
                EventBus.getDefault().post(new EventBusMessage(LoginActivity.TAG));
            }

            mDelivery.post(() -> {
                switch (failResponse.type) {
                    case PipeTapModel.TYPE_ALL:
                        mObservableAll.setValue(null);
                        break;
                    case PipeTapModel.TYPE_MODIFY:
                        mObservableModify.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<List<PipeTapInfo>> getmObservableAll() {
        return mObservableAll;
    }

    public MutableLiveData<String> getmObservableModify() {
        return mObservableModify;
    }
}
