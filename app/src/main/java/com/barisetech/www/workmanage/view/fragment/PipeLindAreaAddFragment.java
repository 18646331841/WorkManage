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
import com.barisetech.www.workmanage.databinding.FragmentPipeLindAreaAddBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeblindAreaViewModel;

public class PipeLindAreaAddFragment extends BaseFragment{

    public static final String TAG="PipeLindAreaAddFragment";
    FragmentPipeLindAreaAddBinding mBinding;
    private PipeblindAreaViewModel pipeblindAreaViewModel;



    public static PipeLindAreaAddFragment newInstance(){
        PipeLindAreaAddFragment fragment = new PipeLindAreaAddFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_lind_area_add, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.add_pipe_lind_area));
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
                if (s.equals("")){
                    ToastUtil.showToast("");
                    getActivity().onBackPressed();
                }else if (s.equals("")){
                    ToastUtil.showToast("");
                }
            }
        });
    }
}
