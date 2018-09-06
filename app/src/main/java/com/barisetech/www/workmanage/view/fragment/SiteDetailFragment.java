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
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentSiteDetailBinding;

public class SiteDetailFragment extends BaseFragment {

    public static final String TAG = "SiteDetailFragment";
    FragmentSiteDetailBinding mBinding;

    private static final String SITE_ID = "siteBean";
    private SiteBean siteBean;

    public static SiteDetailFragment newInstance(SiteBean siteBean) {
        SiteDetailFragment fragment = new SiteDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SITE_ID, siteBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            siteBean = new SiteBean();
            siteBean = (SiteBean) getArguments().getSerializable(SITE_ID);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_site_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.site_detail));
        observableToolbar.set(toolbarInfo);
        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.tvId.setText(String.valueOf(siteBean.SiteId));
        mBinding.tvCompany.setText(siteBean.Company);
        mBinding.tvLongitude.setText(String.valueOf(siteBean.Longitude));
        mBinding.tvLatitude.setText(String.valueOf(siteBean.Latitude));
        mBinding.tvPrincipal.setText(siteBean.Manager);
        mBinding.tvPhoneNum.setText(siteBean.Telephone);
        mBinding.tvLineWhether.setText(siteBean.IsOnLine?"是":"否");
        mBinding.tvDoubleSnesor.setText(siteBean.IsDualSensor?"是":"否");
        mBinding.tvDoubleFilter.setText(siteBean.IsDirFilterEnabled?"是":"否");
        mBinding.tvLeakPlugin.setText(siteBean.LdPluginName);
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
