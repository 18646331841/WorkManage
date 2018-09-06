package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAddPipe;
import com.barisetech.www.workmanage.bean.site.ReqAddSite;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.AlarmModel;
import com.barisetech.www.workmanage.model.NewsModel;
import com.barisetech.www.workmanage.model.SiteModel;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/20.
 */
public class SiteViewModel extends BaseViewModel implements ModelCallBack {


    private static final String TAG = "SiteViewModel";

    private SiteModel siteModel;

    private Handler mDelivery;

    private MediatorLiveData<Integer> mObservableSiteNum;
    private MediatorLiveData<List<SiteBean>> mObservableSiteInfos;

    public SiteViewModel(@NonNull Application application) {
        super(application);

        mDelivery = new Handler(Looper.getMainLooper());
        siteModel = new SiteModel(this);



        mObservableSiteNum = new MediatorLiveData<>();
        mObservableSiteNum.setValue(null);

        mObservableSiteInfos = new MediatorLiveData<>();
        mObservableSiteInfos.setValue(null);
    }


    public Disposable reqSiteNum(){
        Disposable disposable = siteModel.reqSiteNum();
        addDisposable(disposable);
        return disposable;
    }

    public Disposable reqAllSite(ReqSiteInfos reqSiteInfos){
        Disposable disposable = siteModel.reqAllSite(reqSiteInfos);
        addDisposable(disposable);
        return disposable;
    }

    public Disposable reqAddOrModifySite(ReqAddSite reqAddSite) {
        Disposable disposable = siteModel.reqAddOrModifySite(reqAddSite);
        addDisposable(disposable);
        return disposable;
    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof TypeResponse) {
            TypeResponse typeResponse = (TypeResponse) object;
            mDelivery.post(() -> {
                switch (typeResponse.type) {
                    case SiteModel.TYPE_NUM:
                        mObservableSiteNum.setValue((Integer) typeResponse.data);
                        break;
                    case SiteModel.TYPE_ALL:
                        mObservableSiteInfos.setValue((List<SiteBean>) typeResponse.data);
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
            if (failResponse.code == Config.ERROR_UNAUTHORIZED) {
                EventBus.getDefault().post(new EventBusMessage(LoginActivity.TAG));
            }
        }
    }

    public MediatorLiveData<Integer> getmObservableSiteNum() {
        return mObservableSiteNum;
    }

    public MediatorLiveData<List<SiteBean>> getmObservableSiteInfos() {
        return mObservableSiteInfos;
    }
}
