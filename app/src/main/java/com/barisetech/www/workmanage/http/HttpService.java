package com.barisetech.www.workmanage.http;

import android.text.TextUtils;

import com.barisetech.www.workmanage.BuildConfig;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LJH on 2018/8/3.
 */
public class HttpService {
    private static final int DEFAULT_TIME_OUT = 20;//超时时间 10s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit mRetrofit;

    private HttpService(){

    }

    private static class SingletonHolder{
        private static final HttpService INSTANCE = new HttpService();
    }

    /**
     * 获取HttpService
     * @return
     */
    public static HttpService getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * json数据方式请求
     */
    public HttpService buildJsonRetrofit() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept-Encoding", "gzip");
        headerMap.put("Accept", "application/json");
        headerMap.put("Content-Type", "application/json; charset=utf-8");
        return buildRetrofit(headerMap);
    }

    /**
     * urlencoded方式请求
     */
    public HttpService buildUrlencodedRetrofit() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        return buildRetrofit(headerMap);
    }

    /**
     * 构建Retrofit,并设置请求头信息
     * @param headerMap 请求头Map
     * @return
     */
    public HttpService buildRetrofit(Map<String, String> headerMap) {
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间

        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }

        //项目中设置头信息
        Interceptor headerInterceptor = chain -> {
            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder();
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
            requestBuilder.method(originalRequest.method(), originalRequest.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        };

        builder.addInterceptor(headerInterceptor);

        String value = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_IP_PORT, "");
        String path;
        if (!TextUtils.isEmpty(value)) {
            String[] split = value.split("_");
            path = String.format("http://%s:%s/", split[0], split[1]);
        } else {
            path = Config.BASE_URL;
        }

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(path)
                .build();

        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }
}
