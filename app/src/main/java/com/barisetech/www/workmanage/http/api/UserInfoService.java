package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.ReqModifyPwd;
import com.barisetech.www.workmanage.bean.ReqModifyUser;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserInfoService {


    /**
     * 修改密码
     *
     * @param reqModifyPwd
     * @return
     */
    @POST("/api/UserData")
    Observable<BaseResponse<Boolean>> modifyPwd(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Body ReqModifyPwd
            reqModifyPwd);


    /**
     * 修改电话邮箱
     *
     * @param reqModifyUser
     * @return
     */
    @PUT("/api/UserData")
    Observable<BaseResponse<Boolean>> modifyUser(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Body ReqModifyUser
            reqModifyUser);

}
