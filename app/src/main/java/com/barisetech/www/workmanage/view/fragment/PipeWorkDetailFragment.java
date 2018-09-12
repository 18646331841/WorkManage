package com.barisetech.www.workmanage.view.fragment;

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
import com.barisetech.www.workmanage.databinding.FragmentPipeWorkDetailBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

public class PipeWorkDetailFragment extends BaseFragment {

    public static final String TAG = "PipeWorkDetailFragment";

    FragmentPipeWorkDetailBinding mBinding;
    private static final String PW_ID = "pipeWork";
    private PipeWork curPipeWork;

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

    }

    @Override
    public void subscribeToModel() {

    }
}
