package com.barisetech.www.workmanage.view.fragment.worktask;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.PlanContactsListAdapter;
import com.barisetech.www.workmanage.adapter.PlanTaskListAdapter;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.contacts.ReqAllContacts;
import com.barisetech.www.workmanage.bean.contacts.ReqContactsNum;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.bean.workplan.PlanBean;
import com.barisetech.www.workmanage.bean.workplan.ReqAddPlan;
import com.barisetech.www.workmanage.bean.worktask.ReqAddTask;
import com.barisetech.www.workmanage.bean.worktask.ReqAllTask;
import com.barisetech.www.workmanage.bean.worktask.TaskBean;
import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;
import com.barisetech.www.workmanage.databinding.FragmentPlanContactsListBinding;
import com.barisetech.www.workmanage.databinding.FragmentPlanTaskListBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.fragment.SignInFragment;
import com.barisetech.www.workmanage.view.fragment.workplan.SecondPublishFragment;
import com.barisetech.www.workmanage.viewmodel.ContactsViewModel;
import com.barisetech.www.workmanage.viewmodel.TaskViewModel;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class TaskListFragment extends BaseFragment {
    public static final String TAG = "TaskListFragment";

    FragmentPlanTaskListBinding mBinding;
    private Disposable curDisposable;
    private Disposable numDisposable;
    private Disposable addDisposable;
    private TaskBean curTaskBean;
    private List<TaskSiteBean> curSiteList;
    private List<TaskBean> curTaskList;
    private PlanTaskListAdapter planTaskListAdapter;
    private TaskViewModel taskViewModel;

    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum = 100;
    private BaseLoadMoreWrapper loadMoreWrapper;

    private static final String PLAN_ID = "plan";
    private PlanBean curPlanBean;
    private int siteNum = 0;

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance(PlanBean plan) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PLAN_ID, plan);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curSiteList = new ArrayList<>();
        curTaskList = new ArrayList<>();
        if (getArguments() != null) {
            curPlanBean = (PlanBean) getArguments().getSerializable(PLAN_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_task_list, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_plan_task));
        toolbarInfo.setOneText(getString(R.string.plan_task_add));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        String account = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
        if (curPlanBean.PersonLiable.equals(account)) {
            mBinding.toolbar.tvOne.setVisibility(View.VISIBLE);
        } else {
            mBinding.toolbar.tvOne.setVisibility(View.GONE);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("巡线次数：").append(curPlanBean.TotalNumberOfTimes)
                .append(" 完成时间：").append(TimeUtil.replaceTimeT(curPlanBean.EndTime))
                .append(" 责任人：").append(curPlanBean.PersonLiable);

        mBinding.planTaskListTitle.setText(sb.toString());

        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            addTask();
        });

        initRecyclerView();
    }

    private void closeDisposable() {
        if (curDisposable != null) {
            curDisposable.dispose();
        }
        if (numDisposable != null) {
            numDisposable.dispose();
        }
    }

    private void initRecyclerView() {

        planTaskListAdapter = new PlanTaskListAdapter(curSiteList, curPlanBean
                .PersonLiable, getContext(), itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(planTaskListAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.planTaskList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.planTaskList.setAdapter(loadMoreWrapper);
        mBinding.planTaskList.setItemAnimator(new DefaultItemAnimator());

        mBinding.planTaskList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (curSiteList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - curSiteList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(curSiteList.size(), count);
                    } else {
                        updateRecyclerView(curSiteList.size(), PAGE_COUNT);
                    }
                }
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);
    }

    private void getDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqAllTask reqAllTask = new ReqAllTask();
        reqAllTask.isGetAll = "false";
        reqAllTask.TimeQueryChecked = "false";
        reqAllTask.mStartTime = "1970-01-01 00:00:00";
        reqAllTask.mEndTime = TimeUtil.ms2Date(System.currentTimeMillis());
        reqAllTask.startIndex = String.valueOf(0);
        reqAllTask.numberOfRecords = String.valueOf(toIndex);
        reqAllTask.PesonChecked = "false";
        reqAllTask.PersonLiable = curPlanBean.PersonLiable;
        reqAllTask.State = "2";
        reqAllTask.Transferor = "";
        reqAllTask.workTaskID = "-1";
        reqAllTask.workPlanID = String.valueOf(curPlanBean.Id);

//        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
        curDisposable = taskViewModel.reqAll(reqAllTask);
    }

    private void getListNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
    }

    /**
     * 增加任务
     */
    private void addTask() {
        if (addDisposable != null) {
            addDisposable.dispose();
        }
        ReqAddTask reqAddTask = new ReqAddTask();
        reqAddTask.Id = "0";
        reqAddTask.Name = "第" + (curTaskList.size() + 1) + "次巡线";
        reqAddTask.WorkPlanId = String.valueOf(curPlanBean.Id);
        reqAddTask.PersonLiable = curPlanBean.PersonLiable;
        reqAddTask.Transferor = "";
        reqAddTask.State = "2";
        reqAddTask.NumberOfTransfers = "1";
        reqAddTask.Deadline = TimeUtil.ms2YMD(System.currentTimeMillis()) + " 23:59:59";
        reqAddTask.isAdd = "true";
        reqAddTask.toSiteList(curPlanBean.PlanSiteList);

        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
        addDisposable = taskViewModel.reqAdd(reqAddTask);
    }

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof TaskSiteBean) {
            TaskSiteBean taskSiteBean = (TaskSiteBean) item;
            taskSiteBean.range = curPlanBean.Range;

            List<TaskSiteBean> curSites = new ArrayList<>();
            for (TaskBean taskBean : curTaskList) {
                if (taskBean.Id == taskSiteBean.taskId) {
                    curSites = taskBean.TaskSiteList;
                }
            }
            //判断是否该完成每次的最后一个打卡
            boolean isEnd = false;
            int numUnComplete = 0;
            for (TaskSiteBean site : curSites) {
                if (site.State != BaseConstant.STATUS_COMPLETED) {
                    numUnComplete++;
                }
            }
            if (numUnComplete == 1) {
                isEnd = true;
            }

            taskSiteBean.isEnd = isEnd;
            EventBusMessage eventBusMessage = new EventBusMessage(SignInFragment.TAG);
            eventBusMessage.setArg1(taskSiteBean);
            EventBus.getDefault().post(eventBusMessage);
        }
    };

    @Override
    public void bindViewModel() {
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!taskViewModel.getObservableAdd().hasObservers()) {
            taskViewModel.getObservableAdd().observe(this, s -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (s != null) {
                        if (s.equals("成功添加")) {
                            getDatas(0, maxNum);
                        } else {
                            ToastUtil.showToast(s);
                        }
                    } else {
                        ToastUtil.showToast("失败添加");
                    }
                }
            });
        }

        if (!taskViewModel.getObservableAllTask().hasObservers()) {
            taskViewModel.getObservableAllTask().observe(this, taskBeans -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != taskBeans) {
                        if (taskBeans.size() > 0) {
                            curTaskList.clear();
                            curSiteList.clear();
                            curTaskList.addAll(taskBeans);
                            for (TaskBean taskBean : curTaskList) {
                                List<TaskSiteBean> taskSiteBeans = taskBean.TaskSiteList;
                                if (taskSiteBeans != null && taskSiteBeans.size() > 0) {
                                    TaskSiteBean titleSiteBean = new TaskSiteBean();//占位标题类
                                    titleSiteBean.SiteId = -1;
                                    titleSiteBean.Name = taskBean.Name;
                                    curSiteList.add(titleSiteBean);
                                    for (TaskSiteBean sites : taskSiteBeans) {
                                        sites.taskId = taskBean.Id;
                                        if (taskBean.Deadline.contains("T")) {
                                            taskBean.Deadline = taskBean.Deadline.replace("T", " ");
                                        }
                                        sites.deadline = taskBean.Deadline;
                                        //处理服务器返回0001-00-01格式的日期
                                        if (sites.DateTime.contains("0001")) {
                                            sites.DateTime = "--";
                                        }
                                    }
                                    curSiteList.addAll(taskSiteBeans);
                                }
                            }

                            LogUtil.d(TAG, "load complete = " + taskBeans);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }

//        if (!taskViewModel.getObservableTaskNum().hasObservers()) {
//            taskViewModel.getObservableTaskNum().observe(this, integer -> {
//                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
//                    if (null != integer) {
//                        maxNum = integer;
//                        if (maxNum >= PAGE_COUNT) {
//                            getDatas(0, PAGE_COUNT);
//                        } else {
//                            getDatas(0, maxNum);
//                        }
//                    } else {
//                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
//                }
//            });
//        }

//        if (null == curSiteList || curSiteList.size() <= 0) {
//            getListNums();
            getDatas(0, maxNum);
//        }
    }
}
