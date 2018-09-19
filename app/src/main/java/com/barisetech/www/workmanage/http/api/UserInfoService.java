package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.ReqModifyPwd;
import com.barisetech.www.workmanage.bean.ReqModifyUser;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserInfoService {


    /**
     * 修改密码
     * @param reqModifyPwd
     * @return
     */
    @POST("/api/UserData")
    Observable<BaseResponse<Boolean>>modifyPwd(@Body ReqModifyPwd reqModifyPwd);


    /**
     * 修改电话邮箱
     * @param reqModifyUser
     * @return
     */
    @POST("/api/UserData")
    Observable<BaseResponse<Boolean>>modifyUser(@Body ReqModifyUser reqModifyUser);

}
