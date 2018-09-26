package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.digitalizer.DigitalizerBean;
import com.barisetech.www.workmanage.bean.digitalizer.ReqAllDigitalizer;
import com.barisetech.www.workmanage.bean.digitalizer.ReqModifyDigitalizer;
import com.barisetech.www.workmanage.bean.site.ReqAddSite;
import com.barisetech.www.workmanage.bean.site.ReqDelSiteInfo;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DigitalizerService {

    /**
     * 获取全部数字化仪
     *
     * @param
     * @return
     */
    @POST("/api/Digitalizer/{token}")
    Observable<BaseResponse<List<DigitalizerBean>>> getAll(@Path("token") String token, @Body ReqAllDigitalizer
            reqAllDigitalizer);

    /**
     * 获取数字化仪数量
     *
     * @param token
     * @return
     */
    @GET("/api/Digitalizer/{token}")
    Observable<BaseResponse<Integer>> getNum(@Path("token") String token);

    /**
     * 修改数字化仪
     *
     * @param token
     * @param
     * @return
     */
    @PUT("/api/Digitalizer/{token}")
    Observable<BaseResponse<DigitalizerBean>> addOrModify(@Path("token") String token, @Body ReqModifyDigitalizer
            reqModifyDigitalizer);
}
