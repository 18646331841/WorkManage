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
import com.barisetech.www.workmanage.bean.workplan.PlanBean;
import com.barisetech.www.workmanage.bean.workplan.ReqAddPlan;
import com.barisetech.www.workmanage.bean.workplan.ReqAllPlan;
import com.barisetech.www.workmanage.bean.workplan.ReqDeletePlan;
import com.barisetech.www.workmanage.bean.workplan.ReqPlanNum;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.PipeModel;
import com.barisetech.www.workmanage.model.PlanModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class PlanViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "PlanViewModel";

    private Handler mDelivery;

    private PlanModel planModel;
    private MutableLiveData<List<PlanBean>> mObservableAll;
    private MutableLiveData<Integer> mObservableNum;
    private MutableLiveData<String> mObservableAdd;

    public PlanViewModel(@NonNull Application application) {
        super(application);
        mDelivery = new Handler(Looper.getMainLooper());

        planModel = new PlanModel(this);

        mObservableAll = new MutableLiveData<>();
        mObservableAll.setValue(null);

        mObservableNum = new MutableLiveData<>();
        mObservableNum.setValue(null);
        mObservableAdd = new MutableLiveData<>();
        mObservableAdd.setValue(null);
    }

    /**
     * 获取计划数量
     *
     * @return
     */
    public Disposable reqNum(ReqPlanNum reqPlanNum) {
        Disposable disposable = planModel.reqPlanNum(reqPlanNum);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 添加计划
     *
     * @param reqAddPlan
     * @return
     */
    public Disposable reqAdd(ReqAddPlan reqAddPlan) {
        Disposable disposable = planModel.reqAddPlan(reqAddPlan);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 删除计划
     *
     * @param reqDeletePlan
     * @return
     */
    public Disposable reqDelete(ReqDeletePlan reqDeletePlan) {
        Disposable disposable = planModel.reqDeletePlan(reqDeletePlan);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 获取所有计划
     *
     * @param reqAllPlan
     * @return
     */
    public Disposable reqAll(ReqAllPlan reqAllPlan) {
        Disposable disposable = planModel.reqAllPlan(reqAllPlan);
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
                        mObservableAll.setValue((List<PlanBean>) typeResponse.data);
                        break;
                    case PipeModel.TYPE_NUM:
                        mObservableNum.setValue((Integer) typeResponse.data);
                        break;
                    case PipeModel.TYPE_ADD:
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
                    case PipeModel.TYPE_ALL:
                        mObservableAll.setValue(null);
                        break;
                    case PipeModel.TYPE_NUM:
                        mObservableNum.setValue(0);
                        break;
                    case PipeModel.TYPE_ADD:
                        mObservableAdd.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<List<PlanBean>> getObservableAll() {
        return mObservableAll;
    }

    public MutableLiveData<Integer> getObservableNum() {
        return mObservableNum;
    }

    public MutableLiveData<String> getObservableAdd() {
        return mObservableAdd;
    }
}
