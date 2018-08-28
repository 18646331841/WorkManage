package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfoNewest;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAddPC;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.bean.pipecollections.ReqDeletePc;

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
public interface PipeCollectionsService {

    /**
     * 获取全部管线集合
     *
     * @param reqAllPc
     * @return
     */
    @POST("/api/Pipecollections")
    Observable<BaseResponse<List<PipeCollections>>> getAllPc(@Body ReqAllPc reqAllPc);

    /**
     * 获取管线集合数量
     *
     * @param token
     * @return
     */
    @GET("/api/Pipecollections/{token}")
    Observable<BaseResponse<Integer>> getPcNum(@Path("token") String token);

    /**
     * 添加或修改管线集合
     *
     * @param token
     * @param reqAddPC
     * @return
     */
    @PUT("/api/Pipecollections/{token}")
    Observable<BaseResponse<String>> addOrModifyPc(@Path("token") String token, @Body ReqAddPC reqAddPC);

    /**
     * 删除管线集合
     * @param token
     * @param reqDeletePc
     * @return
     */
    @DELETE("/api/Pipecollections/{token}")
    Observable<BaseResponse<Boolean>> deletePc(@Path("token") String token, @Body ReqDeletePc reqDeletePc);
}
