package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.signin.ReqGetSite;
import com.barisetech.www.workmanage.bean.signin.ReqSignIn;
import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/9/20.
 */
public interface SignInService {
    /**
     * 得到站点
     *
     * @param
     * @return
     */
    @POST("/api/PipeTaskSite")
    Observable<BaseResponse<TaskSiteBean>> getSite(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Body ReqGetSite
            reqGetSite);

    /**
     * 打卡
     *
     * @param token
     * @param
     * @return
     */
    @PUT("/api/PipeTaskSite/{token}")
    Observable<BaseResponse<TaskSiteBean>> checkIn(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token, @Body ReqSignIn reqSignIn);
}
