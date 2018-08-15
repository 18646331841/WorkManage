package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.AlarmlistAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.databinding.FragmentAlarmListBinding;
import com.barisetech.www.workmanage.viewmodel.AlarmViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmListFragment extends BaseFragment {
    public static final String TAG = "AlarmListFragment";

    private AlarmViewModel alarmViewModel;
    private FragmentAlarmListBinding mBinding;
    private AlarmlistAdapter alarmlistAdapter;

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
        setToolBarHeight(mBinding.toolbar.getRoot());

        alarmlistAdapter = new AlarmlistAdapter(itemCallBack);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.alarmList.setLayoutManager(llm);
        mBinding.alarmList.setAdapter(alarmlistAdapter);

        return mBinding.getRoot();
    }

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof AlarmInfo) {
            AlarmInfo alarmInfo = (AlarmInfo) item;
        }
    };

    @Override
    public void bindViewModel() {
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        alarmViewModel.getmObservableAllAlarmInfos().observe(this, alarmInfos -> {
            if (null != alarmInfos) {
                alarmlistAdapter.setCommentList(alarmInfos);
            }
        });
    }
}
