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
import com.barisetech.www.workmanage.databinding.FragmentManageBinding;

import org.greenrobot.eventbus.EventBus;

public class ManageFragment extends BaseFragment implements View.OnClickListener{

    public static final String TAG="ManageFragment";

    FragmentManageBinding mBinding;


    public static ManageFragment newInstance() {
        ManageFragment fragment = new  ManageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage, container, false);
            setToolBarHeight(mBinding.toolbar.getRoot());
            mBinding.setFragment(this);
            ToolbarInfo toolbarInfo = new ToolbarInfo();
            toolbarInfo.setTitle(getString(R.string.title_manage));
            observableToolbar.set(toolbarInfo);
            mBinding.itemSite.setOnClickListener(this);
            mBinding.itemNum.setOnClickListener(this);
            mBinding.itemPipeline.setOnClickListener(this);
            mBinding.itemPipelineCollection.setOnClickListener(this);
            mBinding.itemStatistics.setOnClickListener(this);
            mBinding.itemPipelineBlindArea.setOnClickListener(this);
            mBinding.itemPipelineWorkStatus.setOnClickListener(this);
            mBinding.itemContacts.setOnClickListener(this);
            mBinding.itemDisk.setOnClickListener(this);
            return mBinding.getRoot();
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.item_site:
                EventBus.getDefault().post(new EventBusMessage(SiteFragment.TAG));
                break;
            case R.id.item_num:
                EventBus.getDefault().post(new EventBusMessage(DigitizingFragment.TAG));
                break;
            case R.id.item_pipeline:
                EventBus.getDefault().post(new EventBusMessage(PipeFragment.TAG));
                break;
            case R.id.item_pipeline_collection:
                EventBus.getDefault().post(new EventBusMessage(PipeCollectionFragment.TAG));
                break;
            case R.id.item_pipeline_work_status:
                EventBus.getDefault().post(new EventBusMessage(PipeWorkFragment.TAG));
                break;
            case R.id.item_pipeline_blind_area:
                break;
            case R.id.item_contacts:
                break;
            case R.id.item_disk:
                break;
            case R.id.item_statistics:
                break;

        }

    }
}
