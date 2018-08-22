package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.news.ReqAddNews;
import com.barisetech.www.workmanage.bean.news.ReqNewsInfos;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.NewsService;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/20.
 */
public class NewsModel {
    public static final String TAG = "NewsModel";

    public final String basePath = "api/News";

    public static final int TYPE_NUM = 1;
    public static final int TYPE_GET_NEWS = 2;
    public static final int TYPE_ADD = 3;
    public static final int TYPE_QUERY_NEWS = 4;

    private ModelCallBack modelCallBack;
    private NewsService newsService;

    public NewsModel(ModelCallBack callBack) {
        modelCallBack = callBack;
        newsService = HttpService.getInstance().buildJsonRetrofit().create(NewsService.class);
    }

    /**
     * 获取新闻数量
     * @return
     */
    public Disposable newsNum() {
        Disposable disposable = newsService.getNewsNum(Config.NEWS_BASE_URL + basePath)
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
    public Disposable getNewsById(int id) {
        Disposable disposable = newsService.getNewsById(Config.NEWS_BASE_URL + basePath + "/" + id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<NewsInfo>() {
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
                    protected void onSuccess(NewsInfo response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_GET_NEWS, response);
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
    public Disposable queryNews(ReqNewsInfos reqQuery) {
        Disposable disposable = newsService.reqNews(Config.NEWS_BASE_URL + basePath, reqQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<NewsInfo>>() {
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
                    protected void onSuccess(List<NewsInfo> response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_QUERY_NEWS, response);
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
    public Disposable addOrUpdateNews(ReqAddNews reqAddNews) {
        Disposable disposable = newsService.addOrUpdateNews(Config.NEWS_BASE_URL + basePath, reqAddNews)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Integer>() {
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
                        TypeResponse typeResponse = new TypeResponse(TYPE_ADD, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });

        return disposable;
    }
}
