package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.bean.AccessTokenInfo;
import com.barisetech.www.workmanage.bean.TokenInfo;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.api.TokenService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/9.
 */
public class LoginModel {
    private static final String TAG = "LoginModel";

    public void login(String name, String password) {

    }

    private void getAccessToken(String name, String password) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("Cache-Control", "no-cache");
        headerMap.put("Postman-Token", "d05c6ec5-6f1e-4d55-b0d7-2ccb69c40541");

//        Observable<AccessTokenInfo> cache = Observable.create(e -> {
//            AccessTokenInfo accessTokenInfo = getDatabase().tokenInfoDao().loadTokenInfoById(0);
//
//            // 如果缓存数据不为空，则直接读取缓存数据，而不读取网络数据
//            if (accessTokenInfo != null) {
//                long currentTime = System.currentTimeMillis();
//                long expireTime = accessTokenInfo.getExpiresIn() + accessTokenInfo.getTimestamp();
//                if (currentTime < expireTime) {
//                    LogUtil.e(TAG, "数据库中获取token");
//                    e.onNext(accessTokenInfo);
//                } else {
//                    LogUtil.e(TAG, "token失效,网络获取access_token");
//                    getDatabase().tokenInfoDao().delete(accessTokenInfo);
//                    e.onComplete();
//                }
//            } else {
//                LogUtil.e(TAG, "token不存在,网络获取access_token");
//                e.onComplete();
//            }
//
//        });

        TokenService tokenService = HttpService.getInstance().buildRetrofit(headerMap).create(TokenService.class);
        Observable<AccessTokenInfo> network = tokenService.getAccessTokenInfo("password", name, password);
        Disposable disposable = network.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<AccessTokenInfo>() {
                              @Override
                              public void accept(AccessTokenInfo accessTokenInfo) throws Exception {
                                  LogUtil.e(TAG, "网络获取token---" + accessTokenInfo.toString());
                                  //如果需要保存tokenInfo到数据库，再打开使用
//                        AppDatabase appDatabase = BaseApplication.getInstance().getDatabase();
//                        AccessTokenInfo tokenInfo1 = appDatabase.tokenInfoDao().loadTokenInfoById(0);
//                        if (null != tokenInfo1) {
//                            appDatabase.tokenInfoDao().delete(tokenInfo1);
//                            appDatabase.tokenInfoDao().insertTokenInfo(accessTokenInfo);
                              }
                          }
                )
                .flatMap(new Function<AccessTokenInfo, ObservableSource<TokenInfo>>() {
                    @Override
                    public ObservableSource<TokenInfo> apply(AccessTokenInfo accessTokenInfo) throws Exception {
                        if (accessTokenInfo != null && accessTokenInfo.getAccessToken() != null) {
                            headerMap.put("Authorization", "Bearer " + accessTokenInfo.getAccessToken());
                            Observable<TokenInfo> tokenInfo = HttpService.getInstance().buildRetrofit(headerMap).create
                                    (TokenService.class).getTokenInfo(accessTokenInfo.getRefreshToken());
                        }
                        return null;
                    }
                })
                .subscribe(new Consumer<TokenInfo>() {
                    @Override
                    public void accept(TokenInfo tokenInfo) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });


//        Disposable disposable = Observable.concat(cache, network)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(accessTokenInfo -> {
//                    if (accessTokenInfo.getTimestamp() <= 0) {
//                        accessTokenInfo.setTimestamp(System.currentTimeMillis());
//                    }
//                    getDatabase().tokenInfoDao().insertTokenInfo(accessTokenInfo);
//                    this.accessTokenInfo = accessTokenInfo;
//
//                    LogUtil.e(TAG, accessTokenInfo.toString());
//                }, throwable -> {
//                    LogUtil.e(TAG, "获取token失败");
//                });
    }
}
