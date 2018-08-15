package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.adapter.MessageAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.databinding.FragmentMessageBinding;
import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.viewmodel.AlarmViewModel;
import com.barisetech.www.workmanage.viewmodel.IncidentViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class Messagefragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "Messagefragment";

    private AlarmViewModel alarmViewModel;
    private List<MessageInfo> curMessageList = new ArrayList<>();
    private List<? extends MessageInfo> curAlarmList;
    private List<? extends MessageInfo> curIncidentList;

    private FragmentMessageBinding mBinding;
    private MessageAdapter messageAdapter;
//    private CommonDialogFragment commonDialogFragment;
    private IncidentViewModel incidentViewModel;

    public Messagefragment() {
    }

    public static Messagefragment newInstance() {
        Messagefragment fragment = new Messagefragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());

        messageAdapter = new MessageAdapter(itemCallBack);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.messageRecyclerView.setLayoutManager(llm);
        mBinding.messageRecyclerView.setAdapter(messageAdapter);
        mBinding.imgWarn.setOnClickListener(this);
        mBinding.imgAnalysisWarn.setOnClickListener(this);
        mBinding.imgEvent.setOnClickListener(this);
        mBinding.imgNews.setOnClickListener(this);
        return mBinding.getRoot();
    }

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof AlarmInfo) {
            AlarmInfo alarmInfo = (AlarmInfo) item;
            LogUtil.d(TAG, alarmInfo.toContent());
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_warn:
                EventBus.getDefault().post(new EventBusMessage(AlarmListFragment.TAG));
                break;
            case R.id.img_event:
                EventBus.getDefault().post(new EventBusMessage(EventFragment.TAG));
                break;
            case R.id.img_analysis_warn:
                EventBus.getDefault().post(new EventBusMessage(AnalysisWarnFragment.TAG));
                break;
            case R.id.img_news:
                EventBus.getDefault().post(new EventBusMessage(NewsFragment.TAG));
                break;
        }

    }

    @Override
    public void bindViewModel() {
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        incidentViewModel = ViewModelProviders.of(this).get(IncidentViewModel.class);

//        commonDialogFragment = DialogFragmentHelper.showProgress(getFragmentManager(), getString
//                (R.string.dialog_progress_text), true);
        alarmViewModel.getAllAlarm();
        incidentViewModel.reqAllIncident();
    }

    @Override
    public void subscribeToModel() {
        alarmViewModel.getNotReadAlarmInfos().observe(this, alarmInfos -> {
            LogUtil.d(TAG, "observe alarmInfos = " + alarmInfos);
            if (null != alarmInfos) {
//                if (null != commonDialogFragment) {
//                    commonDialogFragment.dismiss();
//                }
                curAlarmList = alarmInfos;
                curMessageList.clear();
                curMessageList.addAll(curAlarmList);
                if (null != curIncidentList) {
                    curMessageList.addAll(curIncidentList);
                }

                messageAdapter.setCommentList(curMessageList);
            }
        });

        incidentViewModel.getmObservableAllIncidentByRead().observe(this, incidentInfos -> {
            if (null != incidentInfos) {
                curIncidentList = incidentInfos;

                curMessageList.clear();
                if (null != curAlarmList) {
                    curMessageList.addAll(curAlarmList);
                }
                curMessageList.addAll(curIncidentList);

                messageAdapter.setCommentList(curMessageList);
            }
        });
    }
}
