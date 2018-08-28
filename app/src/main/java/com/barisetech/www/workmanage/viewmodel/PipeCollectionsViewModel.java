package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAddPC;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.bean.pipecollections.ReqDeletePc;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.PipeCollectionModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class PipeCollectionsViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "PipeCollectionsViewModel";

    private PipeCollectionModel pipeCollectionModel;

    public PipeCollectionsViewModel(@NonNull Application application) {
        super(application);
        pipeCollectionModel = new PipeCollectionModel(this);
    }

    /**
     * 获取管线集合数量
     * @return
     */
    public Disposable reqPcNum() {
        Disposable disposable = pipeCollectionModel.reqPcNum();
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 添加或修改管线集合
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
    }

    @Override
    public void fail(int errorCode) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (errorCode == Config.ERROR_UNAUTHORIZED) {
            EventBus.getDefault().post(new EventBusMessage(LoginActivity.TAG));
        }
    }
}
