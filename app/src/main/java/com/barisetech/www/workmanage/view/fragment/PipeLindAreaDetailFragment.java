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
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipelindarea.PipeLindAreaInfo;
import com.barisetech.www.workmanage.databinding.FragmentPipeLindAreaDetailBinding;

public class PipeLindAreaDetailFragment extends BaseFragment{

    public static final String TAG="PipeLindAreaDetailFragment";
    FragmentPipeLindAreaDetailBinding mBinding;
    private PipeLindAreaInfo pipeLindAreaInfo;
    private static final String LIND_AREA_ID="pipeLindAreaInfo";


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
            pipeLindAreaInfo = new PipeLindAreaInfo();
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
        toolbarInfo.setTwoText("修改");
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
