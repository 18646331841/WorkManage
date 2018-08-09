package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.bean.TokenInfo;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.model.LoginModel;

/**
 * Created by LJH on 2018/8/9.
 */
public class LoginViewModel extends AndroidViewModel {

    private LoginModel loginModel;
    private AppDatabase appDatabase;

    public final LiveData<TokenInfo> mObservableTokenInfo;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        appDatabase = BaseApplication.getInstance().getDatabase();
        loginModel = new LoginModel(appDatabase);
        mObservableTokenInfo = appDatabase.tokenInfoDao().loadTokenInfo(0);
    }

    public void login(String name, String password) {
        loginModel.getToken(name, password);
    }

    public LiveData<TokenInfo> getObservableTokenInfo() {
        return mObservableTokenInfo;
    }
}
