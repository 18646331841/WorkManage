package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MediatorLiveData;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.model.SiteModel;
import com.barisetech.www.workmanage.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/20.
 */
public class SiteViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "SiteViewModel";
    private Handler mDelivery;

    private SiteModel siteModel;

    private MediatorLiveData<Integer> mObservableAddResult;
    private MediatorLiveData<List<SiteBean>> mObservableSiteInfos;

    public SiteViewModel(@NonNull Application application) {
        super(application);

        mDelivery = new Handler(Looper.getMainLooper());

        siteModel = new SiteModel(this);

        mObservableAddResult = new MediatorLiveData<>();
        mObservableAddResult.setValue(null);

        mObservableSiteInfos = new MediatorLiveData<>();
        mObservableSiteInfos.setValue(null);
    }

    public void reqSiteNum() {
        addDisposable(siteModel.siteNum());
    }

    public void reqSiteById(int id) {
        addDisposable(siteModel.getSiteById(id));
    }

    public Disposable reqQuerySite(ReqSiteInfos reqQuery) {
        Disposable disposable = siteModel.querySite(reqQuery);
        addDisposable(disposable);
        return disposable;
    }

//    public void reqAddOrUpdateNews(ReqAddNews reqAddNews) {
//        addDisposable(siteModel.addOrUpdateNews(reqAddNews));
//    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));

        if (object instanceof TypeResponse) {
            TypeResponse typeResponse = (TypeResponse) object;
            mDelivery.post(() -> {
                switch (typeResponse.type) {
                    case SiteModel.TYPE_ADD:
                        mObservableAddResult.setValue((Integer) typeResponse.data);
                        break;
                    case SiteModel.TYPE_QUERY_SITE:
                        mObservableSiteInfos.setValue((List<SiteBean>) typeResponse.data);
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

    /**
     * 分页请求新闻列表
     * @return
     */
    public MediatorLiveData<List<SiteBean>> getmObservableSiteInfos() {
        return mObservableSiteInfos;
    }
}
