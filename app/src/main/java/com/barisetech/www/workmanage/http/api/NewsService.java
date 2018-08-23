package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.news.ReqAddNews;
import com.barisetech.www.workmanage.bean.news.ReqNewsInfos;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

/**
 * Created by LJH on 2018/8/10.
 */
public interface NewsService {

    /**
     * 获取新闻数量
     * @param url
     * @return
     */
    @GET
    Observable<BaseResponse<Integer>> getNewsNum(@Url String url);

    /**
     * 根据Id得到某条信息
     * @param url
     * @return
     */
    @GET
    Observable<BaseResponse<NewsInfo>> getNewsById(@Url String url);

    /**
     * 分页查询新闻
     * @param url
     * @param reqQuery
     * @return
     */
    @POST
    Observable<BaseResponse<List<NewsInfo>>> reqNews(@Url String url, @Body ReqNewsInfos reqQuery);

    /**
     * 增加或修改新闻
     * @param url
     * @param reqAddNews
     * @return 失败返回数量为0
     */
    @PUT
    Observable<BaseResponse<Integer>> addOrUpdateNews(@Url String url, @Body ReqAddNews reqAddNews);
}
