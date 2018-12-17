package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.pipelindarea.PipeLindAreaInfo;
import com.barisetech.www.workmanage.bean.pipework.PipeWork;
import com.barisetech.www.workmanage.databinding.FragmentPipeDetailBinding;
import com.barisetech.www.workmanage.utils.SystemUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;

import org.greenrobot.eventbus.EventBus;

public class PipeDetailFragment extends BaseFragment {

    public static final String TAG = "PipeDetailFragment";

    private static final String PIPE = "pipeInfo";
    private static final String PIPE_ID = "pipeId";
    private PipeInfo pipeInfo;
    private PipeViewModel pipeViewModel;
    FragmentPipeDetailBinding mBinding;

    public static PipeDetailFragment newInstance(PipeInfo pipeInfo) {
        PipeDetailFragment fragment = new PipeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PIPE, pipeInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            pipeInfo = (PipeInfo) getArguments().getSerializable(PIPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe_detail));
        if (SystemUtil.isAdmin()) {
            toolbarInfo.setTwoText(getString(R.string.pipe_detail_wave));
            toolbarInfo.setOneText(getString(R.string.pipe_detail_modify));
        } else {
            toolbarInfo.setOneText(getString(R.string.pipe_detail_wave));
            mBinding.buildPb.setVisibility(View.GONE);
            mBinding.buildPw.setVisibility(View.GONE);
        }
        observableToolbar.set(toolbarInfo);
        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.setPipe(pipeInfo);

        if (SystemUtil.isAdmin()) {

            mBinding.toolbar.tvOne.setOnClickListener(view -> {
                EventBusMessage eventBusMessage = new EventBusMessage(PipeModifyFragment.TAG);
                eventBusMessage.setArg1(pipeInfo);
                EventBus.getDefault().post(eventBusMessage);
            });

            mBinding.toolbar.tvTwo.setOnClickListener(view -> {
                EventBusMessage waveFormMessage = new EventBusMessage(WaveFormFragment.TAG);
                waveFormMessage.setArg1(pipeInfo.PipeId);
                EventBus.getDefault().post(waveFormMessage);
            });
        } else {
            mBinding.toolbar.tvOne.setOnClickListener(view -> {
                EventBusMessage waveFormMessage = new EventBusMessage(WaveFormFragment.TAG);
                waveFormMessage.setArg1(pipeInfo.PipeId);
                EventBus.getDefault().post(waveFormMessage);
            });
        }

        mBinding.toPw.setOnClickListener(view -> {
            EventBusMessage pipeMessage = new EventBusMessage(PipeWorkDetailFragment.TAG);
            PipeWork pipeWork = new PipeWork();
            pipeWork.PipeId = pipeInfo.PipeId;
            pipeWork.Name = BaseConstant.DATA_REQUEST_NAME;
            pipeMessage.setArg1(pipeWork);
            EventBus.getDefault().post(pipeMessage);
        });
        mBinding.toPb.setOnClickListener(view -> {
            EventBusMessage pipeMessage = new EventBusMessage(PipeLindAreaDetailFragment.TAG);
            PipeLindAreaInfo pipeLindAreaInfo = new PipeLindAreaInfo();
            pipeLindAreaInfo.setPipeId(pipeInfo.PipeId);
            pipeLindAreaInfo.setRemark(BaseConstant.DATA_REQUEST_NAME);
            pipeMessage.setArg1(pipeLindAreaInfo);
            EventBus.getDefault().post(pipeMessage);
        });
        mBinding.buildPw.setOnClickListener(view -> {
            EventBusMessage pipeMessage = new EventBusMessage(PipeWorkAddFragment.TAG);
            pipeMessage.setArg1(pipeInfo.PipeId);
            EventBus.getDefault().post(pipeMessage);
        });
        mBinding.buildPb.setOnClickListener(view -> {
            EventBusMessage pipeMessage = new EventBusMessage(PipeLindAreaAddFragment.TAG);
            pipeMessage.setArg1(pipeInfo.PipeId);
            EventBus.getDefault().post(pipeMessage);
        });
    }

    @Override
    public void bindViewModel() {
        pipeViewModel = ViewModelProviders.of(this).get(PipeViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeViewModel.getmObservableAllPipe().hasObservers()) {
            pipeViewModel.getmObservableAllPipe().observe(this, pipeInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (pipeInfos != null && pipeInfos.size() > 0) {
                        pipeInfo = pipeInfos.get(0);
                        mBinding.setPipe(pipeInfo);
                    } else {
                        ToastUtil.showToast("未查到管线数据");
                    }
                }
            });
        }

        if (pipeInfo.Name.equals(BaseConstant.DATA_REQUEST_NAME)) {
            getData();
        }
    }

    private void getData() {
        EventBusMessage eventBusMessage = new EventBusMessage(BaseConstant.PROGRESS_SHOW);
        EventBus.getDefault().post(eventBusMessage);

        ReqAllPipe reqAllPipe = new ReqAllPipe();
        reqAllPipe.setPipeId(String.valueOf(pipeInfo.PipeId));
        reqAllPipe.setStartIndex("0");
        reqAllPipe.setNumberOfRecords("0");

        pipeViewModel.reqAllPipe(reqAllPipe);
    }
}
