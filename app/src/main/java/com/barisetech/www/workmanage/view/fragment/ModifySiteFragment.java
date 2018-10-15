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
import android.widget.RadioGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.plugin.PluginInfo;
import com.barisetech.www.workmanage.bean.plugin.ReqAllPlugin;
import com.barisetech.www.workmanage.bean.site.ReqAddSite;
import com.barisetech.www.workmanage.bean.site.ReqDelSiteInfo;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentModifySiteBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.PluginViewModel;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import java.util.ArrayList;
import java.util.List;

public class ModifySiteFragment extends BaseFragment implements View.OnClickListener {


    public static final String TAG = "ModifySiteFragment";
    FragmentModifySiteBinding mBinding;
    private static final String SITE_ID = "siteBean";
    private SiteBean siteBean;
    private SiteViewModel siteViewModel;
    private CommonDialogFragment commonDialogFragment;

    private PluginViewModel pluginViewModel;
    private List<PluginInfo> pluginInfoList = new ArrayList<>();
    private List<String> pluginName = new ArrayList<>();

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
            siteBean = (SiteBean) getArguments().getSerializable(SITE_ID);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
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
        mBinding.siteLineWhether.setText(siteBean.IsOnLine ? "是" : "否");
        mBinding.siteDoubleSensor.setText(siteBean.IsDualSensor ? "是" : "否");
        mBinding.siteDoubleFilter.setText(siteBean.IsDirFilterEnabled ? "是" : "否");
        mBinding.siteLineWhether.setOnItemClickListener(() -> {
            showDialog(getString(R.string.line_whether), Boolean.valueOf(siteBean.IsOnLine), (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        siteBean.IsOnLine = true;
                        mBinding.siteLineWhether.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        siteBean.IsOnLine = false;
                        mBinding.siteLineWhether.setText("否");
                        break;
                }

            });
        });
        mBinding.siteDoubleSensor.setOnItemClickListener(() -> {
            showDialog(getString(R.string.double_snesor), Boolean.valueOf(siteBean.IsDualSensor), (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        siteBean.IsDualSensor = true;
                        mBinding.siteDoubleSensor.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        siteBean.IsDualSensor = false;
                        mBinding.siteDoubleSensor.setText("否");
                        break;
                }

            });
        });
        mBinding.siteDoubleFilter.setOnItemClickListener(() -> {
            showDialog(getString(R.string.double_filter), Boolean.valueOf(siteBean.IsDirFilterEnabled), (radioGroup,
                                                                                                         i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        siteBean.IsDirFilterEnabled = true;
                        mBinding.siteDoubleFilter.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        siteBean.IsDirFilterEnabled = false;
                        mBinding.siteDoubleFilter.setText("否");
                        break;
                }

            });
        });
    }

    @Override
    public void bindViewModel() {
        siteViewModel = ViewModelProviders.of(this).get(SiteViewModel.class);
        pluginViewModel = ViewModelProviders.of(this).get(PluginViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!siteViewModel.getMeObservableAddOrModifySite().hasObservers()) {
            siteViewModel.getMeObservableAddOrModifySite().observe(this, s -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (!TextUtils.isEmpty(s)) {
                        if (s.equals("成功修改")) {
                            ToastUtil.showToast("成功修改");
                            getActivity().onBackPressed();
                        } else if (s.equals("失败修改")) {
                            ToastUtil.showToast("失败修改");
                        }
                    } else {
                        ToastUtil.showToast("失败修改");
                    }
                }
            });
        }

        if (!siteViewModel.getmObservableSiteDel().hasObservers()) {
            siteViewModel.getmObservableSiteDel().observe(this, flag -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != flag) {
                        if (flag) {
                            ToastUtil.showToast("删除成功");
                            getActivity().onBackPressed();
                        } else {
                            ToastUtil.showToast("删除失败");
                        }
                    }
                }
            });
        }

        if (!pluginViewModel.getmObservableAllPlugin().hasObservers()) {
            pluginViewModel.getmObservableAllPlugin().observe(this, pluginInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != pluginInfos) {
                        if (pluginInfos.size() > 0) {
                            pluginInfoList.addAll(pluginInfos);
                            int selectIndex = 0;
                            for (int i = 0; i < pluginInfos.size(); i++) {
                                PluginInfo pluginInfo = pluginInfos.get(i);
                                pluginName.add(pluginInfo.getName());
                                if (siteBean != null && !TextUtils.isEmpty(siteBean.LdPluginId)) {
                                    if (siteBean.LdPluginId.equals(pluginInfo.getId())) {
                                        selectIndex = i;
                                    }
                                }
                            }

                            mBinding.spSelectPlugin.attachDataSource(pluginName);
                            mBinding.spSelectPlugin.setSelectedIndex(selectIndex);
                        }
                    }
                }
            });
        }

        if (null == pluginInfoList || pluginInfoList.size() <= 0) {
            ReqAllPlugin reqAllPlugin = new ReqAllPlugin();
            reqAllPlugin.setDataSource("site");
            pluginViewModel.reqAllPipe(reqAllPlugin);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sumbit_modify:
                siteBean.SiteId = Integer.valueOf(mBinding.siteId.getText().toString());
                siteBean.Company = mBinding.siteCompany.getText().toString();
                siteBean.Longitude = Double.valueOf(mBinding.siteLongitude.getText().toString());
                siteBean.Latitude = Double.valueOf(mBinding.siteLatitude.getText().toString());
                siteBean.Telephone = mBinding.sitePhone.getText().toString();
                siteBean.Manager = mBinding.sitePrincipal.getText().toString();
                siteBean.IsOnLine = (mBinding.siteLineWhether.getText().equals("是") ? true : false);
                siteBean.IsDualSensor = (mBinding.siteDoubleSensor.getText().equals("是") ? true : false);
                siteBean.IsDirFilterEnabled = (mBinding.siteDoubleFilter.getText().equals("是") ? true : false);
                for (PluginInfo pluginInfo : pluginInfoList) {
                    if (pluginInfo.getName().equals(mBinding.spSelectPlugin.getText().toString())) {
                        siteBean.LdPluginId = pluginInfo.getId();
                        siteBean.LdPluginName = pluginInfo.getName();
                    }
                }
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
