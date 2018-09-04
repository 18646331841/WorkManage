package com.barisetech.www.workmanage.view.fragment;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.AnalysisAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAllAlarmAnalysis;
import com.barisetech.www.workmanage.databinding.FragmentAlarmAnalysisListBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.viewmodel.AlarmAnalysisViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class AlarmAnalysisListFragment extends BaseFragment {
    public static final String TAG = "AlarmAnalysisListFragment";

    private List<AlarmAnalysis> alarmAnalysisList;
    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private int curType;
    private String startTime;
    private String endTime;

    private Disposable curDisposable;
    private BaseLoadMoreWrapper loadMoreWrapper;
    FragmentAlarmAnalysisListBinding mBinding;
    private AlarmAnalysisViewModel alarmAnalysisViewModel;
    private AnalysisAdapter analysisAdapter;

    public AlarmAnalysisListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmAnalysisList = new ArrayList<>();
    }

    public static AlarmAnalysisListFragment newInstance() {
        AlarmAnalysisListFragment fragment = new AlarmAnalysisListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_analysis_list, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_alarm_analysis));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.searchLayout.tvFilter.setOnClickListener(view -> {
            if (mBinding.analysisListFilterLayout.getVisibility() == View.GONE) {
                mBinding.analysisListFilterLayout.setVisibility(View.VISIBLE);
            } else {
                mBinding.analysisListFilterLayout.setVisibility(View.GONE);
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {

        analysisAdapter = new AnalysisAdapter(alarmAnalysisList, getContext(), itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(analysisAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.alarmAnalysisList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.alarmAnalysisList.setAdapter(loadMoreWrapper);
        mBinding.alarmAnalysisList.setItemAnimator(new DefaultItemAnimator());

        mBinding.alarmAnalysisList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (alarmAnalysisList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - alarmAnalysisList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(alarmAnalysisList.size(), count);
                    } else {
                        updateRecyclerView(alarmAnalysisList.size(), PAGE_COUNT);
                    }
                }
            }
        });
    }

    private ItemCallBack itemCallBack = item -> {
        LogUtil.d(TAG, "Id = " + ((AlarmAnalysis) item).getId());
    };

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);
    }

    private void getDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqAllAlarmAnalysis reqAllAlarmAnalysis = new ReqAllAlarmAnalysis();
        reqAllAlarmAnalysis.setIsGetAll("false");
        reqAllAlarmAnalysis.setGetByid("false");
        reqAllAlarmAnalysis.setAlarmId("-1");
        reqAllAlarmAnalysis.setAlarmAnalysisId("-1");
        reqAllAlarmAnalysis.setGetByMy("false");
//        reqAllAlarmAnalysis.setStartTime(startTime);
//        reqAllAlarmAnalysis.setEndTime(endTime);
        reqAllAlarmAnalysis.setStartTime("2009-12-12 12:12:12");
        reqAllAlarmAnalysis.setEndTime("2019-12-12 12:12:12");
        reqAllAlarmAnalysis.setStartIndex(String.valueOf(formIndex));
        reqAllAlarmAnalysis.setNumberOfRecords(String.valueOf(toIndex));
        reqAllAlarmAnalysis.setType(String.valueOf(curType));

        curDisposable = alarmAnalysisViewModel.reqAllAnalysis(reqAllAlarmAnalysis);
    }

    private void getListNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        alarmAnalysisViewModel.reqAnalysisNum();
    }

    @Override
    public void bindViewModel() {
        alarmAnalysisViewModel = ViewModelProviders.of(this).get(AlarmAnalysisViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!alarmAnalysisViewModel.getmObservableAll().hasObservers()) {
            alarmAnalysisViewModel.getmObservableAll().observe(this, alarmAnalyses -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != alarmAnalyses) {
                        if (alarmAnalyses.size() > 0) {
                            alarmAnalysisList.addAll(alarmAnalyses);
                            LogUtil.d(TAG, "load complete = " + alarmAnalyses);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
                        if (null != alarmAnalysisList && alarmAnalysisList.size() > 0) {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    }
                }
            });
        }

        if (!alarmAnalysisViewModel.getmObservableNum().hasObservers()) {
            alarmAnalysisViewModel.getmObservableNum().observe(this, integer -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
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
        if (null == alarmAnalysisList || alarmAnalysisList.size() <= 0) {
            getListNums();
        }
    }
}
