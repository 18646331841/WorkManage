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
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.PipeLindAreaAdapter;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipelindarea.PipeLindAreaInfo;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqAllPipelindArea;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqDeletePipeLindArea;
import com.barisetech.www.workmanage.databinding.FragmentPipeLindAreaBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeblindAreaViewModel;
import com.barisetech.www.workmanage.widget.QPopuWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class PipeLindAreaFragment extends BaseFragment {

    public static final String TAG = "PipeLindAreaFragment";

    private List<PipeLindAreaInfo> pipeLindAreaInfoList;
    private PipeLindAreaAdapter pipeLindAreaAdapter;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private PipeblindAreaViewModel pipeblindAreaViewModel;
    private Disposable disposable;
    FragmentPipeLindAreaBinding mBinding;
    private Point mPoint = new Point();

    private static final int PAGE_COUNT = 10;
    private int maxNum;



    public static PipeLindAreaFragment newInstance(){
        PipeLindAreaFragment fragment = new PipeLindAreaFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pipeLindAreaInfoList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_lind_area, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.tv_pipeline_blind_area));
        toolbarInfo.setTwoText("新增");
        observableToolbar.set(toolbarInfo);

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.toolbar.tvTwo.setOnClickListener(view -> {
            EventBus.getDefault().post(new EventBusMessage(PipeLindAreaAddFragment.TAG));
        });
        initRecyclerView();
    }

    private void initRecyclerView() {

        pipeLindAreaAdapter = new PipeLindAreaAdapter(pipeLindAreaInfoList,getContext());
        loadMoreWrapper = new BaseLoadMoreWrapper(pipeLindAreaAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(),50));
        mBinding.pipelindareaList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.pipelindareaList.setAdapter(loadMoreWrapper);
        mBinding.pipelindareaList.setItemAnimator(new DefaultItemAnimator());

        mBinding.pipelindareaList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (pipeLindAreaInfoList.size()==maxNum){
                    if (maxNum!=0){
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }else {
                    int count = maxNum - pipeLindAreaInfoList.size();
                    if (count < PAGE_COUNT){
                        updateRecyclerView(pipeLindAreaInfoList.size(), count);
                    }else {
                        updateRecyclerView(pipeLindAreaInfoList.size(), PAGE_COUNT);
                    }
                }

            }
        });
        pipeLindAreaAdapter.OnClick(item -> {
            if (item instanceof PipeLindAreaInfo){
                PipeLindAreaInfo pipeLindAreaInfo = (PipeLindAreaInfo) item;
                EventBusMessage eventBusMessage = new EventBusMessage(PipeLindAreaDetailFragment.TAG);
                eventBusMessage.setArg1(pipeLindAreaInfo);
                EventBus.getDefault().post(eventBusMessage);
            }
        });

        pipeLindAreaAdapter.setOnItemLongClickListener((view, position) -> {
            PipeLindAreaInfo pipeLindAreaInfo = pipeLindAreaInfoList.get(position);
            QPopuWindow.getInstance(getActivity()).builder
                    .bindView(view,0)
                    .setPopupItemList(new String[]{"编辑管线盲区", "删除管线盲区"})
                    .setPointers(mPoint.x,mPoint.y)
                    .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                        @Override
                        public void onPopuListItemClick(View anchorView, int anchorViewPosition, int p) {
                            switch (p){
                                case 0:
                                    EventBusMessage eventBusMessage = new EventBusMessage(PipeLindAreaModifyFragment.TAG);
                                    eventBusMessage.setArg1(pipeLindAreaInfo);
                                    EventBus.getDefault().post(eventBusMessage);
                                    break;
                                case 1:
                                    ReqDeletePipeLindArea info = new ReqDeletePipeLindArea();
                                    info.setPipeBlindAreaId(String.valueOf(pipeLindAreaInfo.getId()));

                                    EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
                                    pipeblindAreaViewModel.reqDeletePipeLindArea(info);
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

    private void updateRecyclerView(int fromIndex,int toIndex){
        getDatas(fromIndex,toIndex);

    }

    private void getDatas(int fromIndex,int toIndex){
        if (toIndex <= 0){
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
        ReqAllPipelindArea reqAllPipelindArea = new ReqAllPipelindArea();
        reqAllPipelindArea.setPipeId("0");
        reqAllPipelindArea.setIsGetAll("false");
        reqAllPipelindArea.setPipeIdQueryChecked("false");
        reqAllPipelindArea.setType("0");
        reqAllPipelindArea.setStartIndex(String.valueOf(fromIndex));
        reqAllPipelindArea.setNumberOfRecords(String.valueOf(toIndex));

        disposable = pipeblindAreaViewModel.reqAllPipeLindArea(reqAllPipelindArea);

    }

    private void getPipeLindAreaNums(){
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
        pipeLindAreaInfoList.clear();
        maxNum = 0;
        pipeblindAreaViewModel.reqPipeLindAreaNum();
    }

    @Override
    public void bindViewModel() {
        pipeblindAreaViewModel = ViewModelProviders.of(this).get(PipeblindAreaViewModel.class);

    }

    @Override
    public void subscribeToModel() {
        if (!pipeblindAreaViewModel.getmObservableAllPipeLindArea().hasObservers()){
            pipeblindAreaViewModel.getmObservableAllPipeLindArea().observe(this,pipeLindAreaInfos -> {
                if (PipeLindAreaFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)){
                    if (null != pipeLindAreaInfos) {
                        if (pipeLindAreaInfos.size() > 0) {
                            pipeLindAreaInfoList.addAll(pipeLindAreaInfos);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
                        if (null != pipeLindAreaInfoList && pipeLindAreaInfoList.size() > 0) {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    }
                }
            });
        }

        if (!pipeblindAreaViewModel.getmObservablePipeLindAreaNum().hasObservers()){
            pipeblindAreaViewModel.getmObservablePipeLindAreaNum().observe(this,integer -> {
                if (PipeLindAreaFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)){
                    if (null != integer && integer > 0) {
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
//        if (null== pipeLindAreaInfoList || pipeLindAreaInfoList.size() <=0){
            getPipeLindAreaNums();
//        }

        if (!pipeblindAreaViewModel.getmObservableLindAreaDel().hasObservers()) {
            pipeblindAreaViewModel.getmObservableLindAreaDel().observe(this, flag -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != flag) {
                        if (flag) {
                            ToastUtil.showToast("删除成功");
                            getActivity().onBackPressed();
                        } else {
                            ToastUtil.showToast("删除失败");
                        }
                    } else {
                        ToastUtil.showToast("删除失败");
                    }
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pipeblindAreaViewModel.getmObservableAllPipeLindArea().setValue(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
