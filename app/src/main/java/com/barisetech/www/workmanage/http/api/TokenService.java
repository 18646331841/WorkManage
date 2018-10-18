package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.AccessTokenInfo;
import com.barisetech.www.workmanage.bean.TokenInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAuth;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/8/7.
 */
public interface TokenService {

    /**
     * 获取access_token
     *
     * @param grantType
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("token")
    Observable<AccessTokenInfo> getAccessTokenInfo(@Field("grant_type") String grantType,
                                                   @Field("username") String username,
                                                   @Field("password") String password);

    /**
     * 获取token
     *
     * @param refreshToken
     * @return
     */
    @GET("/api/Login/{refresh_token}")
    Observable<TokenInfo> getTokenInfo(@Path("refresh_token") String refreshToken);

    /**
     * 新获取token
     *
     * @param refreshToken
     * @param reqAuth
     * @return
     */
    @POST("/api/Login/{refresh_token}")
    Observable<BaseResponse<TokenInfo>> getTokenInfo(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("refresh_token") String refreshToken, @Body ReqAuth reqAuth);

    /**
     * 刷新凭据接口
     * @param grantType
     * @param refreshToken
     * @return
     */
    @FormUrlEncoded
    @POST("token")
    Observable<AccessTokenInfo> refreshTokenInfo(@Field("grant_type") String grantType,
                                                 @Field("refresh_token") String refreshToken);
}
