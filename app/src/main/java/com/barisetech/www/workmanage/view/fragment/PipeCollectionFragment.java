package com.barisetech.www.workmanage.view.fragment;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.PipeCollectionAdapter;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.bean.pipecollections.ReqDeletePc;
import com.barisetech.www.workmanage.databinding.FragmentPipeCollectionBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeCollectionsViewModel;
import com.barisetech.www.workmanage.widget.QPopuWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class PipeCollectionFragment extends BaseFragment {
    public static final String TAG = "PipeCollectionFragment";

    private PipeCollectionAdapter pipeCollectionAdapter;
    private List<PipeCollections> pipeCollectionsList;
    FragmentPipeCollectionBinding mBinding;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private PipeCollectionsViewModel pipeCollectionsViewModel;
    private Disposable curDisposable;
    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;

    private Point mPoint = new Point();

    public PipeCollectionFragment() {
        // Required empty public constructor
    }

    public static PipeCollectionFragment newInstance() {
        PipeCollectionFragment fragment = new PipeCollectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pipeCollectionsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_collection, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe_collection));
        toolbarInfo.setTwoText("新增");
        observableToolbar.set(toolbarInfo);

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        initRecyclerView();

        mBinding.toolbar.tvTwo.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(PipeCollectionAddFragment.TAG);
            EventBus.getDefault().post(eventBusMessage);
        });
    }

    private void initRecyclerView() {

        pipeCollectionAdapter = new PipeCollectionAdapter(pipeCollectionsList, getContext(), itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(pipeCollectionAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.pipeCollectionList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.pipeCollectionList.setAdapter(loadMoreWrapper);
        mBinding.pipeCollectionList.setItemAnimator(new DefaultItemAnimator());

        mBinding.pipeCollectionList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (pipeCollectionsList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - pipeCollectionsList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(pipeCollectionsList.size(), count);
                    } else {
                        updateRecyclerView(pipeCollectionsList.size(), PAGE_COUNT);
                    }
                }
            }
        });

        pipeCollectionAdapter.setOnItemLongClickListener((view, position) -> {
            PipeCollections pipeCollection = pipeCollectionsList.get(position);
            QPopuWindow.getInstance(getActivity()).builder
                    .bindView(view,0)
                    .setPopupItemList(new String[]{"编辑管线集合", "删除管线集合"})
                    .setPointers(mPoint.x,mPoint.y)
                    .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                        @Override
                        public void onPopuListItemClick(View anchorView, int anchorViewPosition, int p) {
                            switch (p){
                                case 0:
                                    EventBusMessage eventBusMessage = new EventBusMessage(PipeCollectionModifyFragment.TAG);
                                    eventBusMessage.setArg1(pipeCollection);
                                    EventBus.getDefault().post(eventBusMessage);
                                    break;
                                case 1:
                                    ReqDeletePc reqDeletePc = new ReqDeletePc();
                                    reqDeletePc.setPipeCollectId(pipeCollection.getId());
                                    pipeCollectionsViewModel.reqDeletePc(reqDeletePc);
                                    break;
                            }
                        }
                    }).show();
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void PointEventBus(Point point) {
        mPoint = point;
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

        ReqAllPc reqAllPc = new ReqAllPc();
        reqAllPc.setStartIndex(String.valueOf(formIndex));
        reqAllPc.setNumberOfRecords(String.valueOf(toIndex));
        reqAllPc.setPipeCollectionId("0");

        curDisposable = pipeCollectionsViewModel.reqAllPc(reqAllPc);
    }

    private void getNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
        pipeCollectionsList.clear();
        maxNum = 0;
        pipeCollectionsViewModel.reqPcNum();
    }

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof PipeCollections) {
            PipeCollections pipeCollections = (PipeCollections) item;
            EventBusMessage eventBusMessage = new EventBusMessage(PipeCollectionDetailFragment.TAG);
            eventBusMessage.setArg1(pipeCollections);
            EventBus.getDefault().post(eventBusMessage);
        }
    };

    @Override
    public void bindViewModel() {
        pipeCollectionsViewModel = ViewModelProviders.of(this).get(PipeCollectionsViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeCollectionsViewModel.getmObservableAllPC().hasObservers()) {
            pipeCollectionsViewModel.getmObservableAllPC().observe(this, pipeCollections -> {
                if (PipeCollectionFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

                    if (null != pipeCollections) {
                        if (pipeCollections.size() > 0) {
                            pipeCollectionsList.addAll(pipeCollections);
                            LogUtil.d(TAG, "load complete = " + pipeCollections);
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

        if (!pipeCollectionsViewModel.getmObservableNum().hasObservers()) {
            pipeCollectionsViewModel.getmObservableNum().observe(this, integer -> {
                if (PipeCollectionFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
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

//        if (null == pipeCollectionsList || pipeCollectionsList.size() <= 0) {
            getNums();
//        }

        if (!pipeCollectionsViewModel.getmObservableDelete().hasObservers()) {
            pipeCollectionsViewModel.getmObservableDelete().observe(this, aBoolean -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (aBoolean) {
                        ToastUtil.showToast("删除成功");
                        getActivity().onBackPressed();
                    } else {
                        ToastUtil.showToast("删除失败");
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
