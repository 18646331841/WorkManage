package com.barisetech.www.workmanage.http;

/**
 * Created by LJH on 2018/7/9.
 */
public class Config {
    //默认的
    public static final String BASE_URL = "http://www.barisetech.com:8081/";

    /**
     * 网络请求异常码
     */
    public static final int ERROR_UNAUTHORIZED = 401; //认证失败
    public static final int ERROR_LOGIN_FAILED = 400; //登录失败
    public static final int ERROR_NETWORK = 500; //网络异常
}
