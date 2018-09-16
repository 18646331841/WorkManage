package com.barisetech.www.workmanage.view.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.databinding.FragmentAlarmDetailsBinding;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.viewmodel.AlarmViewModel;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmDetailsFragment extends BaseFragment implements View.OnClickListener{
    public static final String TAG = "AlarmDetailsFragment";

    private FragmentAlarmDetailsBinding mBinding;

    private static final String ALARM_ID = "alarmId";

    private AlarmInfo curAlarmInfo;
    private AlarmViewModel alarmViewModel;
    public ObservableField<AlarmInfo> alarmInfo;

    public AlarmDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curAlarmInfo = (AlarmInfo) getArguments().getSerializable(ALARM_ID);
        }
    }

    public static AlarmDetailsFragment newInstance(AlarmInfo alarmInfo) {
        AlarmDetailsFragment fragment = new AlarmDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALARM_ID, alarmInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_details, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_alarm_details));
        observableToolbar.set(toolbarInfo);

        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        alarmInfo = new ObservableField<>();
        alarmInfo.set(curAlarmInfo);
        mBinding.setMyFragment(this);

        mBinding.toMapBt.setOnClickListener(this);
        mBinding.liftAlarmBt.setOnClickListener(this);
        mBinding.belongLinesBt.setOnClickListener(this);
        mBinding.waveformBt.setOnClickListener(this);
        mBinding.buildAlarmAnalysisBt.setOnClickListener(this);
    }

    @Override
    public void bindViewModel() {
        AlarmViewModel.Factory factory = new AlarmViewModel.Factory(getActivity().getApplication(), curAlarmInfo.getKey());
        alarmViewModel = ViewModelProviders.of(this, factory).get(AlarmViewModel.class);
        alarmViewModel.setReadAlarm(curAlarmInfo.getKey());
    }

    @Override
    public void subscribeToModel() {
        alarmViewModel.getmObservableAlarmInfo().observe(this, alarmInfo -> {
            if (null != alarmInfo) {
                LogUtil.d(TAG, alarmInfo.getDetails());
                this.alarmInfo.set(alarmInfo);
            }
        });
    }

    @Override
    public void onClick(View view) {
        EventBusMessage eventBusMessage;
        switch (view.getId()) {
            case R.id.to_map_bt:
                eventBusMessage = new EventBusMessage(MapFragment.TAG);
                Log.d(TAG, "pipeId = " + curAlarmInfo.getPipeId());
                eventBusMessage.setArg1(String.valueOf(curAlarmInfo.getPipeId()));
                EventBus.getDefault().post(eventBusMessage);
                break;
            case R.id.lift_alarm_bt:

                break;
            case R.id.belong_lines_bt:

                break;
            case R.id.waveform_bt:
                EventBus.getDefault().post(new EventBusMessage(WaveFormFragment.TAG));

                break;
            case R.id.build_alarm_analysis_bt:
                eventBusMessage = new EventBusMessage(AlarmAnalysisFragment.TAG);
                eventBusMessage.setArg1(curAlarmInfo);
                EventBus.getDefault().post(eventBusMessage);
                break;
        }
    }
}
