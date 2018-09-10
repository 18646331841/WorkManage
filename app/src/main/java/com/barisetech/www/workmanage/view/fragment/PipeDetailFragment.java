package com.barisetech.www.workmanage.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.databinding.FragmentPipeDetailBinding;

import org.greenrobot.eventbus.EventBus;

public class PipeDetailFragment extends BaseFragment {

    public static final String TAG = "PipeDetailFragment";

    private static final String PIPE_ID = "pipeInfo";
    private PipeInfo pipeInfo;
    FragmentPipeDetailBinding mBinding;

    public static PipeDetailFragment newInstance(PipeInfo pipeInfo) {
        PipeDetailFragment fragment = new PipeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PIPE_ID, pipeInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            pipeInfo = (PipeInfo) getArguments().getSerializable(PIPE_ID);
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
        observableToolbar.set(toolbarInfo);
        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.modifyPipe.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(PipeCollectionModifyFragment.TAG);
//            eventBusMessage.setArg1(curPipeCollection);
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
