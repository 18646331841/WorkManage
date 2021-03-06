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
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.digitalizer.DigitalizerBean;
import com.barisetech.www.workmanage.bean.plugin.PluginInfo;
import com.barisetech.www.workmanage.bean.plugin.ReqAllPlugin;
import com.barisetech.www.workmanage.bean.site.ReqAddSite;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentAddSiteBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.PluginViewModel;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddSiteFragment extends BaseFragment{

    public static final String TAG = "AddSiteFragment";
    private FragmentAddSiteBinding mBinding;
    private SiteViewModel siteViewModel;
    private CommonDialogFragment commonDialogFragment;
    private SiteBean siteBean = new SiteBean();

    private static final String SITE_ID = "siteId";
    private String siteId;

    private PluginViewModel pluginViewModel;
    private List<PluginInfo> pluginInfoList = new ArrayList<>();
    private List<String> pluginName = new ArrayList<>();

    public static AddSiteFragment newInstance(String siteId) {
        AddSiteFragment fragment = new AddSiteFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SITE_ID, siteId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            siteId = (String) getArguments().getString(SITE_ID);
        }
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
        if (!TextUtils.isEmpty(siteId)) {
            mBinding.siteId.setText(siteId);
        }

        String company = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_COMPANY, "");
        mBinding.siteCompany.setText(company);

        mBinding.addSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mBinding.siteId.getText())) {
                    ToastUtil.showToast("请填写ID");
                    return;
                }
                if (TextUtils.isEmpty(mBinding.siteLongitude.getText()) || TextUtils.isEmpty(mBinding.siteLatitude
                        .getText())) {
                    ToastUtil.showToast("请填写经纬度");
                    return;
                }
                siteBean.SiteId = Integer.valueOf(mBinding.siteId.getText().toString());
                siteBean.Company = mBinding.siteCompany.getText().toString();
                siteBean.Longitude = Double.valueOf(mBinding.siteLongitude.getText().toString());
                siteBean.Latitude = Double.valueOf(mBinding.siteLatitude.getText().toString());
                siteBean.Telephone  = mBinding.sitePhone.getText().toString();
                siteBean.Manager = mBinding.sitePrincipal.getText().toString();
                siteBean.IsOnLine = (mBinding.siteLineWhether.getText().equals("是"));
                siteBean.IsDualSensor = (mBinding.siteDoubleSensor.getText().equals("是"));
                siteBean.IsDirFilterEnabled = (mBinding.siteDoubleFilter.getText().equals("是"));
                siteBean.Name = mBinding.siteName.getText().toString();

                siteBean.EnergyRatio = Double.valueOf(mBinding.siteC.getText());
                siteBean.NegPeakThreshold = Integer.valueOf(mBinding.siteD.getText());
                siteBean.FreqStart = Double.valueOf(mBinding.siteE.getText());
                siteBean.FreqEnd = Double.valueOf(mBinding.siteF.getText());
                siteBean.FreqStep = Double.valueOf(mBinding.siteG.getText());
                siteBean.TimeNum = Integer.valueOf(mBinding.siteH.getText());
                siteBean.ShieldingRange = Double.valueOf(mBinding.siteI.getText());
                siteBean.TimeOffset = Integer.valueOf(mBinding.siteJ.getText());

                for (PluginInfo pluginInfo : pluginInfoList) {
                    if (pluginInfo.getName().equals(mBinding.spSelectPlugin.getText().toString())) {
                        siteBean.LdPluginId = pluginInfo.getId();
                        siteBean.LdPluginName = pluginInfo.getName();
                    }
                }
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

        mBinding.siteA.setOnItemClickListener(()->{
            showDialog(getString(R.string.site_a) + "    ", siteBean.IsDwtEnabled, (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        siteBean.IsDwtEnabled=true;
                        mBinding.siteA.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        siteBean.IsDwtEnabled=false;
                        mBinding.siteA.setText("否");
                        break;
                }

            });
        });
        mBinding.siteB.setOnItemClickListener(()->{
            showDialog(getString(R.string.site_b) + "    ", siteBean.IsAvgEnabled, (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        siteBean.IsAvgEnabled=true;
                        mBinding.siteB.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        siteBean.IsAvgEnabled=false;
                        mBinding.siteB.setText("否");
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

        if (!pluginViewModel.getmObservableAllPlugin().hasObservers()) {
            pluginViewModel.getmObservableAllPlugin().observe(this, pluginInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != pluginInfos) {
                        if (pluginInfos.size() > 0) {
                            pluginInfoList.addAll(pluginInfos);
                            for (int i = 0; i < pluginInfos.size(); i++) {
                                PluginInfo pluginInfo = pluginInfos.get(i);
                                pluginName.add(pluginInfo.getName());
                            }

                            mBinding.spSelectPlugin.attachDataSource(pluginName);
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
