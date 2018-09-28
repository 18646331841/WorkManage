package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
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
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.incident.ReqAllIncident;
import com.barisetech.www.workmanage.bean.incident.ReqIncidentSelectItem;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentMessageBinding;
import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.AlarmViewModel;
import com.barisetech.www.workmanage.viewmodel.IncidentViewModel;
import com.barisetech.www.workmanage.viewmodel.SignInViewModel;
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

    private SignInViewModel signInViewModel;

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
        toolbarInfo.setOneText(getString(R.string.message_mission));
        observableToolbar.set(toolbarInfo);

        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            EventBusMessage mapMessage = new EventBusMessage(PlanListFragment.TAG);
            EventBus.getDefault().post(mapMessage);
        });

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
            MessageInfo messageInfo = curMessageList.get(position);
            if (messageInfo instanceof AlarmInfo) {
                AlarmInfo alarmInfo = (AlarmInfo) messageInfo;
                floatMenu.inflate(R.layout.layout_menu_warn);
                floatMenu.show(mpoint);
                floatMenu.setOnItemClickListener((v, position1) -> {
                    switch (position1) {
                        case 0:
                            curAlarmInfo = alarmInfo;
                            EventBusMessage mapMessage = new EventBusMessage(MapFragment.TAG);
                            mapMessage.setArg1(String.valueOf(curAlarmInfo.getPipeId()));
                            EventBus.getDefault().post(mapMessage);
                            break;
                        case 1:
                            curAlarmInfo = alarmInfo;
                            EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
                            alarmViewModel.reqLiftAlarm(curAlarmInfo.getKey());
                            break;
                        case 2:
                            curAlarmInfo = alarmInfo;
                            EventBusMessage pipeMessage = new EventBusMessage(PipeDetailFragment.TAG);
                            PipeInfo pipeInfo = new PipeInfo();
                            pipeInfo.PipeId = curAlarmInfo.getPipeId();
                            pipeInfo.Name = BaseConstant.DATA_REQUEST_NAME;
                            pipeMessage.setArg1(pipeInfo);
                            EventBus.getDefault().post(pipeMessage);
                            break;
                        case 3:
                            curAlarmInfo = alarmInfo;
                            EventBusMessage waveFormMessage = new EventBusMessage(WaveFormFragment.TAG);
                            waveFormMessage.setArg1(curAlarmInfo.getPipeId());
                            EventBus.getDefault().post(waveFormMessage);
                            break;
                        case 4:
                            EventBusMessage eventBusMessage = new EventBusMessage(AlarmAnalysisFragment.TAG);
                            eventBusMessage.setArg1(alarmInfo);
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
            } else if (messageInfo instanceof IncidentInfo) {
                IncidentInfo incidentInfo = (IncidentInfo) messageInfo;
                floatMenu.inflate(R.layout.layout_menu_incident);
                floatMenu.show(mpoint);
                floatMenu.setOnItemClickListener((v, position1) -> {
                    switch (position1) {
                        case 0:
                            EventBusMessage siteMessage = new EventBusMessage(SiteDetailFragment.TAG);
                            SiteBean siteBean = new SiteBean();
                            siteBean.SiteId = incidentInfo.getSiteId();
                            siteBean.Name = BaseConstant.DATA_REQUEST_NAME;
                            siteMessage.setArg1(siteBean);
                            EventBus.getDefault().post(siteMessage);
                            break;
                        case 1:
                            EventBusMessage pipeMessage = new EventBusMessage(PipeDetailFragment.TAG);
                            PipeInfo pipeInfo = new PipeInfo();
                            pipeInfo.PipeId = incidentInfo.getPipeId();
                            pipeInfo.Name = BaseConstant.DATA_REQUEST_NAME;
                            pipeMessage.setArg1(pipeInfo);
                            EventBus.getDefault().post(pipeMessage);
                            break;
                        case 2:
                            mBinding.mulitpleMenu.setVisibility(View.VISIBLE);
                            mBinding.tvNewMsg.setVisibility(View.GONE);
                            messageAdapter.setFlag(MessageAdapter.SHOW_ALL);
                            mBinding.allSelectTv.setText("全选");
                            flag = false;
                            messageAdapter.notifyDataSetChanged();
                            break;
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
        } else if (item instanceof IncidentInfo) {
            IncidentInfo incidentInfo = (IncidentInfo) item;
            LogUtil.d(TAG, incidentInfo.toContent());
            EventBusMessage eventBusMessage = new EventBusMessage(IncidentDetailsFragment.TAG);
            eventBusMessage.setArg1(incidentInfo);
            EventBus.getDefault().post(eventBusMessage);
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
                if (flag) {
                    messageAdapter.neverall();
                    mBinding.allSelectTv.setText("全选");
                    flag = false;
                } else {
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

    private void getAlarmListNums() {
        alarmViewModel.reqAlarmNum();
    }

    @Override
    public void bindViewModel() {
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        incidentViewModel = ViewModelProviders.of(this).get(IncidentViewModel.class);
        signInViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        signInViewModel.reqReSignIn();
    }

    @Override
    public void subscribeToModel() {
        if (!alarmViewModel.getmObservableNum().hasObservers()) {
            alarmViewModel.getmObservableNum().observe(this, integer -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer) {
                        //TODO


                    }
                }
            });
        }

        if (!alarmViewModel.getNotReadAlarmInfos().hasObservers()) {

            alarmViewModel.getNotReadAlarmInfos().observe(this, alarmInfos -> {
                if (null != alarmInfos && alarmInfos.size() != 0) {
                    LogUtil.d(TAG, "observe alarmInfos = " + alarmInfos.size());


                    curAlarmList = alarmInfos;
                    curMessageList.clear();
                    curMessageList.addAll(curAlarmList);
                    if (null != curIncidentList) {
                        curMessageList.addAll(curIncidentList);
                    }

                    LogUtil.d(TAG, "alarminfos curMessageList = " + curMessageList.size());
                    messageAdapter.notifyDataSetChanged();
                } else {
                    LogUtil.d(TAG, "observe alarmInfos is null");

                    curAlarmList = null;
                    curMessageList.clear();
                    if (null != curIncidentList) {
                        curMessageList.addAll(curIncidentList);
                    }
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
                } else {
                    LogUtil.d(TAG, "observe incidentInfos is null");

                    curIncidentList = null;
                    curMessageList.clear();
                    if (null != curAlarmList) {
                        curMessageList.addAll(curAlarmList);
                    }
                    messageAdapter.notifyDataSetChanged();
                }
            });
        }

        if (!alarmViewModel.getmObservableUnliftAlarmInfos().hasObservers()) {
            alarmViewModel.getmObservableUnliftAlarmInfos().observe(this, alarmInfos -> {
//                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != alarmInfos && alarmInfos.size() > 0) {
                        LogUtil.d(TAG, "unlift alarm num = " + alarmInfos.size());
                        mBinding.messageAlarmNum.setText(String.valueOf(alarmInfos.size()));
                        mBinding.messageAlarmNum.setVisibility(View.VISIBLE);
                    }
//                }
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

//        getAlarmListNums();

        //TODO 需要确定好逻辑
        ReqAllAlarm reqAllAlarm = new ReqAllAlarm();
        reqAllAlarm.setStartIndex("0");
        reqAllAlarm.setNumberOfRecords("0");
        reqAllAlarm.setIsAllAlarm("true");
        reqAllAlarm.setGetByTimeDiff("true");
        reqAllAlarm.setStartTime("1970-1-1 00:00:00");
        reqAllAlarm.setEndTime(TimeUtil.ms2Date(System.currentTimeMillis()));
        alarmViewModel.getAllAlarmByConditionToDB(reqAllAlarm);

        alarmViewModel.getAllUnliftAlarm();

        ReqIncidentSelectItem reqIncidentSelectItem = new ReqIncidentSelectItem();
        reqIncidentSelectItem.setMStartTime("2010-01-01 12:12:12");
        reqIncidentSelectItem.setMEndTime("2019-01-01 12:12:12");
        reqIncidentSelectItem.setTimeQueryChecked("true");
        reqIncidentSelectItem.setSiteIdQueryChecked("false");
        reqIncidentSelectItem.setSiteID("0");
        reqIncidentSelectItem.setMIncidentType("1");

        ReqAllIncident reqAllIncident = new ReqAllIncident();
        reqAllIncident.setStartIndex("1");
        reqAllIncident.setNumberOfRecord("10");
        reqAllIncident.setIncidentSelectItem(reqIncidentSelectItem);
        incidentViewModel.reqAllIncidentToDB(reqAllIncident);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
