package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAddPC;
import com.barisetech.www.workmanage.databinding.FragmentAddSiteBinding;
import com.barisetech.www.workmanage.databinding.FragmentPipeCollectionAddBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeCollectionsViewModel;

import java.util.ArrayList;
import java.util.List;

public class PipeCollectionAddFragment extends BaseFragment{

    public static final String TAG = "PipeCollectionAddFragment";
    FragmentPipeCollectionAddBinding mBinding;

    private PipeCollections pipeCollections;
    private PipeCollectionsViewModel pipeCollectionsViewModel;

    public static PipeCollectionAddFragment newInstance() {
        PipeCollectionAddFragment fragment = new PipeCollectionAddFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_collection_add, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe_collection_add));
        observableToolbar.set(toolbarInfo);

        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.addPc.setOnClickListener(view -> {
            String id = mBinding.pcId.getText();
            String name = mBinding.pcName.getText();
            String sortId = mBinding.pcSortId.getText();
            String manager = mBinding.pcManager.getText();
            String phone = mBinding.pcPhone.getText();
            String email = mBinding.pcEmail.getText();
            String remark = mBinding.pcRemark.getText();

            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(sortId) || TextUtils.isEmpty
                    (manager) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(remark)) {
                ToastUtil.showToast("信息不能有空");
            }

            ReqAddPC reqAddPC = new ReqAddPC();
            reqAddPC.setOperation("1");
            PipeCollections pipeCollections = new PipeCollections();
            pipeCollections.setId(id);
            pipeCollections.setName(name);
            pipeCollections.setSortID(sortId);
            pipeCollections.setManager(manager);
            pipeCollections.setTelephone(phone);
            pipeCollections.setEmail(email);
            pipeCollections.setRemark(remark);
            pipeCollections.setPipeCollects(new ArrayList<>());
            List<PipeCollections> pipeCollectionsList = new ArrayList<>();
            pipeCollectionsList.add(pipeCollections);

            reqAddPC.setPipeCollect(pipeCollectionsList);

            pipeCollectionsViewModel.reqAddOrModifyPc(reqAddPC);
        });
    }

    @Override
    public void bindViewModel() {
        pipeCollectionsViewModel = ViewModelProviders.of(this).get(PipeCollectionsViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeCollectionsViewModel.getmObservableAdd().hasObservers()) {
            pipeCollectionsViewModel.getmObservableAdd().observe(this, s -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != s) {
                        if (s.equals("成功添加")) {
                            ToastUtil.showToast("添加成功");
                        } else if (s.equals("失败添加")){
                            ToastUtil.showToast("添加失败");
                        }
                    } else {
                        ToastUtil.showToast("添加失败");
                    }
                }
            });
        }
    }
}
