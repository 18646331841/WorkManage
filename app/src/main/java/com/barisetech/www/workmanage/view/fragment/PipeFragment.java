package com.barisetech.www.workmanage.view.fragment;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.PipeAdapter;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.databinding.FragmentPipeBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class PipeFragment extends BaseFragment {
    public static final String TAG = "PipeFragment";

    FragmentPipeBinding mBinding;
    private PipeAdapter pipeAdapter;
    private List<PipeInfo> pipeInfoList;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private PipeViewModel pipeViewModel;
    private Disposable curDisposable;
    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;

    public PipeFragment() {
        // Required empty public constructor
    }

    public static PipeFragment newInstance() {
        PipeFragment fragment = new PipeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pipeInfoList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe));
        toolbarInfo.setTwoText("新增");
        observableToolbar.set(toolbarInfo);

        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {

        pipeAdapter = new PipeAdapter(pipeInfoList, getContext(), item -> {
            if (item instanceof PipeInfo) {
                EventBusMessage eventBusMessage = new EventBusMessage(PipeDetailFragment.TAG);
                eventBusMessage.setArg1(item);
                EventBus.getDefault().post(eventBusMessage);
            }
        });
        loadMoreWrapper = new BaseLoadMoreWrapper(pipeAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.pipeList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.pipeList.setAdapter(loadMoreWrapper);
        mBinding.pipeList.setItemAnimator(new DefaultItemAnimator());

        mBinding.pipeList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (pipeInfoList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - pipeInfoList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(pipeInfoList.size(), count);
                    } else {
                        updateRecyclerView(pipeInfoList.size(), PAGE_COUNT);
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

        ReqAllPipe reqAllPipe = new ReqAllPipe();
        reqAllPipe.setPipeId("0");
        reqAllPipe.setStartIndex(String.valueOf(formIndex));
        reqAllPipe.setNumberOfRecords(String.valueOf(toIndex));

        curDisposable = pipeViewModel.reqAllPipe(reqAllPipe);
    }

    private void getPipeNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        pipeViewModel.reqPipeNum();
    }

    @Override
    public void bindViewModel() {
        pipeViewModel = ViewModelProviders.of(this).get(PipeViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeViewModel.getmObservableAllPipe().hasObservers()) {
            pipeViewModel.getmObservableAllPipe().observe(this, pipeInfos -> {
                if (PipeFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

                    if (null != pipeInfos) {
                        if (pipeInfos.size() > 0) {
                            pipeInfoList.addAll(pipeInfos);
                            LogUtil.d(TAG, "load complete = " + pipeInfos);
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

        if (!pipeViewModel.getmObservablePipeNum().hasObservers()) {
            pipeViewModel.getmObservablePipeNum().observe(this, integer -> {
                if (PipeFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
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

        if (null == pipeInfoList || pipeInfoList.size() <= 0) {
            getPipeNums();
        }
    }
}
