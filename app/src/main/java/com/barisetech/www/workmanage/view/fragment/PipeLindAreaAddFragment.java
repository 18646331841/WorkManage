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
import com.barisetech.www.workmanage.bean.pipelindarea.ReqAddPipelindArea;
import com.barisetech.www.workmanage.databinding.FragmentPipeLindAreaAddBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeblindAreaViewModel;

public class PipeLindAreaAddFragment extends BaseFragment{

    public static final String TAG="PipeLindAreaAddFragment";
    FragmentPipeLindAreaAddBinding mBinding;
    private PipeblindAreaViewModel pipeblindAreaViewModel;
    private static final String PIPE_ID = "pipeId";
    private int pipeId = -1;

    public static PipeLindAreaAddFragment newInstance(){
        PipeLindAreaAddFragment fragment = new PipeLindAreaAddFragment();
        return fragment;
    }

    public static PipeLindAreaAddFragment newInstance(int pipeId){
        PipeLindAreaAddFragment fragment = new PipeLindAreaAddFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PIPE_ID, pipeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            pipeId = getArguments().getInt(PIPE_ID);
        }
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
        if (pipeId >= 0) {
            mBinding.lindPipeId.setText(String.valueOf(pipeId));
        }

        mBinding.addLindArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReqAddPipelindArea info = new ReqAddPipelindArea();
                info.setId(mBinding.lindId.getText().toString());
                info.setPipeId(mBinding.lindPipeId.getText().toString());
                info.setType(mBinding.lindType.getText().toString());
                info.setStartDistance(mBinding.lindStart.getText().toString());
                info.setEndDistance(mBinding.lindEnd.getText().toString());
                info.setIsEnabled(mBinding.lindIsenable.getText().toString().equals("是")?"true":"false");
                info.setRemark(mBinding.lindRemark.getText().toString());
                info.setIsAdd("true");
                pipeblindAreaViewModel.reqAddOrModifyPipeLindArea(info);
            }
        });
    }

    @Override
    public void bindViewModel() {
        pipeblindAreaViewModel = ViewModelProviders.of(this).get(PipeblindAreaViewModel.class);

    }

    @Override
    public void subscribeToModel() {
        pipeblindAreaViewModel.getMeObservableAddOrModifyLindArea().observe(this,s -> {
            if (null!=s){
                if (s.equals("成功添加")){
                    ToastUtil.showToast("成功添加");
                    getActivity().onBackPressed();
                }else if (s.equals("失败添加")){
                    ToastUtil.showToast("失败添加");
                }
            }
        });
    }
}
