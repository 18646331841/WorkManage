package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.databinding.FragmentAlarmListBinding;
import com.barisetech.www.workmanage.viewmodel.AlarmViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmListFragment extends BaseFragment {
    public static final String TAG = "AlarmListFragment";

    private AlarmViewModel alarmViewModel;
    private FragmentAlarmListBinding mBinding;

    public AlarmListFragment() {
        // Required empty public constructor
    }

    public static AlarmListFragment newInstance() {
        AlarmListFragment fragment = new AlarmListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_list, container, false);

        setToolBarHeight(mBinding.toolbar);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        alarmViewModel.getAlarmNum();
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
