package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.auth.AuthInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAllAuth;
import com.barisetech.www.workmanage.bean.auth.ReqModifyAuth;
import com.barisetech.www.workmanage.bean.pipetap.PipeTapInfo;
import com.barisetech.www.workmanage.bean.pipetap.ReqAllPipeTap;
import com.barisetech.www.workmanage.bean.pipetap.ReqModifyPipeTap;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/10/10.
 */
public interface PipeTapService {

    /**
     * 获取阀门授权
     *
     * @param reqAllPipeTap
     * @return
     */
    @POST("/api/PipeTapAuthorization")
    Observable<BaseResponse<List<PipeTapInfo>>> getAll(@Body ReqAllPipeTap reqAllPipeTap);

    /**
     * 修改阀门授权
     * @param token
     * @param reqModifyPipeTap
     * @return
     */
    @PUT("/api/PipeTapAuthorization/{token}")
    Observable<BaseResponse<String>> modify(@Path("token") String token, @Body ReqModifyPipeTap reqModifyPipeTap);
}
