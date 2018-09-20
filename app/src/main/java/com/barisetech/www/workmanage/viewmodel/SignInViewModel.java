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
import com.barisetech.www.workmanage.bean.signin.ReqGetSite;
import com.barisetech.www.workmanage.bean.signin.ReqSignIn;
import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.SignInModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class SignInViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "SignInViewModel";

    private Handler mDelivery;

    private SignInModel signInModel;
    private MutableLiveData<TaskSiteBean> mObservableGet;
    private MutableLiveData<TaskSiteBean> mObservableSignIn;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        mDelivery = new Handler(Looper.getMainLooper());

        signInModel = new SignInModel(this);

        mObservableGet = new MutableLiveData<>();
        mObservableGet.setValue(null);
        mObservableSignIn = new MutableLiveData<>();
        mObservableSignIn.setValue(null);
    }

    /**
     * 添加或修改管线
     *
     * @param reqGetSite
     * @return
     */
    public Disposable reqGetSite(ReqGetSite reqGetSite) {
        Disposable disposable = signInModel.reqGetSite(reqGetSite);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 打卡
     *
     * @param reqSignIn
     * @return
     */
    public Disposable reqSignIn(ReqSignIn reqSignIn) {
        Disposable disposable = signInModel.reqSignIn(reqSignIn);
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
                    case SignInModel.TYPE_GET:
                        mObservableGet.setValue((TaskSiteBean) typeResponse.data);
                        break;
                    case SignInModel.TYPE_CHECK:
                        mObservableSignIn.setValue((TaskSiteBean) typeResponse.data);
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
                    case SignInModel.TYPE_GET:
                        mObservableGet.setValue(null);
                        break;
                    case SignInModel.TYPE_CHECK:
                        mObservableSignIn.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<TaskSiteBean> getmObservableGet() {
        return mObservableGet;
    }

    public MutableLiveData<TaskSiteBean> getmObservableSignIn() {
        return mObservableSignIn;
    }
}
