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
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentPipeCollectionDetailBinding;
import com.barisetech.www.workmanage.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;

public class PipeCollectionDetailFragment extends BaseFragment {

    public static final String TAG = "PipeCollectionDetailFragment";

    FragmentPipeCollectionDetailBinding mBinding;
    private static final String PC_ID = "pipeCollection";
    private PipeCollections curPipeCollection;

    public static PipeCollectionDetailFragment newInstance(PipeCollections pipeCollections) {
        PipeCollectionDetailFragment fragment = new PipeCollectionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PC_ID, pipeCollections);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            curPipeCollection = (PipeCollections) getArguments().getSerializable(PC_ID);
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
//        if (!SystemUtil.isAdmin()) {
            mBinding.modifyPipe.setVisibility(View.GONE);
//        }

        mBinding.setPc(curPipeCollection);

        mBinding.modifyPipe.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(PipeCollectionModifyFragment.TAG);
            eventBusMessage.setArg1(curPipeCollection);
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
