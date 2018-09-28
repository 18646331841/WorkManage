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
import com.barisetech.www.workmanage.bean.digitalizer.DigitalizerBean;
import com.barisetech.www.workmanage.bean.digitalizer.ReqAllDigitalizer;
import com.barisetech.www.workmanage.bean.digitalizer.ReqModifyDigitalizer;
import com.barisetech.www.workmanage.bean.wave.ReqWave;
import com.barisetech.www.workmanage.bean.wave.WaveBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.DigitalizerModel;
import com.barisetech.www.workmanage.model.PipeModel;
import com.barisetech.www.workmanage.model.WaveModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class WaveViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "WaveViewModel";

    private Handler mDelivery;

    private WaveModel waveModel;
    private MutableLiveData<WaveBean> mObservableWave;
    private MutableLiveData<List<WaveBean>> mObservableWaves;

    public WaveViewModel(@NonNull Application application) {
        super(application);
        mDelivery = new Handler(Looper.getMainLooper());

        waveModel = new WaveModel(this);

        mObservableWave = new MutableLiveData<>();
        mObservableWave.setValue(null);
        mObservableWaves = new MutableLiveData<>();
        mObservableWaves.setValue(null);
    }

    /**
     * 获取单个波形
     *
     * @param reqWave
     * @return
     */
    public Disposable reqWave(ReqWave reqWave) {
        Disposable disposable = waveModel.reqAll(reqWave);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 获取多个波形
     *
     * @param reqWaves
     * @return
     */
    public Disposable reqAll(List<ReqWave> reqWaves) {
        Disposable disposable = waveModel.reqAll(reqWaves);
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
                    case WaveModel.TYPE_ALL:
                        mObservableWaves.setValue((List<WaveBean>) typeResponse.data);
                        break;
                    case WaveModel.TYPE_SINGLE:
                        mObservableWave.setValue((WaveBean) typeResponse.data);
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
                    case WaveModel.TYPE_ALL:
                        mObservableWaves.setValue(null);
                        break;
                    case WaveModel.TYPE_SINGLE:
                        mObservableWave.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<WaveBean> getmObservableWave() {
        return mObservableWave;
    }

    public MutableLiveData<List<WaveBean>> getmObservableWaves() {
        return mObservableWaves;
    }
}
