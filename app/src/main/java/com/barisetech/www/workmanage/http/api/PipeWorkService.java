package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAddPC;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.bean.pipecollections.ReqDeletePc;
import com.barisetech.www.workmanage.bean.pipework.PipeWork;
import com.barisetech.www.workmanage.bean.pipework.ReqAddPW;
import com.barisetech.www.workmanage.bean.pipework.ReqAllPW;
import com.barisetech.www.workmanage.bean.pipework.ReqDeletePW;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/8/10.
 */
public interface PipeWorkService {

    /**
     * 获取全部管线工况
     *
     * @param reqAllPW
     * @return
     */
    @POST("/api/PipeWork")
    Observable<BaseResponse<List<PipeWork>>> getAllPw(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Body ReqAllPW
            reqAllPW);

    /**
     * 获取管线工况数量
     *
     * @param token
     * @return
     */
    @GET("/api/PipeWork/{token}")
    Observable<BaseResponse<Integer>> getPwNum(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token") String
            token);

    /**
     * 添加或修改管线工况
     *
     * @param token
     * @param reqAddPW
     * @return
     */
    @PUT("/api/PipeWork/{token}")
    Observable<BaseResponse<String>> addOrModifyPw(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token, @Body ReqAddPW reqAddPW);

    /**
     * 删除管线工况
     *
     * @param token
     * @param reqDeletePW
     * @return
     */
    @HTTP(method = "DELETE", path = "/api/PipeWork/{token}", hasBody = true)
    Observable<BaseResponse<Boolean>> deletePw(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token") String
            token, @Body ReqDeletePW reqDeletePW);
}
