package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.adapter.SiteAdapter;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfoNewest;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.alarm.ReqLiftAlarm;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.news.ReqNewsInfos;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAddPC;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.bean.pipecollections.ReqDeletePc;
import com.barisetech.www.workmanage.bean.site.ReqAddSite;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface SiteService {

    /**
     * 获取全部管线集合
     *
     * @param
     * @return
     */
    @POST("/api/Sites")
    Observable<BaseResponse<List<SiteBean>>> getAllSite(@Body ReqSiteInfos reqAllSite);

    /**
     * 获取管线集合数量
     *
     * @param token
     * @return
     */
    @GET("/api/Sites/{token}")
    Observable<BaseResponse<Integer>> getSiteNum(@Path("token") String token);

    /**
     * 添加或修改管线集合
     *
     * @param token
     * @param
     * @return
     */
    @PUT("/api/Sites/{token}")
     Observable<BaseResponse<String>> addOrModifySite(@Path("token") String token, @Body ReqAddSite siteBean);
//
//    /**
//     * 删除管线集合
//     * @param token
//     * @param reqDeletePc
//     * @return
//     */
//    @DELETE("/api/Pipecollections/{token}")
//    Observable<BaseResponse<Boolean>> deletePc(@Path("token") String token, @Body ReqDeletePc reqDeletePc);


}
