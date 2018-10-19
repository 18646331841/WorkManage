package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.NewsListAdapter;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.news.ReqNewsInfos;
import com.barisetech.www.workmanage.databinding.FragmentNewsListBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.NewsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class NewsListFragment extends BaseFragment {
    public static final String TAG = "NewsListFragment";

    private NewsViewModel newsViewModel;
    private FragmentNewsListBinding mBinding;
    private static final int PAGE_COUNT = 10;//每次加载10个
    private NewsListAdapter newsListAdapter;
    private BaseLoadMoreWrapper loadMoreWrapper;

    private String curNewsType = "1";//默认类型
    private Disposable curDisposable;

    private List<NewsInfo> curList;
    /**
     * 最多有多少条
     */
    private int maxNum = 0;

    public NewsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curList = new ArrayList<>();
    }

    public static NewsListFragment newInstance() {
        NewsListFragment fragment = new NewsListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_list, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_news));
        toolbarInfo.setTwoText(getString(R.string.news_release));
        observableToolbar.set(toolbarInfo);

        initView();

        return mBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        newsViewModel.getmObservableNewsInfos().setValue(null);
        LogUtil.d(TAG, "list = " + curList);

    }

    private void initView() {
        mBinding.toolbar.tvTwo.setOnClickListener(view -> {
            EventBus.getDefault().post(new EventBusMessage(NewsAddFragment.TAG));
        });

        mBinding.newsListAll.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            if (radioButton.isChecked()) {
                closeDisposable();
                curList.clear();

                curNewsType = "";//全部类型
                getDatas(0, PAGE_COUNT);
            }
        });
        mBinding.newsListDefault.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            if (radioButton.isChecked()) {
                closeDisposable();
                curList.clear();

                curNewsType = "1";//全部类型
                getDatas(0, PAGE_COUNT);
            }
        });
        mBinding.newsListCompany.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            if (radioButton.isChecked()) {
                closeDisposable();
                curList.clear();

                curNewsType = "2";//全部类型
                getDatas(0, PAGE_COUNT);
            }
        });
        mBinding.newsListIndustry.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            if (radioButton.isChecked()) {
                closeDisposable();
                curList.clear();

                curNewsType = "3";//全部类型
                getDatas(0, PAGE_COUNT);
            }
        });

        initRecyclerView();

        newsListAdapter.setItemCallBack(item -> {
            if (item instanceof NewsInfo) {
                NewsInfo newsInfo = (NewsInfo) item;
                if (TextUtils.isEmpty(newsInfo.getWebUrl())) {
                    ToastUtil.showToast(getString(R.string.news_list_noLink));
                    return;
                }
                EventBusMessage eventBusMessage = new EventBusMessage(NewsDetailsFragment.TAG);
                eventBusMessage.setArg1(newsInfo.getWebUrl());
                EventBus.getDefault().post(eventBusMessage);
            }
        });
    }

    private void closeDisposable() {
        if (null != curDisposable) {
            curDisposable.dispose();
        }
    }

    private void initRecyclerView() {
        // 初始化mBinding.newsListRecycler的Adapter
        // 第一个参数为数据，上拉加载的原理就是分页，所以我设置常量PAGE_COUNT=10，即每次加载10个数据
        // 第二个参数为Context
        // 第三个参数为hasMore，是否有新数据
        newsListAdapter = new NewsListAdapter(curList, getContext());
        loadMoreWrapper = new BaseLoadMoreWrapper(newsListAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.newsListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.newsListRecycler.setAdapter(loadMoreWrapper);
        mBinding.newsListRecycler.setItemAnimator(new DefaultItemAnimator());

        mBinding.newsListRecycler.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (curList.size() == maxNum) {
//                    if (maxNum != 0) {
                        //已加载到最大，不再加载
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - curList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(curList.size(), count);
                    } else {
                        updateRecyclerView(curList.size(), PAGE_COUNT);
                    }
                }
            }
        });
    }

    // 上拉加载时调用的更新RecyclerView的方法
    private void updateRecyclerView(int fromIndex, int toIndex) {
        // 获取从fromIndex到toIndex的数据
        getDatas(fromIndex, toIndex);
    }

    private void getDatas(int fromIndex, int toIndex) {
        LogUtil.d(TAG, "fromIndex =" + fromIndex + ", toIndex = " + toIndex);
//        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
        if (toIndex <= 0) {
            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqNewsInfos reqNewsInfos = new ReqNewsInfos();
        reqNewsInfos.setStartIndex(String.valueOf(fromIndex));
        reqNewsInfos.setNumberOfRecords(String.valueOf(toIndex));
        reqNewsInfos.setType(curNewsType);

        curDisposable = newsViewModel.reqQueryNews(reqNewsInfos);
    }

    private void getNewNum() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        newsViewModel.reqNewsNum();
    }

    @Override
    public void bindViewModel() {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!newsViewModel.getmObservableNewsInfos().hasObservers()) {
            //防止多次订阅
            newsViewModel.getmObservableNewsInfos().observe(this, newsInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

                    if (null != newsInfos) {
                        if (newsInfos.size() > 0) {
                            curList.addAll(newsInfos);
                            LogUtil.d(TAG, "load complete = " + curList);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
                        if (null != curList && curList.size() > 0) {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    }
                }
            });
        }

        if (!newsViewModel.getmObservableNewsNum().hasObservers()) {
            newsViewModel.getmObservableNewsNum().observe(this, integer -> {
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

        if (null == curList || curList.size() <= 0) {
//            getDatas(0, PAGE_COUNT);
            getNewNum();
        }
    }
}
