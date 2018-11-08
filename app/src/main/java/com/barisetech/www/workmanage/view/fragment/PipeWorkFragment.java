package com.barisetech.www.workmanage.view.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.PipeWorkAdapter;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipework.PipeWork;
import com.barisetech.www.workmanage.bean.pipework.ReqAllPW;
import com.barisetech.www.workmanage.bean.pipework.ReqDeletePW;
import com.barisetech.www.workmanage.databinding.FragmentPipeWorkBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeWorkViewModel;
import com.barisetech.www.workmanage.widget.QPopuWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class PipeWorkFragment extends BaseFragment {
    public static final String TAG = "PipeWorkFragment";

    private PipeWorkAdapter pipeWorkAdapter;
    FragmentPipeWorkBinding mBinding;
    private List<PipeWork> pipeWorkList;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private PipeWorkViewModel pipeWorkViewModel;
    private Disposable curDisposable;

    private DatePickerDialog startDatePicker;
    private DatePickerDialog endDatePicker;
    private TimePickerDialog startTimePicker;
    private TimePickerDialog endTimePicker;

    private String startTime = "";
    private String endTime = "";

    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private int curType = BaseConstant.TYPE_INCIDENT_ALL;
    private int selectType;//选择的类型
    private Point mPoint = new Point();

    public PipeWorkFragment() {
        // Required empty public constructor
    }

    public static PipeWorkFragment newInstance() {
        PipeWorkFragment fragment = new PipeWorkFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pipeWorkList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_work, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe_work));
        toolbarInfo.setOneText("新增");
        observableToolbar.set(toolbarInfo);

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        initRecyclerView();

        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(PipeWorkAddFragment.TAG);
            EventBus.getDefault().post(eventBusMessage);
        });
    }

    private void initRecyclerView() {

        pipeWorkAdapter = new PipeWorkAdapter(pipeWorkList, getContext(), itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(pipeWorkAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.pipeWorkList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.pipeWorkList.setAdapter(loadMoreWrapper);
        mBinding.pipeWorkList.setItemAnimator(new DefaultItemAnimator());

        mBinding.pipeWorkList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (pipeWorkList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - pipeWorkList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(pipeWorkList.size(), count);
                    } else {
                        updateRecyclerView(pipeWorkList.size(), PAGE_COUNT);
                    }
                }
            }
        });

        pipeWorkAdapter.setOnItemLongClickListener((view, position) -> {
            PipeWork pipeWork = pipeWorkList.get(position);
            QPopuWindow.getInstance(getActivity()).builder
                    .bindView(view,0)
                    .setPopupItemList(new String[]{"编辑管线工况", "删除管线工况"})
                    .setPointers(mPoint.x,mPoint.y)
                    .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                        @Override
                        public void onPopuListItemClick(View anchorView, int anchorViewPosition, int p) {
                            switch (p){
                                case 0:
                                    EventBusMessage eventBusMessage = new EventBusMessage(PipeWorkModifyFragment.TAG);
                                    eventBusMessage.setArg1(pipeWork);
                                    EventBus.getDefault().post(eventBusMessage);
                                    break;
                                case 1:
                                    ReqDeletePW reqDeletePW = new ReqDeletePW();
                                    reqDeletePW.setPipeWorkId(String.valueOf(pipeWork.Id));

                                    EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
                                    pipeWorkViewModel.reqDeletePw(reqDeletePW);
                                    break;
                            }
                        }
                    }).show();
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void PointEventBus(Point point) {
        mPoint = point;
    }

    private ItemCallBack itemCallBack = item -> {
        EventBusMessage eventBusMessage = new EventBusMessage(PipeWorkDetailFragment.TAG);
        eventBusMessage.setArg1(item);
        EventBus.getDefault().post(eventBusMessage);
    };

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);
    }

    private void getDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqAllPW reqAllPW = new ReqAllPW();
        reqAllPW.setIsGetAll("false");
        reqAllPW.setPipeIdQueryChecked("false");
        reqAllPW.setPipeId("0");
        reqAllPW.setTimeQueryChecked("true");
        reqAllPW.setType("0");
        reqAllPW.setMStartTime("1970-1-1 00:00:00");
        reqAllPW.setMEndTime(TimeUtil.ms2Date(System.currentTimeMillis()));
        reqAllPW.setStartIndex(String.valueOf(formIndex));
        reqAllPW.setNumberOfRecords(String.valueOf(toIndex));

        curDisposable = pipeWorkViewModel.reqAllPw(reqAllPW);
    }

    private void getNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        pipeWorkList.clear();
        maxNum = 0;
        pipeWorkViewModel.reqPwNum();
    }

    @Override
    public void bindViewModel() {
        pipeWorkViewModel = ViewModelProviders.of(this).get(PipeWorkViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeWorkViewModel.getmObservableAllPW().hasObservers()) {
            pipeWorkViewModel.getmObservableAllPW().observe(this, pipeWorks -> {
                if (PipeWorkFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

                    if (null != pipeWorks) {
                        if (pipeWorks.size() > 0) {
                            pipeWorkList.addAll(pipeWorks);
                            LogUtil.d(TAG, "load complete = " + pipeWorks);
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

        if (!pipeWorkViewModel.getmObservableNum().hasObservers()) {
            pipeWorkViewModel.getmObservableNum().observe(this, integer -> {
                if (PipeWorkFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer) {
                        maxNum = integer;
                        if (maxNum >= PAGE_COUNT) {
                            getDatas(0, PAGE_COUNT);
                        } else {
                            getDatas(0, maxNum);
                        }
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }

//        if (null == pipeWorkList || pipeWorkList.size() <= 0) {
            getNums();
//        }

        if (!pipeWorkViewModel.getmObservableDel().hasObservers()) {
            pipeWorkViewModel.getmObservableDel().observe(this, aBoolean -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != aBoolean) {
                        if (aBoolean) {
                            ToastUtil.showToast("删除成功");
                            getActivity().onBackPressed();
                        } else {
                            ToastUtil.showToast("删除失败");
                        }
                    } else {
                        ToastUtil.showToast("删除失败");
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
