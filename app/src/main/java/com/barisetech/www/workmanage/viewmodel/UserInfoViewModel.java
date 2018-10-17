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
import com.barisetech.www.workmanage.bean.ReqModifyPwd;
import com.barisetech.www.workmanage.bean.ReqModifyUser;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.UserInfoModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class UserInfoViewModel extends BaseViewModel implements ModelCallBack {

    public static final String TAG = "UserInfoViewModel";

    private Handler mDelivery;

    private UserInfoModel userInfoModel;

    private MutableLiveData<Boolean> mObservableModifyPwd;
    private MutableLiveData<Boolean> mObservableModifyUser;


    public UserInfoViewModel(@NonNull Application application) {
        super(application);

        mDelivery = new Handler(Looper.getMainLooper());

        userInfoModel = new UserInfoModel(this);

        mObservableModifyPwd = new MutableLiveData<>();
        mObservableModifyPwd.setValue(null);

        mObservableModifyUser = new MutableLiveData<>();
        mObservableModifyUser.setValue(null);
    }


    public Disposable reqUser(ReqModifyUser reqModifyUser) {
        Disposable disposable = userInfoModel.reqModifyUser(reqModifyUser);
        addDisposable(disposable);
        return disposable;
    }


    public Disposable reqPwd(ReqModifyPwd reqModifyPwd) {
        Disposable disposable = userInfoModel.reqModifyPwd(reqModifyPwd);
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
                    case UserInfoModel.TYPE_USER:
                        mObservableModifyUser.setValue((Boolean) typeResponse.data);
                        break;
                    case UserInfoModel.TYPE_PWD:
                        mObservableModifyPwd.setValue((Boolean) typeResponse.data);
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
                    case UserInfoModel.TYPE_USER:
                        mObservableModifyUser.setValue(false);
                        break;
                    case UserInfoModel.TYPE_PWD:
                        mObservableModifyPwd.setValue(false);
                }
            });
        }
    }


    public MutableLiveData<Boolean> getmObservableModifyPwd() {
        return mObservableModifyPwd;
    }

    public MutableLiveData<Boolean> getmObservableModifyUser() {
        return mObservableModifyUser;
    }
}
