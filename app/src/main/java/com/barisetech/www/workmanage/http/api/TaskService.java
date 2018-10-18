package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.site.ReqAddSite;
import com.barisetech.www.workmanage.bean.site.ReqDelSiteInfo;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
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
public interface TaskService {
    /**
     * 获取全部任务
     *
     * @param
     * @return
     */
    @POST("/api/PipeWorkTask")
    Observable<BaseResponse<List<TaskBean>>> getAllTask(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Body
            ReqAllTask reqAllTask);

    /**
     * 获取任务数量
     *
     * @param reqTaskNum
     * @return
     */
    @GET("/api/PipeWorkTask/{reqTaskNum}")
    Observable<BaseResponse<Integer>> getTaskNum(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("reqTaskNum")
            String reqTaskNum);

    /**
     * 添加任务
     *
     * @param token
     * @param
     * @return
     */
    @PUT("/api/PipeWorkTask/{token}")
    Observable<BaseResponse<String>> addTask(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token") String
            token, @Body ReqAddTask reqAddTask);

    /**
     * 删除站点
     *
     * @param token
     * @param
     * @return
     */
    @HTTP(method = "DELETE", path = "/api/PipeWorkTask/{token}", hasBody = true)
    Observable<BaseResponse<Boolean>> deleteTask(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token, @Body ReqDeleteTask reqDeleteTask);
}
