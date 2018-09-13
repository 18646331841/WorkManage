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
import com.barisetech.www.workmanage.bean.map.pipe.PipeTrackInfo;
import com.barisetech.www.workmanage.bean.map.pipe.ReqPipeTrack;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.MapModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/9/12.
 */
public class MapViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "MapViewModel";

    private MapModel mapModel;
    private Handler mDelivery;
    private MutableLiveData<Map<String, List<PipeTrackInfo>>> mObservableTrack;

    public MapViewModel(@NonNull Application application) {
        super(application);

        mapModel = new MapModel(this);
        mDelivery = new android.os.Handler(Looper.getMainLooper());

        mObservableTrack = new MutableLiveData<>();
        mObservableTrack.setValue(null);
    }

    public Disposable reqPipeTrack(ReqPipeTrack reqPipeTrack) {
        if (null != reqPipeTrack) {
            Disposable disposable = mapModel.pipeTrack(reqPipeTrack);
            addDisposable(disposable);
            return disposable;
        }
        return null;
    }

    public Disposable reqAllPipeTrack(List<PipeInfo> pipeInfos) {
        if (pipeInfos != null && pipeInfos.size() > 0) {
            Disposable disposable = mapModel.allPipeTrack(pipeInfos);
            addDisposable(disposable);
            return disposable;
        }
        return null;
    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof TypeResponse) {
            TypeResponse typeResponse = (TypeResponse) object;
            mDelivery.post(() -> {
                switch (typeResponse.type) {
                    case MapModel.TYPE_TRACK:
                        mObservableTrack.setValue((Map<String, List<PipeTrackInfo>>) typeResponse.data);
                        break;
                }
            });
        }
    }

    @Override
    public void fail(Object errorObject) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (errorObject instanceof FailResponse) {
            FailResponse failResponse = (FailResponse) errorObject;
            if (failResponse.code == Config.ERROR_UNAUTHORIZED) {
                EventBus.getDefault().post(new EventBusMessage(LoginActivity.TAG));
            }

            mDelivery.post(() -> {
                switch (failResponse.type) {
                    case MapModel.TYPE_TRACK:
                        mObservableTrack.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<Map<String, List<PipeTrackInfo>>> getmObservableTrack() {
        return mObservableTrack;
    }
}
