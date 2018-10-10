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
import com.barisetech.www.workmanage.bean.map.pipe.PipeTrackInfo;
import com.barisetech.www.workmanage.bean.map.pipe.ReqPipeTrack;
import com.barisetech.www.workmanage.bean.onlineuser.ReqOffline;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.MapModel;
import com.barisetech.www.workmanage.model.OnlineUserModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/9/12.
 */
public class OnlineUserViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "OnlineUserViewModel";

    private OnlineUserModel onlineUserModel;
    private Handler mDelivery;
    private MutableLiveData<List<String>> mObservableAll;
    private MutableLiveData<Boolean> mObservableOffline;

    public OnlineUserViewModel(@NonNull Application application) {
        super(application);

        onlineUserModel = new OnlineUserModel(this);
        mDelivery = new Handler(Looper.getMainLooper());

        mObservableAll = new MutableLiveData<>();
        mObservableAll.setValue(null);
        mObservableOffline = new MutableLiveData<>();
        mObservableOffline.setValue(null);
    }

    public Disposable getAll() {
        Disposable disposable = onlineUserModel.getAll();
        addDisposable(disposable);
        return disposable;
    }

    public Disposable offlineUser(ReqOffline reqOffline) {
        if (reqOffline != null) {
            Disposable disposable = onlineUserModel.offlineUser(reqOffline);
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
                    case OnlineUserModel.TYPE_ALL:
                        mObservableAll.setValue((List<String>) typeResponse.data);
                        break;
                    case OnlineUserModel.TYPE_OFFLINE:
                        mObservableOffline.setValue((boolean) typeResponse.data);
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
                    case OnlineUserModel.TYPE_ALL:
                        mObservableAll.setValue(null);
                        break;
                    case OnlineUserModel.TYPE_OFFLINE:
                        mObservableOffline.setValue(false);
                        break;
                }
            });
        }
    }

    public MutableLiveData<List<String>> getmObservableAll() {
        return mObservableAll;
    }

    public MutableLiveData<Boolean> getmObservableOffline() {
        return mObservableOffline;
    }
}
