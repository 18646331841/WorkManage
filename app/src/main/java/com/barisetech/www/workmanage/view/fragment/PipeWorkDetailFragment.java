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
import com.barisetech.www.workmanage.bean.pipework.PipeWork;
import com.barisetech.www.workmanage.bean.pipework.ReqAllPW;
import com.barisetech.www.workmanage.databinding.FragmentPipeWorkDetailBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeWorkViewModel;

import org.greenrobot.eventbus.EventBus;

public class PipeWorkDetailFragment extends BaseFragment {

    public static final String TAG = "PipeWorkDetailFragment";

    FragmentPipeWorkDetailBinding mBinding;
    private static final String PW_ID = "pipeWork";
    private PipeWork curPipeWork;
    private PipeWorkViewModel pipeWorkViewModel;

    public static PipeWorkDetailFragment newInstance(PipeWork pipeWork) {
        PipeWorkDetailFragment fragment = new PipeWorkDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PW_ID, pipeWork);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            curPipeWork = (PipeWork) getArguments().getSerializable(PW_ID);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_work_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe_work_detail));
        toolbarInfo.setOneText("修改");
        observableToolbar.set(toolbarInfo);
        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.setPw(curPipeWork);

        String role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
        if (role.equals(BaseConstant.ROLE_SUPER_ADMINS) || role.equals(BaseConstant.ROLE_ADMINS)) {
            mBinding.originCard.setVisibility(View.VISIBLE);
            mBinding.slaveCard.setVisibility(View.VISIBLE);
        } else {
            mBinding.originCard.setVisibility(View.GONE);
            mBinding.slaveCard.setVisibility(View.GONE);
        }
        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(PipeWorkModifyFragment.TAG);
            eventBusMessage.setArg1(curPipeWork);
            EventBus.getDefault().post(eventBusMessage);
        });
    }

    @Override
    public void bindViewModel() {
        pipeWorkViewModel = ViewModelProviders.of(this).get(PipeWorkViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeWorkViewModel.getmObservableAllPW().hasObservers()) {
            pipeWorkViewModel.getmObservableAllPW().observe(this, pipeWorks -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (pipeWorks != null && pipeWorks.size() > 0) {
                        curPipeWork = pipeWorks.get(0);
                        mBinding.setPw(curPipeWork);
                    } else {
                        ToastUtil.showToast("未查到管线工况数据");
                    }
                }
            });
        }

        if (curPipeWork.Name.equals(BaseConstant.DATA_REQUEST_NAME)) {
            getData();
        }
    }

    private void getData() {
        EventBusMessage eventBusMessage = new EventBusMessage(BaseConstant.PROGRESS_SHOW);
        EventBus.getDefault().post(eventBusMessage);

        ReqAllPW reqAllPW = new ReqAllPW();
        reqAllPW.setIsGetAll("false");
        reqAllPW.setPipeIdQueryChecked("true");
        reqAllPW.setPipeId(String.valueOf(curPipeWork.PipeId));
        reqAllPW.setTimeQueryChecked("false");
        reqAllPW.setType("0");
        reqAllPW.setMStartTime("1970-1-1 00:00:00");
        reqAllPW.setMEndTime(TimeUtil.ms2Date(System.currentTimeMillis()));
        reqAllPW.setStartIndex("0");
        reqAllPW.setNumberOfRecords("1");

        pipeWorkViewModel.reqAllPw(reqAllPW);
    }
}
