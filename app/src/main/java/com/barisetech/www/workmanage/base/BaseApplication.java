package com.barisetech.www.workmanage.base;

import android.Manifest;
import android.app.Activity;
import android.app.Application;

import com.barisetech.www.workmanage.bean.TokenInfo;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.api.TokenService;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/7/9.
 */
public class BaseApplication extends Application {
    private static BaseApplication Instance;
    public static String dataDir;
    public static String appDir = "/WorkManage";

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        dataDir = getFilesDir().getAbsolutePath() + appDir;

        getAccessToken();
    }

    public void requestPermissions(Activity activity) {
        String tag = activity.getLocalClassName();
        RxPermissions rxPermission = new RxPermissions(activity);
        Disposable disposable = rxPermission
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 用户已经同意该权限
                        LogUtil.d(tag, permission.name + " is granted.");
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        LogUtil.d(tag, permission.name + " is denied. More info should be provided.");
                    } else {
                        // 用户拒绝了该权限，并且选中『不再询问』
                        LogUtil.d(tag, permission.name + " is denied.");
                    }
                });
    }

    public static BaseApplication getInstance() {
        return Instance;
    }

    private void getAccessToken() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("Cache-Control", "no-cache");
        headerMap.put("Postman-Token", "d05c6ec5-6f1e-4d55-b0d7-2ccb69c40541");

        TokenService tokenService = HttpService.getInstance().buildRetrofit(headerMap).create(TokenService.class);
        tokenService.getTokenInfo("password", "admin", "123456")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new DisposableObserver<TokenInfo>() {
                    @Override
                    public void onNext(TokenInfo tokenInfo) {
                        LogUtil.d("application", tokenInfo.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
