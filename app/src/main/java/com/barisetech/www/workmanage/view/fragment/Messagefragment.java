package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.barisetech.www.workmanage.adapter.MessageAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.databinding.FragmentMessageBinding;
import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.viewmodel.AlarmViewModel;
import com.barisetech.www.workmanage.viewmodel.IncidentViewModel;
import com.barisetech.www.workmanage.widget.popumenu.FloatMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private IncidentViewModel incidentViewModel;
    private Point mpoint = new Point();

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
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_message));
        toolbarInfo.setTwoText(getString(R.string.message_mission));
        observableToolbar.set(toolbarInfo);

        EventBus.getDefault().register(this);

        messageAdapter = new MessageAdapter(itemCallBack);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.messageRecyclerView.setLayoutManager(llm);
        mBinding.messageRecyclerView.setAdapter(messageAdapter);


        mBinding.allSelectTv.setOnClickListener(this);
        mBinding.cancelTv.setOnClickListener(this);
        mBinding.markReadTv.setOnClickListener(this);
        mBinding.scrollTitleView.setmRecyclerView(mBinding.messageRecyclerView);
        mBinding.imgAlarm.setOnClickListener(this);
        mBinding.imgAnalysisAlarm.setOnClickListener(this);
        mBinding.imgIncident.setOnClickListener(this);
        mBinding.imgNews.setOnClickListener(this);
        messageAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FloatMenu floatMenu = new FloatMenu(getActivity());
//                floatMenu.items("菜单1", "菜单2", "菜单3");
                floatMenu.inflate(R.layout.layout_menu_warn);
                floatMenu.show(mpoint);
                floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
                    @Override
                    public void onClick(View v, int position) {
                        switch (position){
                            case 1:
                                Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                                break;
                            case 5:
                                mBinding.mulitpleMenu.setVisibility(View.VISIBLE);
                                mBinding.tvNewMsg.setVisibility(View.GONE);
                                messageAdapter.setFlag(1);
                                messageAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
            }
        });
        return mBinding.getRoot();
    }

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof AlarmInfo) {
            AlarmInfo alarmInfo = (AlarmInfo) item;
            EventBusMessage eventBusMessage = new EventBusMessage(AlarmDetailsFragment.TAG);
            eventBusMessage.setArg1(alarmInfo);
            EventBus.getDefault().post(eventBusMessage);
            LogUtil.d(TAG, alarmInfo.toContent());
        } else if (item instanceof IncidentInfo) {
            IncidentInfo incidentInfo = (IncidentInfo) item;
            LogUtil.d(TAG, incidentInfo.toContent());
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_alarm:
                EventBus.getDefault().post(new EventBusMessage(AlarmListFragment.TAG));
                break;
            case R.id.img_incident:
                EventBus.getDefault().post(new EventBusMessage(EventFragment.TAG));
                break;
            case R.id.img_analysis_alarm:
                EventBus.getDefault().post(new EventBusMessage(AlarmAnalysisFragment.TAG));
                break;
            case R.id.img_news:
//                EventBus.getDefault().post(new EventBusMessage(NewsListFragment.TAG));
                EventBus.getDefault().post(new EventBusMessage(NewsAddFragment.TAG));
                break;
            case R.id.all_select_tv:
                break;
            case R.id.mark_read_tv:
                break;
            case R.id.cancel_tv:
                mBinding.mulitpleMenu.setVisibility(View.GONE);
                mBinding.tvNewMsg.setVisibility(View.VISIBLE);
                messageAdapter.setFlag(2);
                messageAdapter.notifyDataSetChanged();
                break;

        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(Point point) {
       mpoint = point;
    }


    @Override
    public void bindViewModel() {
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        incidentViewModel = ViewModelProviders.of(this).get(IncidentViewModel.class);

    }

    @Override
    public void subscribeToModel() {
        alarmViewModel.getNotReadAlarmInfos().observe(this, alarmInfos -> {
            if (null != alarmInfos && alarmInfos.size() != 0) {
                LogUtil.d(TAG, "observe alarmInfos = " + alarmInfos.size());
//                if (null != commonDialogFragment) {
//                    commonDialogFragment.dismiss();
//                }
                curAlarmList = alarmInfos;
                curMessageList.clear();
                curMessageList.addAll(curAlarmList);
                if (null != curIncidentList) {
                    curMessageList.addAll(curIncidentList);
                }

                LogUtil.d(TAG, "alarminfos curMessageList = " + curMessageList.size());
                messageAdapter.setCommentList(curMessageList);
            }
        });

        incidentViewModel.getmObservableAllIncidentByRead().observe(this, incidentInfos -> {
            if (null != incidentInfos && incidentInfos.size() != 0) {
                LogUtil.d(TAG, "observe incidentInfos = " + incidentInfos.size());
                curIncidentList = incidentInfos;

                curMessageList.clear();
                if (null != curAlarmList) {
                    curMessageList.addAll(curAlarmList);
                }
                curMessageList.addAll(curIncidentList);

                LogUtil.d(TAG, "incidents curMessageList = " + curMessageList.size());
                messageAdapter.setCommentList(curMessageList);
            }
        });

        //TODO 需要确定好逻辑
//        ReqAllAlarm reqAllAlarm = new ReqAllAlarm();
//        String endTime = TimeUtil.ms2Date(System.currentTimeMillis());
//        reqAllAlarm.setStartIndex("1");
//        reqAllAlarm.setNumberOfRecords("1");
//        reqAllAlarm.setIsAllAlarm("true");
//        reqAllAlarm.setGetByTimeDiff("true");
//        reqAllAlarm.setEndTime(endTime);
//        reqAllAlarm.setStartTime(SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_LAST_TIME_NEWINFO, endTime));

        alarmViewModel.getAllUnliftAlarm();
        incidentViewModel.reqAllIncident();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
