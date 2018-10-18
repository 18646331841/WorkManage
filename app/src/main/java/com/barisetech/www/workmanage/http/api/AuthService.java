package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.auth.AuthInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAllAuth;
import com.barisetech.www.workmanage.bean.auth.ReqModifyAuth;
import com.barisetech.www.workmanage.bean.onlineuser.ReqOffline;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/10/10.
 */
public interface AuthService {

    /**
     * 获取登录授权
     *
     * @param reqAllAuth
     * @return
     */
    @POST("/api/UserAuthorization")
    Observable<BaseResponse<List<AuthInfo>>> getAll(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Body ReqAllAuth
            reqAllAuth);

    /**
     * 修改登录授权
     *
     * @param token
     * @param reqModifyAuth
     * @return
     */
    @PUT("/api/UserAuthorization/{token}")
    Observable<BaseResponse<String>> modify(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token") String
            token, @Body ReqModifyAuth reqModifyAuth);
}
