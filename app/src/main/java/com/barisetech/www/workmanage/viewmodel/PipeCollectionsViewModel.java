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
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAddPC;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.bean.pipecollections.ReqDeletePc;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.PipeCollectionModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class PipeCollectionsViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "PipeCollectionsViewModel";

    private PipeCollectionModel pipeCollectionModel;
    private Handler mDelivery;

    private MutableLiveData<List<PipeCollections>> mObservableAllPC;
    private MutableLiveData<Integer> mObservableNum;
    private MutableLiveData<String> mObservableAdd;

    public PipeCollectionsViewModel(@NonNull Application application) {
        super(application);
        pipeCollectionModel = new PipeCollectionModel(this);
        mDelivery = new android.os.Handler(Looper.getMainLooper());

        mObservableAllPC = new MutableLiveData<>();
        mObservableAllPC.setValue(null);
        mObservableNum = new MutableLiveData<>();
        mObservableNum.setValue(null);
        mObservableAdd = new MutableLiveData<>();
        mObservableAdd.setValue(null);
    }

    /**
     * 获取管线集合数量
     *
     * @return
     */
    public Disposable reqPcNum() {
        Disposable disposable = pipeCollectionModel.reqPcNum();
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 添加或修改管线集合
     *
     * @param reqAddPC
     * @return
     */
    public Disposable reqAddOrModifyPc(ReqAddPC reqAddPC) {
        Disposable disposable = pipeCollectionModel.reqAddOrModifyPc(reqAddPC);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 删除管线集合
     *
     * @param reqDeletePc
     * @return
     */
    public Disposable reqDeletePc(ReqDeletePc reqDeletePc) {
        Disposable disposable = pipeCollectionModel.reqDeletePc(reqDeletePc);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 获取所有管线集合
     *
     * @param reqAllPc
     * @return
     */
    public Disposable reqAllPc(ReqAllPc reqAllPc) {
        Disposable disposable = pipeCollectionModel.reqAllPc(reqAllPc);
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
                    case PipeCollectionModel.TYPE_ALL:
                        mObservableAllPC.setValue((List<PipeCollections>) typeResponse.data);
                        break;
                    case PipeCollectionModel.TYPE_NUM:
                        mObservableNum.setValue((Integer) typeResponse.data);
                        break;
                    case PipeCollectionModel.TYPE_ADD:
                        mObservableAdd.setValue((String) typeResponse.data);
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
                    case PipeCollectionModel.TYPE_NUM:
                        mObservableNum.setValue(null);
                        break;
                    case PipeCollectionModel.TYPE_ALL:
                        mObservableAllPC.setValue(null);
                        break;
                    case PipeCollectionModel.TYPE_ADD:
                        mObservableAdd.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<List<PipeCollections>> getmObservableAllPC() {
        return mObservableAllPC;
    }

    public MutableLiveData<Integer> getmObservableNum() {
        return mObservableNum;
    }

    public MutableLiveData<String> getmObservableAdd() {
        return mObservableAdd;
    }
}
