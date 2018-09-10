package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
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
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAddPC;
import com.barisetech.www.workmanage.bean.pipecollections.ReqDeletePc;
import com.barisetech.www.workmanage.databinding.FragmentPipeCollectionModifyBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeCollectionsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class PipeCollectionModifyFragment extends BaseFragment{

    public static final String TAG = "PipeCollectionModifyFragment";

    FragmentPipeCollectionModifyBinding mBinding;
    private static final String PC_ID = "pcId";

    private PipeCollections pipeCollections;
    private PipeCollectionsViewModel pipeCollectionsViewModel;

    public static PipeCollectionAddFragment newInstance(PipeCollections pipeCollections) {
        PipeCollectionAddFragment fragment = new PipeCollectionAddFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PC_ID, pipeCollections);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            pipeCollections = (PipeCollections) getArguments().getSerializable(PC_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_collection_modify, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe_collection_modify));
        observableToolbar.set(toolbarInfo);

        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.pcId.setText(pipeCollections.getId());
        mBinding.pcName.setText(pipeCollections.getName());
        mBinding.pcSortId.setText(pipeCollections.getSortID());
        mBinding.pcManager.setText(pipeCollections.getManager());
        mBinding.pcPhone.setText(pipeCollections.getTelephone());
        mBinding.pcEmail.setText(pipeCollections.getEmail());
        mBinding.pcRemark.setText(pipeCollections.getRemark());

        mBinding.modifyPc.setOnClickListener(view -> {
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
            reqAddPC.setOperation("0");
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

            EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
            pipeCollectionsViewModel.reqAddOrModifyPc(reqAddPC);
        });

        mBinding.deletePc.setOnClickListener(view -> {
            ReqDeletePc reqDeletePc = new ReqDeletePc();
            reqDeletePc.setPipeCollectId(pipeCollections.getId());

            EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
            pipeCollectionsViewModel.reqDeletePc(reqDeletePc);
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

        if (!pipeCollectionsViewModel.getmObservableDelete().hasObservers()) {
            pipeCollectionsViewModel.getmObservableDelete().observe(this, aBoolean -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (aBoolean) {
                        ToastUtil.showToast("删除成功");
                    } else {
                        ToastUtil.showToast("删除失败");
                    }
                }
            });
        }
    }
}
