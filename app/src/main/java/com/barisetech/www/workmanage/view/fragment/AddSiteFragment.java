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
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentAddSiteBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddSiteFragment extends BaseFragment{

    public static final String TAG = "AddSiteFragment";
    private FragmentAddSiteBinding mBinding;
    private SiteViewModel siteViewModel;
    private CommonDialogFragment commonDialogFragment;
    private  SiteBean siteBean = new SiteBean();



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
                siteBean.SiteId = Integer.valueOf(mBinding.siteId.getText().toString());
                siteBean.Company = mBinding.siteCompany.getText().toString();
                siteBean.Longitude = Double.valueOf(mBinding.siteLongitude.getText().toString());
                siteBean.Latitude = Double.valueOf(mBinding.siteLatitude.getText().toString());
                siteBean.Telephone  = mBinding.sitePhone.getText().toString();
                siteBean.Manager = mBinding.sitePrincipal.getText().toString();
                siteBean.IsOnLine = (mBinding.siteLineWhether.getText().equals("是")?true:false);
                siteBean.IsDualSensor = (mBinding.siteDoubleSensor.getText().equals("是")?true:false);
                siteBean.IsDirFilterEnabled = (mBinding.siteDoubleFilter.getText().equals("是")?true:false);
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
