package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
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
import com.barisetech.www.workmanage.databinding.FragmentPipeLindAreaModifyBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeblindAreaViewModel;

public class PipeLindAreaModifyFragment extends BaseFragment{

    public static final String TAG="PipeLindAreaModifyFragment";
    FragmentPipeLindAreaModifyBinding mBinding;
    private static final String LIND_AREA_ID = "pipeLindAreaInfo";
    private PipeLindAreaInfo pipeLindAreaInfo;
    private PipeblindAreaViewModel pipeblindAreaViewModel;



    public static PipeLindAreaModifyFragment newInstance(PipeLindAreaInfo pipeLindAreaInfo) {
        PipeLindAreaModifyFragment fragment = new PipeLindAreaModifyFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_lind_area_modify, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.modify_pipe_lind_area));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
    }

    @Override
    public void bindViewModel() {
        pipeblindAreaViewModel = ViewModelProviders.of(this).get(PipeblindAreaViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        pipeblindAreaViewModel.getMeObservableAddOrModifyLindArea().observe(this,s -> {
            if (null!=s){
                if (s.equals("成功修改")){
                    ToastUtil.showToast("成功修改");
                    getActivity().onBackPressed();
                }else if (s.equals("失败修改")){
                    ToastUtil.showToast("失败修改");
                }
            }
        });


        pipeblindAreaViewModel.getmObservableLindAreaDel().observe(this,flag->{
            if (null!=flag){
                if (flag){
                    ToastUtil.showToast("");
                    getActivity().onBackPressed();
                }else {
                    ToastUtil.showToast("");
                }
            }
        });

    }
}
