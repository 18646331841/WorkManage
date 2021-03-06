package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.site.ReqAddSite;
import com.barisetech.www.workmanage.bean.site.ReqDelSiteInfo;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;

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
import retrofit2.http.Url;

public interface SiteService {

    /**
     * 获取全部站点
     *
     * @param
     * @return
     */
    @POST("/api/Sites")
    Observable<BaseResponse<List<SiteBean>>> getAllSite(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Body
            ReqSiteInfos reqAllSite);

    /**
     * 获取站点
     *
     * @param token
     * @return
     */
    @GET("/api/Sites/{token}")
    Observable<BaseResponse<Integer>> getSiteNum(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token);

    /**
     * 添加或修改站点
     *
     * @param token
     * @param
     * @return
     */
    @PUT("/api/Sites/{token}")
    Observable<BaseResponse<String>> addOrModifySite(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token, @Body ReqAddSite reqAddSite);

    /**
     * 删除站点
     *
     * @param token
     * @param
     * @return
     */
    @HTTP(method = "DELETE", path = "/api/Sites/{token}", hasBody = true)
    Observable<BaseResponse<Boolean>> deleteSite(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token, @Body ReqDelSiteInfo reqDelSiteInfo);


}
