package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipelindarea.PipeLindAreaInfo;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqAllPipelindArea;
import com.barisetech.www.workmanage.databinding.FragmentPipeLindAreaDetailBinding;
import com.barisetech.www.workmanage.utils.SystemUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeblindAreaViewModel;

import org.greenrobot.eventbus.EventBus;

public class PipeLindAreaDetailFragment extends BaseFragment{

    public static final String TAG="PipeLindAreaDetailFragment";
    FragmentPipeLindAreaDetailBinding mBinding;
    private PipeLindAreaInfo pipeLindAreaInfo;
    private static final String LIND_AREA_ID="pipeLindAreaInfo";
    private PipeblindAreaViewModel pipeblindAreaViewModel;

    public static PipeLindAreaDetailFragment newInstance(PipeLindAreaInfo pipeLindAreaInfo) {
        PipeLindAreaDetailFragment fragment = new PipeLindAreaDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LIND_AREA_ID, pipeLindAreaInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            pipeLindAreaInfo = (PipeLindAreaInfo) getArguments().getSerializable(LIND_AREA_ID);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_lind_area_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.pipe_lind_area_detail));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
//        if (!SystemUtil.isAdmin()) {
            mBinding.toLindModify.setVisibility(View.GONE);
//        }

        mBinding.setPb(pipeLindAreaInfo);

//        mBinding.toLindModify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBusMessage eventBusMessage = new EventBusMessage(PipeLindAreaModifyFragment.TAG);
//                eventBusMessage.setArg1(pipeLindAreaInfo);
//                EventBus.getDefault().post(eventBusMessage);
//            }
//        });
    }

    @Override
    public void bindViewModel() {
        pipeblindAreaViewModel = ViewModelProviders.of(this).get(PipeblindAreaViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeblindAreaViewModel.getmObservableAllPipeLindArea().hasObservers()) {
            pipeblindAreaViewModel.getmObservableAllPipeLindArea().observe(this, pipeLindAreaInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (pipeLindAreaInfos != null && pipeLindAreaInfos.size() > 0) {
                        pipeLindAreaInfo = pipeLindAreaInfos.get(0);
                        mBinding.setPb(pipeLindAreaInfo);
                    } else {
                        ToastUtil.showToast("未查到管线盲区数据");
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(pipeLindAreaInfo.getRemark()) && pipeLindAreaInfo.getRemark()
                .equals(BaseConstant.DATA_REQUEST_NAME)) {

            getData();
        }
    }

    private void getData() {
        EventBusMessage eventBusMessage = new EventBusMessage(BaseConstant.PROGRESS_SHOW);
        EventBus.getDefault().post(eventBusMessage);

        ReqAllPipelindArea reqAllPipelindArea = new ReqAllPipelindArea();
        reqAllPipelindArea.setPipeId(String.valueOf(pipeLindAreaInfo.getPipeId()));
        reqAllPipelindArea.setIsGetAll("false");
        reqAllPipelindArea.setPipeIdQueryChecked("true");
        reqAllPipelindArea.setType("0");
        reqAllPipelindArea.setStartIndex("0");
        reqAllPipelindArea.setNumberOfRecords("1");

        pipeblindAreaViewModel.reqAllPipeLindArea(reqAllPipelindArea);
    }
}
