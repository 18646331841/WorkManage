package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.news.ReqAddNews;
import com.barisetech.www.workmanage.bean.news.ReqQuery;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.model.NewsModel;
import com.barisetech.www.workmanage.utils.LogUtil;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by LJH on 2018/8/20.
 */
public class NewsViewModel extends AndroidViewModel implements ModelCallBack {
    private static final String TAG = "NewsViewModel";

    private NewsModel newsModel;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public NewsViewModel(@NonNull Application application) {
        super(application);

        newsModel = new NewsModel(this);
    }

    public void reqNewsNum() {
        mDisposable.add(newsModel.newsNum());
    }

    public void reqNewsById(int id) {
        mDisposable.add(newsModel.getNewsById(id));
    }

    public void reqQueryNews(ReqQuery reqQuery) {
        mDisposable.add(newsModel.queryNews(reqQuery));
    }

    public void reqAddOrUpdateNews(ReqAddNews reqAddNews) {
        mDisposable.add(newsModel.addOrUpdateNews(reqAddNews));
    }

    @Override
    public void netResult(Object object) {
        if (object instanceof TypeResponse) {
            TypeResponse typeResponse = (TypeResponse) object;
            LogUtil.d(TAG, typeResponse.toString());
        }
    }

    @Override
    public void fail(int errorCode) {

    }
}
