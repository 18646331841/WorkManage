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
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.databinding.FragmentPipeBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;

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

        pipeAdapter = new PipeAdapter(pipeInfoList, getContext());
        loadMoreWrapper = new BaseLoadMoreWrapper(pipeAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.pipeList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.pipeList.setAdapter(loadMoreWrapper);
        mBinding.pipeList.setItemAnimator(new DefaultItemAnimator());

        mBinding.pipeList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                updateRecyclerView(pipeInfoList.size(), PAGE_COUNT);
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);
    }

    private void getDatas(int formIndex, int toIndex) {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqAllPipe reqAllPipe = new ReqAllPipe();
        reqAllPipe.setPipeId("0");
        reqAllPipe.setStartIndex(String.valueOf(formIndex));
        reqAllPipe.setNumberOfRecords(String.valueOf(toIndex));

        curDisposable = pipeViewModel.reqAllPipe(reqAllPipe);
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
                        if (null != pipeInfoList && pipeInfoList.size() > 0) {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    }
                }
            });
        }

        if (null == pipeInfoList || pipeInfoList.size() <= 0) {
            getDatas(0, PAGE_COUNT);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pipeViewModel.getmObservableAllPipe().setValue(null);
    }
}
