package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.workplan.PlanBean;
import com.barisetech.www.workmanage.bean.workplan.ReqAddPlan;
import com.barisetech.www.workmanage.bean.workplan.ReqAllPlan;
import com.barisetech.www.workmanage.bean.workplan.ReqDeletePlan;
import com.barisetech.www.workmanage.bean.worktask.ReqAddTask;
import com.barisetech.www.workmanage.bean.worktask.ReqAllTask;
import com.barisetech.www.workmanage.bean.worktask.ReqDeleteTask;
import com.barisetech.www.workmanage.bean.worktask.TaskBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/9/18.
 */
public interface PlanService {
    /**
     * 获取全部计划
     *
     * @param
     * @return
     */
    @POST("/api/PipeWorkPlan")
    Observable<BaseResponse<List<PlanBean>>> getAllPlan(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Body
            ReqAllPlan reqAllPlan);

    /**
     * 获取计划数量
     *
     * @param reqPlanNum
     * @return
     */
    @GET("/api/PipeWorkPlan/{reqPlanNum}")
    Observable<BaseResponse<Integer>> getPlanNum(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("reqPlanNum")
            String reqPlanNum);

    /**
     * 添加计划
     *
     * @param token
     * @param
     * @return
     */
    @PUT("/api/PipeWorkPlan/{token}")
    Observable<BaseResponse<String>> addPlan(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token") String
            token, @Body ReqAddPlan reqAddPlan);

    /**
     * 删除计划
     *
     * @param token
     * @param
     * @return
     */
    @HTTP(method = "DELETE", path = "/api/PipeWorkPlan/{token}", hasBody = true)
    Observable<BaseResponse<Boolean>> deletePlan(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token, @Body ReqDeletePlan reqDeletePlan);
}
