package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.AlarmlistAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.databinding.FragmentAlarmListBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.viewmodel.AlarmViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmListFragment extends BaseFragment {
    public static final String TAG = "AlarmListFragment";

    private AlarmViewModel alarmViewModel;
    private FragmentAlarmListBinding mBinding;
    private AlarmlistAdapter alarmlistAdapter;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private List<AlarmInfo> alarmList;

    private String startTime = "";
    private String endTime = "";
    private Disposable curDisposable;
    private Boolean getByTime = false;
    private Boolean getAllAlarm = true;

    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private int curType;//默认0为全部类型

    public AlarmListFragment() {
        // Required empty public constructor
    }

    public static AlarmListFragment newInstance() {
        AlarmListFragment fragment = new AlarmListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_list, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_alarm));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        initRecyclerView();

        mBinding.searchLayout.tvFilter.setOnClickListener(view -> {
            transFilterLayout();
        });

        mBinding.alarmListAll.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            getAllAlarm = true;

            closeDisposable();
            transFilterLayout();
            alarmList.clear();
            if (maxNum >= PAGE_COUNT) {
                getDatas(0, PAGE_COUNT);
            } else {
                getDatas(0, maxNum);
            }
        });
        mBinding.alarmListUnlift.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            getAllAlarm = false;

            closeDisposable();
            transFilterLayout();
            alarmList.clear();
            if (maxNum >= PAGE_COUNT) {
                getDatas(0, PAGE_COUNT);
            } else {
                getDatas(0, maxNum);
            }
        });
    }

    private void closeDisposable() {
        if (null != curDisposable) {
            curDisposable.dispose();
        }
    }

    private void cleanFilterGruop() {
        mBinding.alarmListUnlift.setChecked(false);
        mBinding.alarmListAll.setChecked(false);
    }

    /**
     * 打开或关闭筛选选择框
     */
    private void transFilterLayout() {
        if (mBinding.alarmListFilterLayout.getVisibility() == View.GONE) {
            mBinding.alarmListFilterLayout.setVisibility(View.VISIBLE);
        } else {
            mBinding.alarmListFilterLayout.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {

        alarmlistAdapter = new AlarmlistAdapter(alarmList, getContext(), itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(alarmlistAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.alarmList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.alarmList.setAdapter(loadMoreWrapper);
        mBinding.alarmList.setItemAnimator(new DefaultItemAnimator());

        mBinding.alarmList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (alarmList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - alarmList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(alarmList.size(), count);
                    } else {
                        updateRecyclerView(alarmList.size(), PAGE_COUNT);
                    }
                }
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);
    }

    private void getDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqAllAlarm reqAllAlarm = new ReqAllAlarm();
        reqAllAlarm.setIsAllAlarm(String.valueOf(getAllAlarm));
        reqAllAlarm.setStartIndex(String.valueOf(formIndex));
        reqAllAlarm.setNumberOfRecords(String.valueOf(toIndex));
        reqAllAlarm.setGetByTimeDiff(String.valueOf(getByTime));
        reqAllAlarm.setStartTime(startTime);
        reqAllAlarm.setEndTime(endTime);

        curDisposable = alarmViewModel.reqAllAlarmByCondition(reqAllAlarm);
    }

    private void getListNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        alarmViewModel.reqAlarmNum();
    }

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof AlarmInfo) {
            AlarmInfo alarmInfo = (AlarmInfo) item;
            EventBusMessage eventBusMessage = new EventBusMessage(AlarmDetailsFragment.TAG);
            eventBusMessage.setArg1(alarmInfo);
            EventBus.getDefault().post(eventBusMessage);
        }
    };

    @Override
    public void bindViewModel() {
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!alarmViewModel.getmObservableNetAlarmInfos().hasObservers()) {
            alarmViewModel.getmObservableNetAlarmInfos().observe(this, alarmInfos -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != alarmInfos) {
                        if (alarmInfos.size() > 0) {
                            alarmList.addAll(alarmInfos);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }

        if (!alarmViewModel.getmObservableNum().hasObservers()) {
            alarmViewModel.getmObservableNum().observe(this, integer -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer) {
                        maxNum = integer;
                        if (maxNum >= PAGE_COUNT) {
                            getDatas(0, PAGE_COUNT);
                        } else {
                            getDatas(0, maxNum);
                        }
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }

        if (null == alarmList || alarmList.size() <= 0) {
            getListNums();
        }
    }
}
