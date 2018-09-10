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
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipework.PipeWork;
import com.barisetech.www.workmanage.databinding.FragmentPipeCollectionDetailBinding;

import org.greenrobot.eventbus.EventBus;

public class PipeWorkDetailFragment extends BaseFragment {

    public static final String TAG = "PipeWorkDetailFragment";

    FragmentPipeCollectionDetailBinding mBinding;
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_collection_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe_collection_detail));
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
