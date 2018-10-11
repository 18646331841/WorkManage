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
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.AuthModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/9/12.
 */
public class AuthViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "AuthViewModel";

    private AuthModel authModel;
    private Handler mDelivery;
    private MutableLiveData<List<AuthInfo>> mObservableAll;
    private MutableLiveData<String> mObservableModify;

    public AuthViewModel(@NonNull Application application) {
        super(application);

        authModel = new AuthModel(this);
        mDelivery = new Handler(Looper.getMainLooper());

        mObservableAll = new MutableLiveData<>();
        mObservableAll.setValue(null);
        mObservableModify = new MutableLiveData<>();
        mObservableModify.setValue(null);
    }

    public Disposable getAll(ReqAllAuth reqAllAuth) {
        Disposable disposable = authModel.getAll(reqAllAuth);
        addDisposable(disposable);
        return disposable;
    }

    public Disposable modify(ReqModifyAuth reqModifyAuth) {
        if (reqModifyAuth != null) {
            Disposable disposable = authModel.modify(reqModifyAuth);
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
                    case AuthModel.TYPE_ALL:
                        mObservableAll.setValue((List<AuthInfo>) typeResponse.data);
                        break;
                    case AuthModel.TYPE_MODIFY:
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
                    case AuthModel.TYPE_ALL:
                        mObservableAll.setValue(null);
                        break;
                    case AuthModel.TYPE_MODIFY:
                        mObservableModify.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<List<AuthInfo>> getmObservableAll() {
        return mObservableAll;
    }

    public MutableLiveData<String> getmObservableModify() {
        return mObservableModify;
    }
}
