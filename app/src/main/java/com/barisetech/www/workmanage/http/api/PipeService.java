package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqDeletePipe;
import com.barisetech.www.workmanage.bean.pipe.ReqAddPipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/8/10.
 */
public interface PipeService {

    /**
     * 获取全部管线
     *
     * @param reqAllPipe
     * @return
     */
    @POST("/api/Pipes")//TODO 返回对象需要修改
    Observable<BaseResponse<List<PipeInfo>>> getAllPipe(@Body ReqAllPipe reqAllPipe);

    /**
     * 获取管线数量
     *
     * @param token
     * @return
     */
    @GET("/api/Pipes/{token}")
    Observable<BaseResponse<Integer>> getPipeNum(@Path("token") String token);

    /**
     * 添加或修改管线
     *
     * @param token
     * @param reqPipeInfo
     * @return
     */
    @PUT("/api/Pipes/{token}")
    Observable<BaseResponse<String>> addOrModifyPipe(@Path("token") String token, @Body ReqAddPipe reqPipeInfo);

    /**
     * 删除管线
     * @param token
     * @param reqDeletePipe
     * @return
     */
    @DELETE("/api/Pipes/{token}")
    Observable<BaseResponse<Boolean>> deletePipe(@Path("token") String token, @Body ReqDeletePipe reqDeletePipe);
}
