package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.news.ReqAddNews;
import com.barisetech.www.workmanage.bean.news.ReqNewsInfos;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.NewsModel;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/20.
 */
public class NewsViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "NewsViewModel";
    private Handler mDelivery;

    private NewsModel newsModel;

    private MediatorLiveData<Integer> mObservableAddResult;
    private MediatorLiveData<List<NewsInfo>> mObservableNewsInfos;
    private MediatorLiveData<Integer> mObservableNewsNum;

    public NewsViewModel(@NonNull Application application) {
        super(application);

        mDelivery = new Handler(Looper.getMainLooper());

        newsModel = new NewsModel(this);

        mObservableAddResult = new MediatorLiveData<>();
        mObservableAddResult.setValue(null);

        mObservableNewsInfos = new MediatorLiveData<>();
        mObservableNewsInfos.setValue(null);

        mObservableNewsNum = new MediatorLiveData<>();
        mObservableNewsNum.setValue(null);
    }

    public void reqNewsById(int id) {
        addDisposable(newsModel.getNewsById(id));
    }

    public Disposable reqQueryNews(ReqNewsInfos reqQuery) {
        Disposable disposable = newsModel.queryNews(reqQuery);
        addDisposable(disposable);
        return disposable;
    }

    public Disposable reqNewsNum() {
        Disposable disposable = newsModel.newsNum();
        addDisposable(disposable);
        return disposable;
    }

    public void reqAddOrUpdateNews(ReqAddNews reqAddNews) {
        addDisposable(newsModel.addOrUpdateNews(reqAddNews));
    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));

        if (object instanceof TypeResponse) {
            TypeResponse typeResponse = (TypeResponse) object;
            mDelivery.post(() -> {
                switch (typeResponse.type) {
                    case NewsModel.TYPE_ADD:
                        mObservableAddResult.setValue((Integer) typeResponse.data);
                        break;
                    case NewsModel.TYPE_QUERY_NEWS:
                        mObservableNewsInfos.setValue((List<NewsInfo>) typeResponse.data);
                        break;
                    case NewsModel.TYPE_NUM:
                        mObservableNewsNum.setValue((Integer) typeResponse.data);
                        break;
                }
            });
        }
    }

    @Override
    public void fail(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof FailResponse) {
            FailResponse failResponse = (FailResponse) object;
            mDelivery.post(() -> {
                switch (failResponse.type) {
                    case NewsModel.TYPE_NUM:
                        mObservableNewsNum.setValue(null);
                        break;
                    case NewsModel.TYPE_QUERY_NEWS:
                        mObservableNewsInfos.setValue(null);
                        break;
                }
            });
        }
    }

    /**
     * 获取增加和修改新闻请求结果
     *
     * @return
     */
    public MediatorLiveData<Integer> getmObservableAddResult() {
        return mObservableAddResult;
    }

    /**
     * 分页请求新闻列表
     *
     * @return
     */
    public MediatorLiveData<List<NewsInfo>> getmObservableNewsInfos() {
        return mObservableNewsInfos;
    }

    public MediatorLiveData<Integer> getmObservableNewsNum() {
        return mObservableNewsNum;
    }
}
