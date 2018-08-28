package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.SiteAdapter;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentSiteBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.disposables.Disposable;

public class SiteFragment extends BaseFragment {


    public static final String TAG = "SiteFragment";
    FragmentSiteBinding mBinding;
    private SiteAdapter siteAdapter;
    private SiteViewModel siteViewModel;
    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private BaseLoadMoreWrapper loadMoreWrapper;

    private String curSiteType = "1";
    private Disposable curDisposable;

    private List<SiteBean> siteList;
    private Observer<List<SiteBean>> observerList;

    public SiteFragment() {

    }

    public static SiteFragment newInstance() {
        SiteFragment fragment = new SiteFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        siteList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_site, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.tv_site));
        toolbarInfo.setTwoText(getString(R.string.message_mission));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.toolbar.tvTwo.setOnClickListener(view -> {
            EventBus.getDefault().post(new EventBusMessage(AddSiteFragment.TAG));
        });

        initRecyclerView();


    }

    private void initRecyclerView() {

        siteAdapter = new SiteAdapter(siteList, getContext());
        loadMoreWrapper = new BaseLoadMoreWrapper(siteAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.siteList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.siteList.setAdapter(loadMoreWrapper);
        mBinding.siteList.setItemAnimator(new DefaultItemAnimator());

        mBinding.siteList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                updateRecyclerView(siteList.size(), PAGE_COUNT);
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);

    }

    private void getDatas(int formIndex, int toIndex) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqSiteInfos reqSiteInfos = new ReqSiteInfos();
        reqSiteInfos.setStartIndex(String.valueOf(formIndex));
        reqSiteInfos.setNumberOfRecords(String.valueOf(toIndex));

        curDisposable = siteViewModel.reqQuerySite(reqSiteInfos);
    }

    @Override
    public void bindViewModel() {
        siteViewModel = ViewModelProviders.of(this).get(SiteViewModel.class);

    }

    @Override
    public void subscribeToModel() {

        observerList = sitebean -> {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

                if (null != sitebean) {
                    if (sitebean.size() > 0) {
                        siteList.addAll(sitebean);
                        LogUtil.d(TAG, "load complete = " + siteList);
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                } else {
                    if (null != siteList && siteList.size() > 0) {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            }
        };

        if (!siteViewModel.getmObservableSiteInfos().hasObservers()) {
            //防止多次订阅
           siteViewModel.getmObservableSiteInfos().observe(this, observerList);
        }

        if (null == siteList || siteList.size() <= 0) {
            getDatas(0, PAGE_COUNT);
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        siteViewModel.getmObservableSiteInfos().setValue(null);
    }
}
