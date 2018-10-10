package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TokenInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAuth;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.model.LoginModel;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/9.
 */
public class LoginViewModel extends BaseViewModel implements ModelCallBack{

    private LoginModel loginModel;
    private AppDatabase appDatabase;

    private final LiveData<TokenInfo> mObservableTokenInfo;

    /**
     * 监听登录失败结果
     */
    private final MediatorLiveData<Integer> mObservableLoginFail;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        mObservableLoginFail = new MediatorLiveData<>();
        mObservableLoginFail.setValue(null);

        appDatabase = BaseApplication.getInstance().getDatabase();
        loginModel = new LoginModel(appDatabase, this);
        mObservableTokenInfo = loginModel.getTokenInfo();
    }

    public Disposable login(String name, String password) {
        Disposable disposable = loginModel.getToken(name, password);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 新登录方法
     * @param reqAuth
     * @param name
     * @param password
     * @return
     */
    public Disposable login(ReqAuth reqAuth, String name, String password) {
        Disposable disposable = loginModel.getToken(reqAuth, name, password);
        addDisposable(disposable);
        return disposable;
    }

    public LiveData<TokenInfo> getObservableTokenInfo() {
        return mObservableTokenInfo;
    }

    public LiveData<Integer> getObservableLoginFail() {
        return mObservableLoginFail;
    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
    }

    @Override
    public void fail(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof FailResponse) {
            FailResponse failResponse = (FailResponse) object;
            mObservableLoginFail.setValue(failResponse.code);
        }
    }
}
