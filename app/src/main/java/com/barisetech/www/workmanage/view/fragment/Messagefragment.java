package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.adapter.MessageAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.databinding.FragmentMessageBinding;
import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.AlarmViewModel;
import com.barisetech.www.workmanage.viewmodel.IncidentViewModel;
import com.barisetech.www.workmanage.widget.popumenu.FloatMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Messagefragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "Messagefragment";

    private AlarmViewModel alarmViewModel;
    private List<MessageInfo> curMessageList;
    private List<? extends MessageInfo> curAlarmList;
    private List<? extends MessageInfo> curIncidentList;

    private FragmentMessageBinding mBinding;
    private MessageAdapter messageAdapter;
    private IncidentViewModel incidentViewModel;
    private Point mpoint = new Point();

    private boolean flag = false;
    private AlarmInfo curAlarmInfo;

    public Messagefragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curMessageList = new ArrayList<>();
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

        messageAdapter = new MessageAdapter(itemCallBack, curMessageList);
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
        messageAdapter.setOnItemLongClickListener((view, position) -> {
            FloatMenu floatMenu = new FloatMenu(getActivity());
            floatMenu.inflate(R.layout.layout_menu_warn);
            floatMenu.show(mpoint);
            floatMenu.setOnItemClickListener((v, position1) -> {
                switch (position1){
                    case 1:
                        curAlarmInfo = (AlarmInfo) curMessageList.get(position);
                        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
                        alarmViewModel.reqLiftAlarm(curAlarmInfo.getKey());
                        break;
                    case 4:
                        EventBusMessage eventBusMessage = new EventBusMessage(AlarmAnalysisFragment.TAG);
                        eventBusMessage.setArg1(curMessageList.get(position));
                        EventBus.getDefault().post(eventBusMessage);
                        break;
                    case 5:
                        mBinding.mulitpleMenu.setVisibility(View.VISIBLE);
                        mBinding.tvNewMsg.setVisibility(View.GONE);
                        messageAdapter.setFlag(MessageAdapter.SHOW_ALL);
                        mBinding.allSelectTv.setText("全选");
                        flag = false;
                        messageAdapter.notifyDataSetChanged();
                        break;
                }
            });
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
                EventBus.getDefault().post(new EventBusMessage(IncidentListFragment.TAG));
                break;
            case R.id.img_analysis_alarm:
                EventBusMessage eventBusMessage = new EventBusMessage(AlarmAnalysisListFragment.TAG);
                EventBus.getDefault().post(eventBusMessage);
                break;
            case R.id.img_news:
                EventBus.getDefault().post(new EventBusMessage(NewsListFragment.TAG));
                break;
            case R.id.all_select_tv:
                if (flag){
                    messageAdapter.neverall();
                    mBinding.allSelectTv.setText("全选");
                    flag = false;
                }else {
                    messageAdapter.All();
                    mBinding.allSelectTv.setText("取消全选");
                    flag = true;
                }
                break;
            case R.id.mark_read_tv:
                markReadWork();
                break;
            case R.id.cancel_tv:
                mBinding.mulitpleMenu.setVisibility(View.GONE);
                mBinding.tvNewMsg.setVisibility(View.VISIBLE);
                messageAdapter.setFlag(MessageAdapter.HIDE_ALL);
                break;

        }

    }

    private void markReadWork() {
        LogUtil.d(TAG, "mapSize = " + messageAdapter.map.size());
        Set<Map.Entry<Integer, MessageInfo>> entries = messageAdapter.map.entrySet();
        for (Map.Entry<Integer, MessageInfo> entry : entries) {
            LogUtil.d(TAG, "type = " + entry.getKey());
        }
        messageAdapter.neverall();
        mBinding.mulitpleMenu.setVisibility(View.GONE);
        mBinding.tvNewMsg.setVisibility(View.VISIBLE);
        messageAdapter.setFlag(MessageAdapter.HIDE_ALL);
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
        if (!alarmViewModel.getNotReadAlarmInfos().hasObservers()) {

            alarmViewModel.getNotReadAlarmInfos().observe(this, alarmInfos -> {
                if (null != alarmInfos && alarmInfos.size() != 0) {
                    LogUtil.d(TAG, "observe alarmInfos = " + alarmInfos.size());
                    mBinding.messageAlarmNum.setText(String.valueOf(alarmInfos.size()));
                    mBinding.messageAlarmNum.setVisibility(View.VISIBLE);

                    curAlarmList = alarmInfos;
                    curMessageList.clear();
                    curMessageList.addAll(curAlarmList);
                    if (null != curIncidentList) {
                        curMessageList.addAll(curIncidentList);
                    }

                    LogUtil.d(TAG, "alarminfos curMessageList = " + curMessageList.size());
                    messageAdapter.notifyDataSetChanged();
                }
            });
        }

        if (!incidentViewModel.getmObservableAllIncidentByRead().hasObservers()) {

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
                    messageAdapter.notifyDataSetChanged();
                }
            });
        }

        if (!alarmViewModel.getmObservableLiftAlarm().hasObservers()) {

            alarmViewModel.getmObservableLiftAlarm().observe(this, aBoolean -> {
                if (null != aBoolean) {
                    if (aBoolean) {
                        ToastUtil.showToast(getString(R.string.alarm_lift_success));
                        if (curAlarmInfo != null) {
                            alarmViewModel.setLiftAlarm(curAlarmInfo);
                        }
                    } else {
                        ToastUtil.showToast(getString(R.string.alarm_lift_fail));
                    }
                }
            });
        }

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
        incidentViewModel.reqAllIncidentToDB();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
