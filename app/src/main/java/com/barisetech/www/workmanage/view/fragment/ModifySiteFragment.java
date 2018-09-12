package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.site.ReqAddSite;
import com.barisetech.www.workmanage.bean.site.ReqDelSiteInfo;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentModifySiteBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import java.util.ArrayList;
import java.util.List;

public class ModifySiteFragment extends BaseFragment implements View.OnClickListener{



    public static final String TAG = "ModifySiteFragment";
    FragmentModifySiteBinding mBinding;
    private static final String SITE_ID = "siteBean";
    private SiteBean siteBean;
    private SiteViewModel siteViewModel;
    private CommonDialogFragment commonDialogFragment;


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
        mBinding.siteLineWhether.setOnItemClickListener(()->{
            showDialog(getString(R.string.line_whether), Boolean.valueOf(siteBean.IsOnLine), (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        siteBean.IsOnLine=true;
                        mBinding.siteLineWhether.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        siteBean.IsOnLine=false;
                        mBinding.siteLineWhether.setText("否");
                        break;
                }

            });
        });
        mBinding.siteDoubleSensor.setOnItemClickListener(()->{
            showDialog(getString(R.string.double_snesor), Boolean.valueOf(siteBean.IsDualSensor), (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        siteBean.IsDualSensor=true;
                        mBinding.siteDoubleSensor.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        siteBean.IsDualSensor=false;
                        mBinding.siteDoubleSensor.setText("否");
                        break;
                }

            });
        });
        mBinding.siteDoubleFilter.setOnItemClickListener(()->{
            showDialog(getString(R.string.double_filter), Boolean.valueOf(siteBean.IsDirFilterEnabled), (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        siteBean.IsDirFilterEnabled=true;
                        mBinding.siteDoubleFilter.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        siteBean.IsDirFilterEnabled=false;
                        mBinding.siteDoubleFilter.setText("否");
                        break;
                }

            });
        });
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
                   getActivity().onBackPressed();
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
                siteBean.IsOnLine = (mBinding.siteLineWhether.getText().equals("是")?true:false);
                siteBean.IsDualSensor = (mBinding.siteDoubleSensor.getText().equals("是")?true:false);
                siteBean.IsDirFilterEnabled = (mBinding.siteDoubleFilter.getText().equals("是")?true:false);
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


    private void showDialog(String title, boolean defaultV, RadioGroup.OnCheckedChangeListener
            onCheckedChangeListener) {
        int value;
        if (!defaultV) {
            value = DialogFragmentHelper.DIALOG_NO;
        } else {
            value = DialogFragmentHelper.DIALOG_YES;
        }
        commonDialogFragment = DialogFragmentHelper.showYesDialog(getFragmentManager(), title, value,
                onCheckedChangeListener, true);
    }

    private void closeDialog() {
        if (commonDialogFragment != null) {
            commonDialogFragment.dismiss();
        }
    }
}
