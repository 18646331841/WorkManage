package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.news.ReqAddNews;
import com.barisetech.www.workmanage.bean.news.ReqNewsInfos;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.model.NewsModel;
import com.barisetech.www.workmanage.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by LJH on 2018/8/20.
 */
public class NewsViewModel extends AndroidViewModel implements ModelCallBack {
    private static final String TAG = "NewsViewModel";
    private Handler mDelivery;

    private NewsModel newsModel;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    private MediatorLiveData<Integer> mObservableAddResult;
    private MediatorLiveData<List<NewsInfo>> mObservableNewsInfos;

    public NewsViewModel(@NonNull Application application) {
        super(application);

        mDelivery = new Handler(Looper.getMainLooper());

        newsModel = new NewsModel(this);

        mObservableAddResult = new MediatorLiveData<>();
        mObservableAddResult.setValue(null);

        mObservableNewsInfos = new MediatorLiveData<>();
        mObservableNewsInfos.setValue(null);
    }

    public void reqNewsNum() {
        mDisposable.add(newsModel.newsNum());
    }

    public void reqNewsById(int id) {
        mDisposable.add(newsModel.getNewsById(id));
    }

    public void reqQueryNews(ReqNewsInfos reqQuery) {
        mDisposable.add(newsModel.queryNews(reqQuery));
    }

    public void reqAddOrUpdateNews(ReqAddNews reqAddNews) {
        mDisposable.add(newsModel.addOrUpdateNews(reqAddNews));
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
                }
            });
        }
    }

    @Override
    public void fail(int errorCode) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        ToastUtil.showToast("请求失败");
    }

    /**
     * 获取增加和修改新闻请求结果
     * @return
     */
    public MediatorLiveData<Integer> getmObservableAddResult() {
        return mObservableAddResult;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDelivery = null;
    }

    /**
     * 分页请求新闻列表
     * @return
     */
    public MediatorLiveData<List<NewsInfo>> getmObservableNewsInfos() {
        return mObservableNewsInfos;
    }
}
