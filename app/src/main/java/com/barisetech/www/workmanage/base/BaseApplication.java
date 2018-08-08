package com.barisetech.www.workmanage.base;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.barisetech.www.workmanage.bean.TokenInfo;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.api.TokenService;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/7/9.
 */
public class BaseApplication extends Application {
    public static final String TAG = "BaseApplication";
    private static BaseApplication Instance;
    public static String dataDir;
    public static String appDir = "/WorkManage";

    public TokenInfo tokenInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        dataDir = getFilesDir().getAbsolutePath() + appDir;

        getAccessToken();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getsInstance(this);
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


    public int getheight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static BaseApplication getInstance() {
        return Instance;
    }

    private void getAccessToken() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("Cache-Control", "no-cache");
        headerMap.put("Postman-Token", "d05c6ec5-6f1e-4d55-b0d7-2ccb69c40541");

        Observable<TokenInfo> cache = Observable.create(e -> {
            TokenInfo tokenInfo = getDatabase().tokenInfoDao().loadTokenInfoById(0);

            // 如果缓存数据不为空，则直接读取缓存数据，而不读取网络数据
            if (tokenInfo != null) {
                long currentTime = System.currentTimeMillis();
                long expireTime = tokenInfo.getExpiresIn() + tokenInfo.getTimestamp();
                if (currentTime < expireTime) {
                    LogUtil.e(TAG, "数据库中获取token");
                    e.onNext(tokenInfo);
                } else {
                    LogUtil.e(TAG, "token失效,网络获取access_token");
                    getDatabase().tokenInfoDao().delete(tokenInfo);
                    e.onComplete();
                }
            } else {
                LogUtil.e(TAG, "token不存在,网络获取access_token");
                e.onComplete();
            }

        });

        TokenService tokenService = HttpService.getInstance().buildRetrofit(headerMap).create(TokenService.class);
        Observable<TokenInfo> network = tokenService.getTokenInfo("password", "admin", "123456");

//        Observable<TokenInfo> tokenInfoNetwork = Observable.create(e -> {
//            Observable<TokenInfo> tokenInfo = tokenService.getTokenInfo("password", "admin", "123456");
//            tokenInfo
//        })

        Disposable disposable = Observable.concat(cache, network)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(tokenInfo -> {
                    if (tokenInfo.getTimestamp() <= 0) {
                        tokenInfo.setTimestamp(System.currentTimeMillis());
                    }
                    getDatabase().tokenInfoDao().insertTokenInfo(tokenInfo);
                    this.tokenInfo = tokenInfo;

                    LogUtil.e(TAG, tokenInfo.toString());
                }, throwable -> {
                    LogUtil.e(TAG, "获取token失败");
                });
    }
}
