package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.news.ReqNewsInfos;
import com.barisetech.www.workmanage.databinding.FragmentNewsListBinding;
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
    private GridLayoutManager mLayoutManager;
    private int lastVisibleItem = 0;

    private String curNewsType = "1";//默认类型
    private Disposable curDisposable;

    private List<NewsInfo> curList;
    private Observer<List<NewsInfo>> observerList;

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
        toolbarInfo.setOneText("新增");
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
        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            EventBus.getDefault().post(new EventBusMessage(NewsAddFragment.TAG));
        });

        mBinding.newsListAll.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            if (radioButton.isChecked()) {
                curDisposable.dispose();
                curList.clear();

                curNewsType = "";//全部类型
                getDatas(0, PAGE_COUNT);
            }
        });
        mBinding.newsListDefault.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            if (radioButton.isChecked()) {
                curDisposable.dispose();
                curList.clear();

                curNewsType = "1";//全部类型
                getDatas(0, PAGE_COUNT);
            }
        });
        mBinding.newsListCompany.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            if (radioButton.isChecked()) {
                curDisposable.dispose();
                curList.clear();

                curNewsType = "2";//全部类型
                getDatas(0, PAGE_COUNT);
            }
        });
        mBinding.newsListIndustry.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            if (radioButton.isChecked()) {
                curDisposable.dispose();
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

    private void initRecyclerView() {
        // 初始化mBinding.newsListRecycler的Adapter
        // 第一个参数为数据，上拉加载的原理就是分页，所以我设置常量PAGE_COUNT=10，即每次加载10个数据
        // 第二个参数为Context
        // 第三个参数为hasMore，是否有新数据
        newsListAdapter = new NewsListAdapter(getContext());
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mBinding.newsListRecycler.setLayoutManager(mLayoutManager);
        mBinding.newsListRecycler.setAdapter(newsListAdapter);
        mBinding.newsListRecycler.setItemAnimator(new DefaultItemAnimator());

        // 实现上拉加载重要步骤，设置滑动监听器，mBinding.newsListRecycler自带的ScrollListener
        mBinding.newsListRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 在newState为滑到底部时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果没有隐藏footView，那么最后一个条目的位置就比我们的getItemCount少1，自己可以算一下
                    if (!newsListAdapter.isFadeTips() && lastVisibleItem + 1 == newsListAdapter.getItemCount
                            ()) {
                        // 然后调用updateRecyclerView方法更新RecyclerView
                        updateRecyclerView(newsListAdapter.getRealLastPosition(), PAGE_COUNT);
                    }

                    // 如果隐藏了提示条，我们又上拉加载时，那么最后一个条目就要比getItemCount要少2
                    if (newsListAdapter.isFadeTips() && lastVisibleItem + 2 == newsListAdapter.getItemCount()) {
                        // 然后调用updateRecyclerView方法更新RecyclerView
                        updateRecyclerView(newsListAdapter.getRealLastPosition(), PAGE_COUNT);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 在滑动完成后，拿到最后一个可见的item的位置
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
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

        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
        newsListAdapter.resetDatas();

        ReqNewsInfos reqNewsInfos = new ReqNewsInfos();
        reqNewsInfos.setStartIndex(String.valueOf(fromIndex));
        reqNewsInfos.setNumberOfRecords(String.valueOf(toIndex));
        reqNewsInfos.setType(curNewsType);

        curDisposable = newsViewModel.reqQueryNews(reqNewsInfos);
    }

    @Override
    public void bindViewModel() {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        observerList = newsInfos -> {
            if (null != newsInfos) {
                if (newsInfos.size() > 0) {
                    curList.addAll(newsInfos);

                    if (newsInfos.size() < PAGE_COUNT) {
                        newsListAdapter.updateList(curList, false);
                    } else {
                        newsListAdapter.updateList(curList, true);
                    }
                } else {
                    newsListAdapter.updateList(null, false);
                }
            } else {
                if (null == curList) {
                    newsListAdapter.updateList(null, false);
                } else {
                    newsListAdapter.updateList(curList, false);
                }
            }
        };

        if (!newsViewModel.getmObservableNewsInfos().hasObservers()) {
            //防止多次订阅
            newsViewModel.getmObservableNewsInfos().observe(this, observerList);
        }

        if (null == curList || curList.size() <= 0) {
            getDatas(0, PAGE_COUNT);
        } else {
            newsListAdapter.updateList(curList, false);
        }
    }
}
