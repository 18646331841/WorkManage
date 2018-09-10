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
import com.barisetech.www.workmanage.bean.site.ReqAddSite;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentAddSiteBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddSiteFragment extends BaseFragment{

    public static final String TAG = "AddSiteFragment";
    private FragmentAddSiteBinding mBinding;
    private SiteViewModel siteViewModel;



    public static AddSiteFragment newInstance() {
        AddSiteFragment fragment = new AddSiteFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_site, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.add_site));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.addSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteBean siteBean = new SiteBean();
                siteBean.SiteId = Integer.valueOf(mBinding.siteId.getText().toString());
                siteBean.Company = mBinding.siteCompany.getText().toString();
                siteBean.Longitude = Double.valueOf(mBinding.siteLongitude.getText().toString());
                siteBean.Latitude = Double.valueOf(mBinding.siteLatitude.getText().toString());
                siteBean.Telephone  = mBinding.sitePhone.getText().toString();
                siteBean.Manager = mBinding.sitePrincipal.getText().toString();
                siteBean.IsOnLine = (mBinding.siteLineWhether.toString().equals("是")?true:false);
                siteBean.IsDualSensor = (mBinding.siteDoubleSensor.toString().equals("是")?true:false);
                siteBean.IsDirFilterEnabled = (mBinding.siteDoubleFilter.toString().equals("是")?true:false);
                siteBean.LdPluginName = mBinding.siteLeakPlugin.getText().toString();
                siteBean.Name = mBinding.siteName.getText().toString();
                //TODO：设置参数
                siteBean.SensorCode1 = null;
                siteBean.SensorCode2 = null;
                siteBean.LdPluginId="c0f607c5-fe14-4655-a125-c8fcb5dc1f4c";
                ReqAddSite reqAddSite = new ReqAddSite();
                reqAddSite.setOperation("1");
                List<SiteBean> list = new ArrayList<>();
                list.add(siteBean);
                reqAddSite.setSiteInfo(list);
                siteViewModel.reqAddOrModifySite(reqAddSite);
            }
        });

    }

    @Override
    public void bindViewModel() {
        siteViewModel = ViewModelProviders.of(this).get(SiteViewModel.class);

    }

    @Override
    public void subscribeToModel() {
        siteViewModel.getMeObservableAddOrModifySite().observe(this,s -> {
            if (null != s){
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
