package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAddPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.pipelindarea.PipeLindAreaInfo;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqAddPipelindArea;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqAllPipelindArea;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqDeletePipeLindArea;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface PipeblindAreaService {

    /**
     * 获取管线盲区数量
     *
     * @param token
     * @return
     */
    @GET("/api/PipeblindArea/{token}")
    Observable<BaseResponse<Integer>> getPipelindAreaNum(@Path("token") String token);



    /**
     * 获取全部管线
     *
     * @param reqAllPipelindArea
     * @return
     */
    @POST("/api/PipeblindArea")
    Observable<BaseResponse<List<PipeLindAreaInfo>>> getAllPipeLindArea(@Body ReqAllPipelindArea reqAllPipelindArea);


    /**
     * 添加或修改管线
     *
     * @param token
     * @param reqAddPipelindAreaInfo
     * @return
     */
    @PUT("/api/PipeblindArea/{token}")
    Observable<BaseResponse<String>> addOrModifyPipelindArea(@Path("token") String token, @Body ReqAddPipelindArea reqAddPipelindAreaInfo);



    /**
     * 删除管线
     * @param token
     * @param reqDeletePipeLindArea
     * @return
     */
    @DELETE("/api/Pipes/{token}")
    Observable<BaseResponse<Boolean>> deletePipeLindArea(@Path("token") String token, @Body ReqDeletePipeLindArea reqDeletePipeLindArea);





}
