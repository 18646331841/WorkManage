package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.onlineuser.ReqOffline;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/10/10.
 */
public interface OnlineUserService {

    /**
     * 获取在线用户
     *
     * @param token
     * @return
     */
    @POST("/api/OnlineUser/{token}")
    Observable<BaseResponse<List<String>>> getOnlineUser(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path
            ("token") String token);

    /**
     * 强制下线用户
     *
     * @param token
     * @param reqOffline
     * @return
     */
    @HTTP(method = "DELETE", path = "/api/OnlineUser/{token}", hasBody = true)
    Observable<BaseResponse<Boolean>> offlineUser(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token, @Body ReqOffline reqOffline);
}
