package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.map.pipe.PipeLine;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.databinding.FragmentPadMapListBinding;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeCollectionsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by LJH on 2018/9/17.
 */
public class PadMapListFragment extends BaseFragment {
    public static final String TAG = "PadMapListFragment";

    private static final String ALARM_ID = "alarmId";
    private String curPipeId;
    private List<PipeCollections> pipeCollectionsList = new ArrayList<>();

    FragmentPadMapListBinding mBinding;
    private PipeCollectionsViewModel pipeCollectionsViewModel;

    public static PadMapListFragment newInstance(String pipeId) {
        PadMapListFragment fragment = new PadMapListFragment();
        if (!TextUtils.isEmpty(pipeId)) {
            Bundle bundle = new Bundle();
            bundle.putString(ALARM_ID, pipeId);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curPipeId = getArguments().getString(ALARM_ID, "");
        }
        initMapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pad_map_list, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_map_pipe_list));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        EventBusMessage eventBusMessage = new EventBusMessage(NullFragment.TAG);
//        EventBus.getDefault().post(eventBusMessage);
//    }

    private void initView() {

    }

    private void initMapFragment() {
        EventBusMessage eventBusMessage = new EventBusMessage(PadMapFragment.TAG);
        eventBusMessage.setArg1(curPipeId);
        EventBus.getDefault().post(eventBusMessage);
    }

    /**
     * 初始化菜单
     *
     * @param oneList
     * @param sparseArray
     */
    private void initMenu(ArrayList<String> oneList, SparseArray<LinkedList<String>> sparseArray) {
        mBinding.padMapChildView.setActivity(getActivity());
        mBinding.padMapChildView.setDatas(oneList, sparseArray);
        mBinding.padMapChildView.setOnSelectListener(showText -> onRefresh(showText));
    }

    //视图被点击后刷新数据
    private void onRefresh(String showText) {
        LogUtil.d(TAG, "menu click = " + showText);
        EventBus.getDefault().post(showText);
    }

    @Override
    public void bindViewModel() {
        pipeCollectionsViewModel = ViewModelProviders.of(this).get(PipeCollectionsViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeCollectionsViewModel.getmObservableAllPC().hasObservers()) {
            pipeCollectionsViewModel.getmObservableAllPC().observe(this, pipeCollections -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

                    if (null != pipeCollections && pipeCollections.size() > 0) {
                        pipeCollectionsList.addAll(pipeCollections);

                        ArrayList<String> oneList = new ArrayList<>();
                        SparseArray<LinkedList<String>> sparseArray = new SparseArray<>();

                        for (int i = 0; i < pipeCollections.size(); i++) {
                            PipeCollections pipeCollections1 = pipeCollections.get(i);
                            oneList.add(i, pipeCollections1.getName());
                            List<PipeInfo> pipeInfos = pipeCollections1.getPipeCollects();
                            LinkedList<String> twoList = new LinkedList<>();
                            if (pipeInfos != null && pipeInfos.size() > 0) {
                                for (int j = 0; j < pipeInfos.size(); j++) {
                                    twoList.add(pipeInfos.get(j).Name);
                                }
                            }
                            sparseArray.put(i, twoList);
                        }

                        initMenu(oneList, sparseArray);
                    } else {
                        ToastUtil.showToast("获取管线集合失败");
                    }
                }
            });
        }

        if (!pipeCollectionsViewModel.getmObservableNum().hasObservers()) {
            pipeCollectionsViewModel.getmObservableNum().observe(this, integer -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer && integer > 0) {
                        ReqAllPc reqAllPc = new ReqAllPc();
                        reqAllPc.setStartIndex(String.valueOf(0));
                        reqAllPc.setNumberOfRecords(String.valueOf(integer));
                        reqAllPc.setPipeCollectionId("0");

                        pipeCollectionsViewModel.reqAllPc(reqAllPc);
                    } else {
                        ToastUtil.showToast("未找到管线集合");
                    }
                }
            });
        }

        if (null == pipeCollectionsList || pipeCollectionsList.size() <= 0) {
            pipeCollectionsViewModel.reqPcNum();
        }
    }
}
