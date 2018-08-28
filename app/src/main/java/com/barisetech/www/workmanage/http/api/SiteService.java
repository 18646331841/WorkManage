package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.news.ReqNewsInfos;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SiteService {

    /*
    * 获取站点信息数量
    * */
    @GET
    Observable<BaseResponse<Integer>> getSiteNum(@Url String url);

    /*
    * 根据站点id获取信息
    * */
    @GET
    Observable<BaseResponse<SiteBean>> getSiteById(@Url String url);


    /*
    * 分页查询
    * */
    @POST
    Observable<BaseResponse<List<SiteBean>>> reqSites(@Url String url, @Body ReqSiteInfos reqQuery);



}
