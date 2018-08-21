package com.barisetech.www.workmanage.view.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.viewmodel.AlarmViewModel;

public class AlarmAnalysisFragment extends BaseFragment {
    public static final String TAG = "AlarmAnalysisFragment";

    private static final String ALARM_ID = "alarmId";

    private AlarmInfo curAlarmInfo;
//    private AlarmViewModel alarmViewModel;
    private FragmentAlarmAnalysisBinding mBinding;
    public ObservableField<AlarmInfo> alarmInfo;


    public AlarmAnalysisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curAlarmInfo = (AlarmInfo) getArguments().getSerializable(ALARM_ID);
        }
    }

    public static AlarmAnalysisFragment newInstance(AlarmInfo alarmInfo) {
        AlarmAnalysisFragment fragment = new AlarmAnalysisFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALARM_ID, alarmInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_analysis, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_alarm_analysis));
        observableToolbar.set(toolbarInfo);
//
//        initView();

        return mBinding.getRoot();
    }

    public void initView() {
        alarmInfo = new ObservableField<>();
        alarmInfo.set(curAlarmInfo);
        mBinding.setMyFragment(this);
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
