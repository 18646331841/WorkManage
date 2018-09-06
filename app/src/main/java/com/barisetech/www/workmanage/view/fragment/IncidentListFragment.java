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
import com.barisetech.www.workmanage.adapter.IncidentListAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.databinding.FragmentAlarmListBinding;
import com.barisetech.www.workmanage.databinding.FragmentIncidentListBinding;
import com.barisetech.www.workmanage.model.IncidentModel;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.viewmodel.AlarmViewModel;
import com.barisetech.www.workmanage.viewmodel.IncidentViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncidentListFragment extends BaseFragment {
    public static final String TAG = "IncidentListFragment";

    private IncidentViewModel incidentViewModel;
    private IncidentListAdapter incidentListAdapter;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private List<IncidentInfo> incidentInfoList;
    FragmentIncidentListBinding mBinding;

    private String startTime = "";
    private String endTime = "";
    private Disposable curDisposable;

    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private int curType;//默认0为全部类型

    public IncidentListFragment() {
        // Required empty public constructor
    }

    public static IncidentListFragment newInstance() {
        IncidentListFragment fragment = new IncidentListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incidentInfoList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_incident_list, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_incident));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        initRecyclerView();

        mBinding.searchLayout.tvFilter.setOnClickListener(view -> {
            transFilterLayout();
        });
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

        incidentListAdapter = new IncidentListAdapter(incidentInfoList, getContext(), itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(incidentListAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.alarmList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.alarmList.setAdapter(loadMoreWrapper);
        mBinding.alarmList.setItemAnimator(new DefaultItemAnimator());

        mBinding.alarmList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (incidentInfoList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - incidentInfoList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(incidentInfoList.size(), count);
                    } else {
                        updateRecyclerView(incidentInfoList.size(), PAGE_COUNT);
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
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

    }

    private void getListNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

//        incidentViewModel.reqIncidentNum();
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
        incidentViewModel = ViewModelProviders.of(this).get(IncidentViewModel.class);
    }

    @Override
    public void subscribeToModel() {

    }
}
