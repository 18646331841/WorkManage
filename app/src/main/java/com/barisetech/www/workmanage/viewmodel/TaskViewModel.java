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
import com.barisetech.www.workmanage.bean.worktask.ReqAddTask;
import com.barisetech.www.workmanage.bean.worktask.ReqAllTask;
import com.barisetech.www.workmanage.bean.worktask.ReqDeleteTask;
import com.barisetech.www.workmanage.bean.worktask.ReqTaskNum;
import com.barisetech.www.workmanage.bean.worktask.TaskBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.PipeModel;
import com.barisetech.www.workmanage.model.TaskModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class TaskViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "TaskViewModel";

    private Handler mDelivery;

    private TaskModel taskModel;
    private MutableLiveData<List<TaskBean>> mObservableAllTask;
    private MutableLiveData<Integer> mObservableTaskNum;
    private MutableLiveData<String> mObservableAdd;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        mDelivery = new Handler(Looper.getMainLooper());

        taskModel = new TaskModel(this);

        mObservableAllTask = new MutableLiveData<>();
        mObservableAllTask.setValue(null);

        mObservableTaskNum = new MutableLiveData<>();
        mObservableTaskNum.setValue(null);
        mObservableAdd = new MutableLiveData<>();
        mObservableAdd.setValue(null);
    }

    /**
     * 获取任务数量
     *
     * @return
     */
    public Disposable reqNum(ReqTaskNum reqTaskNum) {
        Disposable disposable = taskModel.reqTaskNum(reqTaskNum);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 添加任务
     *
     * @param reqAddTask
     * @return
     */
    public Disposable reqAdd(ReqAddTask reqAddTask) {
        Disposable disposable = taskModel.reqAddTask(reqAddTask);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 删除任务
     *
     * @param reqDeleteTask
     * @return
     */
    public Disposable reqDelete(ReqDeleteTask reqDeleteTask) {
        Disposable disposable = taskModel.reqDeleteTask(reqDeleteTask);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 获取所有管线
     *
     * @param reqAllTask
     * @return
     */
    public Disposable reqAll(ReqAllTask reqAllTask) {
        Disposable disposable = taskModel.reqAllTask(reqAllTask);
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
                        mObservableAllTask.setValue((List<TaskBean>) typeResponse.data);
                        break;
                    case PipeModel.TYPE_NUM:
                        mObservableTaskNum.setValue((Integer) typeResponse.data);
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
                        mObservableAllTask.setValue(null);
                        break;
                    case PipeModel.TYPE_NUM:
                        mObservableTaskNum.setValue(0);
                        break;
                    case PipeModel.TYPE_ADD:
                        mObservableAdd.setValue(null);
                        break;
                }
            });
        }
    }

    public MutableLiveData<List<TaskBean>> getObservableAllTask() {
        return mObservableAllTask;
    }

    public MutableLiveData<Integer> getObservableTaskNum() {
        return mObservableTaskNum;
    }

    public MutableLiveData<String> getObservableAdd() {
        return mObservableAdd;
    }
}
