package com.barisetech.www.workmanage.model;

import android.arch.lifecycle.LiveData;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.AccessTokenInfo;
import com.barisetech.www.workmanage.bean.TokenInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAuth;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.db.dao.TokenInfoDao;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.api.TokenService;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/9.
 */
public class LoginModel {
    private static final String TAG = "LoginModel";

    private AppDatabase appDatabase;
    private ModelCallBack modelCallBack;
    public LoginModel(AppDatabase appDatabase, ModelCallBack modelCallBack) {
        this.appDatabase = appDatabase;
        this.modelCallBack = modelCallBack;
    }

    public Disposable getToken(String name, String password) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("Cache-Control", "no-cache");
//        headerMap.put("Postman-Token", "d05c6ec5-6f1e-4d55-b0d7-2ccb69c40541");

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
        Disposable disposable = network
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(accessTokenInfo -> {
                    LogUtil.d(TAG, "网络获取accesstoken---" + accessTokenInfo.toString());
                    //如果需要保存tokenInfo到数据库，再打开使用
//                        AppDatabase appDatabase = BaseApplication.getInstance().getDatabase();
//                        AccessTokenInfo tokenInfo1 = appDatabase.tokenInfoDao().loadTokenInfoById(0);
//                        if (null != tokenInfo1) {
//                            appDatabase.tokenInfoDao().delete(tokenInfo1);
//                            appDatabase.tokenInfoDao().insertTokenInfo(accessTokenInfo);
                }
                )
                .flatMap((Function<AccessTokenInfo, ObservableSource<TokenInfo>>) accessTokenInfo -> {
                    if (accessTokenInfo != null && accessTokenInfo.getAccessToken() != null) {
                        LogUtil.d(TAG, "网络获取token");
                        headerMap.put("Authorization", "Bearer " + accessTokenInfo.getAccessToken());
                        Observable<TokenInfo> tokenInfo = HttpService.getInstance().buildRetrofit(headerMap).create
                                (TokenService.class).getTokenInfo(accessTokenInfo.getRefreshToken());
                        return tokenInfo;
                    }
                    return null;
                })
                .doOnNext(tokenInfo -> {
                    if (null != tokenInfo) {
                        if (tokenInfo.isLoginResult()) {
                            TokenInfoDao tokenInfoDao = appDatabase.tokenInfoDao();
                            tokenInfoDao.insert(tokenInfo);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenInfo -> {
                    if (null != tokenInfo) {
                        if (tokenInfo.isLoginResult()) {
                            LogUtil.d(TAG, "网络获取token结果---" + tokenInfo.toString());
                            //登录成功，保存用户account到SP中
                            SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_ACCOUNT, name);
                            SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_ROLE, tokenInfo.getRole().trim());
                            SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_COMPANY, tokenInfo.getCompany().trim());
                        } else {
                            modelCallBack.fail(Config.ERROR_LOGIN_FAILED);
                            LogUtil.d(TAG, "网络获取token失败---" + tokenInfo.toString());
                        }
                    } else {
                        modelCallBack.fail(Config.ERROR_LOGIN_FAILED);
                    }
                }, throwable -> {
                    if (throwable.getMessage().contains("400")) {
                        modelCallBack.fail(Config.ERROR_LOGIN_FAILED);
                    } else {
                        modelCallBack.fail(Config.ERROR_NETWORK);
                    }
                    LogUtil.e(TAG, "网络获取token失败---throwable" + throwable.getMessage());
                });

        return disposable;
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

    /**
     * 新登录请求
     *
     * @param reqAuth
     * @param name
     * @param password
     * @return
     */
    public Disposable getToken(ReqAuth reqAuth, String name, String password) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept-Encoding", "gzip");
        headerMap.put("Accept", "application/json");
        headerMap.put("Content-Type", "application/json; charset=utf-8");

        TokenService tokenService = HttpService.getInstance().buildRetrofit(headerMap).create(TokenService.class);
        Observable<AccessTokenInfo> network = tokenService.getAccessTokenInfo("password", name, password);
        Disposable disposable = network
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(accessTokenInfo -> {
                    LogUtil.d(TAG, "网络获取accesstoken---" + accessTokenInfo.toString());
                    //如果需要保存tokenInfo到数据库，再打开使用
//                        AppDatabase appDatabase = BaseApplication.getInstance().getDatabase();
//                        AccessTokenInfo tokenInfo1 = appDatabase.tokenInfoDao().loadTokenInfoById(0);
//                        if (null != tokenInfo1) {
//                            appDatabase.tokenInfoDao().delete(tokenInfo1);
//                            appDatabase.tokenInfoDao().insertTokenInfo(accessTokenInfo);
                }
                )
                .flatMap((Function<AccessTokenInfo, ObservableSource<BaseResponse<TokenInfo>>>) accessTokenInfo -> {
                    if (accessTokenInfo != null && accessTokenInfo.getAccessToken() != null) {
                        LogUtil.d(TAG, "网络获取token");
                        headerMap.put("Authorization", "Bearer " + accessTokenInfo.getAccessToken());
                        reqAuth.Token = accessTokenInfo.getRefreshToken();
                        return HttpService.getInstance().buildRetrofit(headerMap).create
                                (TokenService.class).getTokenInfo(accessTokenInfo.getRefreshToken(), reqAuth)
                                .onErrorReturnItem(new BaseResponse<>(-1, "", null));
                    }
                    return null;
                })
                .map(tokenInfoBaseResponse -> {
                    if (tokenInfoBaseResponse != null) {
                        int code = tokenInfoBaseResponse.Code;
                        if (code == 200) {
                            return tokenInfoBaseResponse.Response;
                        }
                    }
                    return null;
                })
                .doOnNext(tokenInfo -> {
                    if (null != tokenInfo) {
                        if (tokenInfo.isLoginResult()) {
                            TokenInfoDao tokenInfoDao = appDatabase.tokenInfoDao();
                            tokenInfoDao.insert(tokenInfo);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenInfo -> {
                    if (null != tokenInfo) {
                        if (tokenInfo.isLoginResult()) {
                            LogUtil.d(TAG, "网络获取token结果---" + tokenInfo.toString());
                            //登录成功，保存用户account到SP中
                            SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_ACCOUNT, name);
                            SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_ROLE, tokenInfo.getRole().trim());
                            SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_COMPANY, tokenInfo.getCompany().trim());
                        } else {
                            modelCallBack.fail(Config.ERROR_LOGIN_FAILED);
                            LogUtil.d(TAG, "网络获取token失败---" + tokenInfo.toString());
                        }
                    } else {
                        modelCallBack.fail(Config.ERROR_LOGIN_FAILED);
                    }
                }, throwable -> {
                    if (throwable.getMessage().contains("400")) {
                        modelCallBack.fail(Config.ERROR_LOGIN_FAILED);
                    } else {
                        modelCallBack.fail(Config.ERROR_NETWORK);
                    }
                    LogUtil.e(TAG, "网络获取token失败---throwable" + throwable.getMessage());
                });

        return disposable;
    }

    public LiveData<TokenInfo> getTokenInfo() {
        return appDatabase.tokenInfoDao().loadTokenInfo(0);
    }
}
