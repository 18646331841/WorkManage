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
import com.barisetech.www.workmanage.bean.site.ReqDelSiteInfo;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentModifySiteBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import java.util.ArrayList;
import java.util.List;

public class ModifySiteFragment extends BaseFragment implements View.OnClickListener{



    public static final String TAG = "ModifySiteFragment";
    FragmentModifySiteBinding mBinding;
    private static final String SITE_ID = "siteBean";
    private SiteBean siteBean;
    private SiteViewModel siteViewModel;


    public static ModifySiteFragment newInstance(SiteBean siteBean) {
        ModifySiteFragment fragment = new ModifySiteFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_site, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.modify_site));
        observableToolbar.set(toolbarInfo);
        mBinding.sumbitModify.setOnClickListener(this);
        mBinding.deleteSite.setOnClickListener(this);
        iniView();
        return mBinding.getRoot();
    }

    private void iniView() {
        mBinding.siteId.setText(String.valueOf(siteBean.SiteId));
        mBinding.siteCompany.setText(siteBean.Company);
        mBinding.siteLongitude.setText(String.valueOf(siteBean.Longitude));
        mBinding.siteLatitude.setText(String.valueOf(siteBean.Latitude));
        mBinding.sitePhone.setText(siteBean.Telephone);
        mBinding.sitePrincipal.setText(siteBean.Manager);
        mBinding.siteLineWhether.setText(siteBean.IsOnLine?"是":"否");
        mBinding.siteDoubleSensor.setText(siteBean.IsDualSensor?"是":"否");
        mBinding.siteDoubleFilter.setText(siteBean.IsDirFilterEnabled?"是":"否");
        mBinding.siteLeakPlugin.setText(siteBean.LdPluginName);
    }

    @Override
    public void bindViewModel() {
        siteViewModel = ViewModelProviders.of(this).get(SiteViewModel.class);
    }

    @Override
    public void subscribeToModel() {
       siteViewModel.getMeObservableAddOrModifySite().observe(this,s -> {
           if (null != s) {
               if (s.equals("成功修改")) {
                   ToastUtil.showToast("成功修改");
                   getActivity().onBackPressed();
               } else if (s.equals("失败修改")){
                   ToastUtil.showToast("失败修改");
               }
           }
       });

       siteViewModel.getmObservableSiteDel().observe(this,flag->{
           if (null!=flag){
               if (flag){
                   ToastUtil.showToast("删除成功");
               }else {
                   ToastUtil.showToast("删除失败");
               }
           }
       });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sumbit_modify:
                siteBean.SiteId = Integer.valueOf(mBinding.siteId.getText().toString());
                siteBean.Company = mBinding.siteCompany.getText().toString();
                siteBean.Longitude = Double.valueOf(mBinding.siteLongitude.getText().toString());
                siteBean.Latitude = Double.valueOf(mBinding.siteLatitude.getText().toString());
                siteBean.Telephone  = mBinding.sitePhone.getText().toString();
                siteBean.Manager = mBinding.sitePrincipal.getText().toString();
                siteBean.IsOnLine = (mBinding.siteLineWhether.toString().equals("是")?true:false);
                siteBean.IsDualSensor = (mBinding.siteDoubleSensor.toString().equals("是")?true:false);
                siteBean.IsDirFilterEnabled = (mBinding.siteDoubleFilter.toString().equals("是")?true:false);
                siteBean.LdPluginName = mBinding.siteLeakPlugin.toString();
                ReqAddSite reqAddSite = new ReqAddSite();
                reqAddSite.setOperation("0");
                List<SiteBean> list = new ArrayList<>();
                list.add(siteBean);
                reqAddSite.setSiteInfo(list);
                siteViewModel.reqAddOrModifySite(reqAddSite);
                break;
            case R.id.delete_site:
                ReqDelSiteInfo reqDelSiteInfo = new ReqDelSiteInfo();
                reqDelSiteInfo.setSiteId(String.valueOf(siteBean.SiteId));
                siteViewModel.reqDelSite(reqDelSiteInfo);
                break;
        }
    }
}
