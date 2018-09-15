package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.plugin.PluginInfo;
import com.barisetech.www.workmanage.bean.plugin.ReqAllPlugin;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.PluginModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class PluginViewModel extends BaseViewModel implements ModelCallBack {

    private static final String TAG = "PluginViewModel";

    private Handler mDelivery;

    private PluginModel pluginModel;
    private MutableLiveData<List<PluginInfo>> mObservableAllPlugin;


    public PluginViewModel(@NonNull Application application) {
        super(application);
        mDelivery = new Handler(Looper.getMainLooper());

        pluginModel = new PluginModel(this);

        mObservableAllPlugin = new MutableLiveData<>();
        mObservableAllPlugin.setValue(null);
    }

    public Disposable reqAllPipe(ReqAllPlugin reqAllPlugin) {
        Disposable disposable = pluginModel.reqAllPlugin(reqAllPlugin);
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
                    case PluginModel.TYPE_ALL:
                        mObservableAllPlugin.setValue((List<PluginInfo>) typeResponse.data);
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

            mDelivery.post(() -> {
                switch (failResponse.type) {
                    case PluginModel.TYPE_ALL:
                        mObservableAllPlugin.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<List<PluginInfo>> getmObservableAllPlugin() {
        return mObservableAllPlugin;
    }
}
