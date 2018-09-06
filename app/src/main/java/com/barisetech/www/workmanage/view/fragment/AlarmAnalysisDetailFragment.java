package com.barisetech.www.workmanage.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.databinding.FragmentAlarmAnalysisDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmAnalysisDetailFragment extends BaseFragment {
    public static final String TAG = "AlarmAnalysisDetailFragment";

    private static final String ARG_ANALYSIS = "analysis";
    private AlarmAnalysis curAnalysis;
    FragmentAlarmAnalysisDetailBinding mBinding;

    public AlarmAnalysisDetailFragment() {
        // Required empty public constructor
    }

    public static AlarmAnalysisDetailFragment newInstance(AlarmAnalysis alarmAnalysis) {
        AlarmAnalysisDetailFragment fragment = new AlarmAnalysisDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ANALYSIS, alarmAnalysis);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            curAnalysis = (AlarmAnalysis) getArguments().getSerializable(ARG_ANALYSIS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_analysis_detail, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_analysis_detail));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.setAnalysisinfo(curAnalysis);
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
