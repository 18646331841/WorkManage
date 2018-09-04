package com.barisetech.www.workmanage.view.fragment;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAllAlarmAnalysis;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.databinding.FragmentAlarmAnalysisListBinding;
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
        reqAllAlarmAnalysis.setStartTime(startTime);
        reqAllAlarmAnalysis.setEndTime(endTime);
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
