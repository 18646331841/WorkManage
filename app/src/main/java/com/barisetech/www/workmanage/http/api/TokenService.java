package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.bean.TokenInfo;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by LJH on 2018/8/7.
 */
public interface TokenService {

    @FormUrlEncoded
    @POST("token")
    Observable<TokenInfo> getTokenInfo(@Field("grant_type") String grantType,
                                       @Field("username") String username,
                                       @Field("password") String password);
}
