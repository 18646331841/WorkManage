package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.news.ReqAddNews;
import com.barisetech.www.workmanage.bean.news.ReqNewsInfos;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.NewsService;
import com.barisetech.www.workmanage.http.api.SiteService;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/20.
 */
public class SiteModel {
    public static final String TAG = "NewsModel";

    public final String basePath = "api/Sites/";

    public static final int TYPE_NUM = 1;
    public static final int TYPE_GET_SITE = 2;
    public static final int TYPE_ADD = 3;
    public static final int TYPE_QUERY_SITE = 4;

    private ModelCallBack modelCallBack;
    private SiteService siteService;

    public SiteModel(ModelCallBack callBack) {
        modelCallBack = callBack;
        siteService = HttpService.getInstance().buildJsonRetrofit().create(SiteService.class);
    }

    /**
     * 获取新闻数量
     * @return
     */
    public Disposable siteNum() {
        Disposable disposable = siteService.getSiteNum(Config.BASE_URL + basePath)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Integer>(){
                    @Override
                    protected void onThrowable(Throwable e) {
                        modelCallBack.fail(Config.ERROR_NETWORK);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        if (response.Code == 401) {
                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
                        }
                        modelCallBack.fail(Config.ERROR_FAIL);

                    }

                    @Override
                    protected void onSuccess(Integer response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_NUM, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    /**
     * 根据Id得到某条新闻
     * @param id
     * @return
     */
    public Disposable getSiteById(int id) {
        Disposable disposable = siteService.getSiteById(Config.BASE_URL + basePath + "/" + id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<SiteBean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        modelCallBack.fail(Config.ERROR_NETWORK);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        if (response.Code == 401) {
                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
                        }
                        modelCallBack.fail(Config.ERROR_FAIL);

                    }

                    @Override
                    protected void onSuccess(SiteBean response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_GET_SITE, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });

        return disposable;
    }

    /**
     * 查询新闻
     * @param reqQuery
     * @return
     */
    public Disposable querySite(ReqSiteInfos reqQuery) {
        Disposable disposable = siteService.reqSites(Config.BASE_URL + basePath, reqQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<SiteBean>>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        modelCallBack.fail(Config.ERROR_NETWORK);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        if (response.Code == 401) {
                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
                        }
                        modelCallBack.fail(Config.ERROR_FAIL);

                    }

                    @Override
                    protected void onSuccess(List<SiteBean> response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_QUERY_SITE, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });

        return disposable;
    }

    /**
     * 添加或修改新闻
     * @param reqAddNews
     * @return
     */
//    public Disposable addOrUpdateNews(ReqAddNews reqAddNews) {
//        Disposable disposable = newsService.addOrUpdateNews(Config.NEWS_BASE_URL + basePath, reqAddNews)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribeWith(new ObserverCallBack<Integer>() {
//                    @Override
//                    protected void onThrowable(Throwable e) {
//                        modelCallBack.fail(Config.ERROR_NETWORK);
//
//                    }
//
//                    @Override
//                    protected void onFailure(BaseResponse response) {
//                        if (response.Code == 401) {
//                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
//                        }
//                        modelCallBack.fail(Config.ERROR_FAIL);
//                    }
//
//                    @Override
//                    protected void onSuccess(Integer response) {
//                        TypeResponse typeResponse = new TypeResponse(TYPE_ADD, response);
//                        modelCallBack.netResult(typeResponse);
//                    }
//                });
//
//        return disposable;
//    }
}
