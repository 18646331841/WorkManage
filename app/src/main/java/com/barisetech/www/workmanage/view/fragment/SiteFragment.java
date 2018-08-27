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
import com.barisetech.www.workmanage.databinding.FragmentSiteBinding;

import org.greenrobot.eventbus.EventBus;

public class SiteFragment extends BaseFragment {


    public static final String TAG = "SiteFragment";
    FragmentSiteBinding mBinding;





    public static SiteFragment newInstance() {
        SiteFragment fragment = new SiteFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_site, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.tv_site));
        toolbarInfo.setTwoText(getString(R.string.message_mission));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.toolbar.tvTwo.setOnClickListener(view -> {
            EventBus.getDefault().post(new EventBusMessage(AddSiteFragment.TAG));
        });
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
