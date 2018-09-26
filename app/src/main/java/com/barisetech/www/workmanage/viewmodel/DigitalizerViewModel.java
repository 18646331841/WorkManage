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
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAddPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqDeletePipe;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.DigitalizerModel;
import com.barisetech.www.workmanage.model.PipeModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class DigitalizerViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "DigitalizerViewModel";

    private Handler mDelivery;

    private DigitalizerModel digitalizerModel;
    private MutableLiveData<List<DigitalizerBean>> mObservableAll;
    private MutableLiveData<Integer> mObservableNum;
    private MutableLiveData<DigitalizerBean> mObservableModify;

    public DigitalizerViewModel(@NonNull Application application) {
        super(application);
        mDelivery = new Handler(Looper.getMainLooper());

        digitalizerModel = new DigitalizerModel(this);

        mObservableAll = new MutableLiveData<>();
        mObservableAll.setValue(null);

        mObservableNum = new MutableLiveData<>();
        mObservableNum.setValue(null);
        mObservableModify = new MutableLiveData<>();
        mObservableModify.setValue(null);
    }

    /**
     * 获取数字化仪数量
     *
     * @return
     */
    public Disposable reqNum() {
        Disposable disposable = digitalizerModel.reqNum();
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 修改数字化仪
     *
     * @param reqModifyDigitalizer
     * @return
     */
    public Disposable reqModify(ReqModifyDigitalizer reqModifyDigitalizer) {
        Disposable disposable = digitalizerModel.reqModify(reqModifyDigitalizer);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 获取所有数字化仪
     *
     * @param reqAllDigitalizer
     * @return
     */
    public Disposable reqAll(ReqAllDigitalizer reqAllDigitalizer) {
        Disposable disposable = digitalizerModel.reqAll(reqAllDigitalizer);
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
                    case PipeModel.TYPE_ALL:
                        mObservableAll.setValue((List<DigitalizerBean>) typeResponse.data);
                        break;
                    case PipeModel.TYPE_NUM:
                        mObservableNum.setValue((Integer) typeResponse.data);
                        break;
                    case PipeModel.TYPE_ADD:
                        mObservableModify.setValue((DigitalizerBean) typeResponse.data);
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
                    case PipeModel.TYPE_ALL:
                        mObservableAll.setValue(null);
                        break;
                    case PipeModel.TYPE_NUM:
                        mObservableNum.setValue(0);
                        break;
                    case PipeModel.TYPE_ADD:
                        mObservableModify.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<Integer> getmObservableNum() {
        return mObservableNum;
    }

    public MutableLiveData<List<DigitalizerBean>> getmObservableAll() {
        return mObservableAll;
    }

    public MutableLiveData<DigitalizerBean> getmObservableModify() {
        return mObservableModify;
    }
}
