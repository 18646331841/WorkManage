package com.barisetech.www.workmanage.view.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAddAlarmAnalysis;
import com.barisetech.www.workmanage.databinding.FragmentAlarmAnalysisDetailBinding;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.viewmodel.AlarmAnalysisViewModel;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmAnalysisDetailFragment extends BaseFragment {
    public static final String TAG = "AlarmAnalysisDetailFragment";

    private static final String ARG_ANALYSIS = "analysis";
    private AlarmAnalysis curAnalysis;
    FragmentAlarmAnalysisDetailBinding mBinding;
    private AlarmAnalysisViewModel alarmAnalysisViewModel;

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
        alarmAnalysisViewModel = ViewModelProviders.of(this).get(AlarmAnalysisViewModel.class);
    }

    @Override
    public void subscribeToModel() {

        String account = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
        if (!TextUtils.isEmpty(account)) {
            if (!curAnalysis.getReadPeopleList().contains(account)) {
                LogUtil.d(TAG, "first read");
                ReqAddAlarmAnalysis reqAddAlarmAnalysis = new ReqAddAlarmAnalysis();
                reqAddAlarmAnalysis.setId(String.valueOf(curAnalysis.getId()));
                reqAddAlarmAnalysis.setIsNewReader("true");
                reqAddAlarmAnalysis.setReadLV(String.valueOf(curAnalysis.getReadLV()));
                reqAddAlarmAnalysis.setTittle(curAnalysis.getTittle());
                reqAddAlarmAnalysis.setReleaseTime(curAnalysis.getReleaseTime());
                reqAddAlarmAnalysis.setAlarmID(String.valueOf(curAnalysis.getAlarmID()));
                reqAddAlarmAnalysis.setAlarmCause(String.valueOf(curAnalysis.getAlarmCause()));
                reqAddAlarmAnalysis.setAlarmDetail(curAnalysis.getAlarmDetail());
                reqAddAlarmAnalysis.setAnalyst(curAnalysis.getAnalyst());
                reqAddAlarmAnalysis.setReadQuantity("0");
                reqAddAlarmAnalysis.setIsAdd("false");
                reqAddAlarmAnalysis.setReadPeopleList(new LinkedList<>());
                reqAddAlarmAnalysis.setImageList(new ArrayList<>());

                alarmAnalysisViewModel.reqAddOrModifyAnalysis(reqAddAlarmAnalysis);
            }
        }
    }
}
