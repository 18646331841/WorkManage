package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.DigitizingAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.digitalizer.DigitalizerBean;
import com.barisetech.www.workmanage.bean.digitalizer.ReqAllDigitalizer;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.databinding.FragmentDigitizingBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.viewmodel.DigitalizerViewModel;
import com.barisetech.www.workmanage.widget.QPopuWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class DigitizingFragment extends BaseFragment {

    public static final String TAG = "DigitizingFragment";
    FragmentDigitizingBinding mBinding;
    private DigitizingAdapter digitizingAdapter;
    private List<DigitalizerBean> digitalizerBeanList;
    private BaseLoadMoreWrapper loadMoreWrapper;

    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private DigitalizerViewModel digitalizerViewModel;
    private Disposable curDisposable;

    private Point mPoint = new Point();

    public static DigitizingFragment newInstance() {
        DigitizingFragment fragment = new DigitizingFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        digitalizerBeanList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_digitizing, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.tv_num));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        initRecyclerView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void PointEventBus(Point point) {
        mPoint = point;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initRecyclerView() {
        digitizingAdapter = new DigitizingAdapter(digitalizerBeanList, getContext());
        digitizingAdapter.setOnItemLongClickListener(onItemLongClickListener);
        digitizingAdapter.onClick(itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(digitizingAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.digitizingList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.digitizingList.setAdapter(loadMoreWrapper);
        mBinding.digitizingList.setItemAnimator(new DefaultItemAnimator());

        mBinding.digitizingList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (digitalizerBeanList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - digitalizerBeanList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(digitalizerBeanList.size(), count);
                    } else {
                        updateRecyclerView(digitalizerBeanList.size(), PAGE_COUNT);
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

        ReqAllDigitalizer reqAllDigitalizer = new ReqAllDigitalizer();
        reqAllDigitalizer.setIsGetAll("false");
        reqAllDigitalizer.setSiteId("-1");
        reqAllDigitalizer.setStartIndex(String.valueOf(formIndex));
        reqAllDigitalizer.setNumberOfRecords(String.valueOf(toIndex));

        curDisposable = digitalizerViewModel.reqAll(reqAllDigitalizer);
    }

    private void getNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        digitalizerBeanList.clear();
        maxNum = 0;
        digitalizerViewModel.reqNum();
    }

    private DigitizingAdapter.OnItemLongClickListener onItemLongClickListener = (view, position) ->
            QPopuWindow.getInstance(getActivity()).builder
                    .bindView(view, 0)
                    .setPopupItemList(new String[]{"新增站点"})
                    .setPointers(mPoint.x, mPoint.y)
                    .setOnPopuListItemClickListener((anchorView, anchorViewPosition, position1) -> {
                        switch (position1) {
                            case 0:
                                EventBus.getDefault().post(new EventBusMessage(AddSiteFragment.TAG));
                                break;
                        }
                    }).show();

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof DigitalizerBean) {
            DigitalizerBean digitalizerBean = (DigitalizerBean) item;
            EventBusMessage eventBusMessage = new EventBusMessage(DigitizingDetailFragment.TAG);
            eventBusMessage.setArg1(digitalizerBean);
            EventBus.getDefault().post(eventBusMessage);
        }
    };

    @Override
    public void bindViewModel() {
        digitalizerViewModel = ViewModelProviders.of(this).get(DigitalizerViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!digitalizerViewModel.getmObservableAll().hasObservers()) {
            digitalizerViewModel.getmObservableAll().observe(this, digitalizerBeans -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != digitalizerBeans) {
                        if (digitalizerBeans.size() > 0) {
                            digitalizerBeanList.addAll(digitalizerBeans);
                            LogUtil.d(TAG, "load complete = " + digitalizerBeans);
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

        if (!digitalizerViewModel.getmObservableNum().hasObservers()) {
            digitalizerViewModel.getmObservableNum().observe(this, integer -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
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

//        if (null == digitalizerBeanList || digitalizerBeanList.size() <= 0) {
            getNums();
//        }
    }
}
