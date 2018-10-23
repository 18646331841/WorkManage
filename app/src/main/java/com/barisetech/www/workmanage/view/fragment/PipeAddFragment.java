package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAddPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqPipeInfo;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.bean.plugin.PluginInfo;
import com.barisetech.www.workmanage.bean.plugin.ReqAllPlugin;
import com.barisetech.www.workmanage.bean.site.ReqSiteBean;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentPipeAddBinding;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.PipeCollectionsViewModel;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;
import com.barisetech.www.workmanage.viewmodel.PluginViewModel;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class PipeAddFragment extends BaseFragment {

    public static final String TAG = "PipeAddFragment";

    private PipeViewModel pipeViewModel;
    private CommonDialogFragment commonDialogFragment;
    private Disposable curDisposable;
    FragmentPipeAddBinding mBinding;
    private ReqPipeInfo reqPipeInfo = new ReqPipeInfo();
    private List<SiteBean> siteList = new ArrayList<>();
    private List<String> siteName = new ArrayList<>();
    private SiteViewModel siteViewModel;
    private PipeCollectionsViewModel pipeCollectionsViewModel;
    private List<PipeCollections> pipeCollectionsList = new ArrayList<>();
    private List<String> pcName = new ArrayList<>();

    private PluginViewModel pluginViewModel;
    private List<PluginInfo> pluginInfoList = new ArrayList<>();
    private List<String> pluginName = new ArrayList<>();

    public static PipeAddFragment newInstance() {
        PipeAddFragment fragment = new PipeAddFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pcName.add(0, "单独管线");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_add, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe_add));
        observableToolbar.set(toolbarInfo);
        initView();

        return mBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != commonDialogFragment) {
            commonDialogFragment.dismiss();
        }
    }

    private void closeDisposable() {
        if (curDisposable != null) {
            curDisposable.dispose();
        }
    }

    private void initView() {
        String comp = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_COMPANY, "");
        mBinding.pipeCompany.setText(comp);

        mBinding.spSelectPipePc.attachDataSource(pcName);

        mBinding.pipeAlgorithm.setOnItemClickListener(() -> {
            showDialog(getString(R.string.pipe_detail_is_algorithm), Boolean.valueOf(reqPipeInfo.Algorithm),
                    (radioGroup, i) -> {
                        closeDialog();
                        switch (i) {
                            case R.id.dialog_yes_rb:
                                reqPipeInfo.Algorithm = "true";
                                mBinding.pipeAlgorithm.setText("是");
                                break;
                            case R.id.dialog_no_rb:
                                reqPipeInfo.Algorithm = "false";
                                mBinding.pipeAlgorithm.setText("否");
                                break;
                        }
                    });
        });

        mBinding.pipeTest.setOnItemClickListener(() -> {
            showDialog(getString(R.string.pipe_detail_is_test), Boolean.valueOf(reqPipeInfo.IsTestMode), (radioGroup,
                                                                                                          i) -> {
                closeDialog();

                switch (i) {
                    case R.id.dialog_yes_rb:
                        reqPipeInfo.IsTestMode = "true";
                        mBinding.pipeTest.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        reqPipeInfo.IsTestMode = "false";
                        mBinding.pipeTest.setText("否");
                        break;
                }

            });
        });
        mBinding.pipeBall.setOnItemClickListener(() -> {
            showDialog(getString(R.string.pipe_detail_ball), Boolean.valueOf(reqPipeInfo.BallChokLocation),
                    (radioGroup, i) -> {
                        closeDialog();

                        switch (i) {
                            case R.id.dialog_yes_rb:
                                reqPipeInfo.BallChokLocation = "true";
                                mBinding.pipeBall.setText("是");
                                break;
                            case R.id.dialog_no_rb:
                                reqPipeInfo.BallChokLocation = "false";
                                mBinding.pipeBall.setText("否");
                                break;
                        }

                    });
        });
        mBinding.pipeLeak.setOnItemClickListener(() -> {
            showDialog(getString(R.string.pipe_detail_leak_age), Boolean.valueOf(reqPipeInfo.LeakageAssessment),
                    (radioGroup, i) -> {
                        closeDialog();

                        switch (i) {
                            case R.id.dialog_yes_rb:
                                reqPipeInfo.LeakageAssessment = "true";
                                mBinding.pipeLeak.setText("是");
                                break;
                            case R.id.dialog_no_rb:
                                reqPipeInfo.LeakageAssessment = "false";
                                mBinding.pipeLeak.setText("否");
                                break;
                        }

                    });
        });

        mBinding.modifyPipe.setOnClickListener(view -> {
            closeDisposable();

            String id = mBinding.pipeId.getText();
            String name = mBinding.pipeName.getText();
            String sortId = mBinding.pipeSortId.getText();
//            String pcId = mBinding.pipePc.getText();
            String length = mBinding.pipeLength.getText();
            String materail = mBinding.pipeMaterial.getText();
            String company = mBinding.pipeCompany.getText();
//            String startSite = mBinding.pipeStartSite.getText();
            String speed = mBinding.pipeSpeed.getText();
            String minTime = mBinding.pipeMinTime.getText();

            reqPipeInfo.PipeId = id;
            reqPipeInfo.Name = name;
            reqPipeInfo.SortID = sortId;
//            PipeCollections pc = new PipeCollections();
////            pc.setId(pcId);
            List<PipeCollections> pcList = new ArrayList<>();
            if (!mBinding.spSelectPipePc.getText().toString().equals("单独管线")) {
                for (PipeCollections pipeCollections : pipeCollectionsList) {
                    if (pipeCollections.getName().equals(mBinding.spSelectPipePc.getText().toString())) {
                        pcList.add(pipeCollections);
                    }
                }
            }
            reqPipeInfo.PipeCollectID = pcList;
            reqPipeInfo.Length = length;
            reqPipeInfo.PipeMaterial = materail;
            reqPipeInfo.Company = company;
//            reqPipeInfo.StartSiteId = startSite;
            reqPipeInfo.Speed = speed;
            reqPipeInfo.LeakCheckGap = minTime;
            List<ReqSiteBean> reqSiteBeans = new ArrayList<>();
            for (SiteBean bean : siteList) {
                if (bean.Name.equals(mBinding.spSelectStartSite.getText().toString())) {
                    reqPipeInfo.StartSiteId = String.valueOf(bean.SiteId);
                    ReqSiteBean reqSiteBean = new ReqSiteBean();
                    reqSiteBean.toSiteBean(bean);
                    reqSiteBeans.add(reqSiteBean);
                } else if (bean.Name.equals(mBinding.spSelectEndSite.getText().toString())) {
                    ReqSiteBean reqSiteBean = new ReqSiteBean();
                    reqSiteBean.toSiteBean(bean);
                    reqSiteBeans.add(reqSiteBean);
                }
            }
            reqPipeInfo.Sites = reqSiteBeans;

            for (PluginInfo pluginInfo : pluginInfoList) {
                if (pluginInfo.getName().equals(mBinding.spSelectPlugin.getText().toString())) {
                    reqPipeInfo.LlPluginId = pluginInfo.getId();
                    reqPipeInfo.LlPluginName = pluginInfo.getName();
                    break;
                }
            }

            ReqAddPipe reqAddPipe = new ReqAddPipe();
            reqAddPipe.setOperation("1");
            List<ReqPipeInfo> pipeInfos = new ArrayList<>();
            pipeInfos.add(reqPipeInfo);
            reqAddPipe.setPipeInfo(pipeInfos);

            EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
            curDisposable = pipeViewModel.reqAddOrModifyPipe(reqAddPipe);
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

    @Override
    public void bindViewModel() {
        pipeViewModel = ViewModelProviders.of(this).get(PipeViewModel.class);
        siteViewModel = ViewModelProviders.of(this).get(SiteViewModel.class);
        pluginViewModel = ViewModelProviders.of(this).get(PluginViewModel.class);
        pipeCollectionsViewModel = ViewModelProviders.of(this).get(PipeCollectionsViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeViewModel.getmObservableAdd().hasObservers()) {
            pipeViewModel.getmObservableAdd().observe(this, s -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != s) {
                        if (s.equals("成功添加")) {
                            ToastUtil.showToast("成功添加");
                            getActivity().onBackPressed();
                        } else if (s.equals("失败添加")) {
                            ToastUtil.showToast("失败添加");
                        }
                    } else {
                        ToastUtil.showToast("失败添加");
                    }
                }
            });
        }

        if (!siteViewModel.getmObservableSiteInfos().hasObservers()) {
            siteViewModel.getmObservableSiteInfos().observe(this, siteBeans -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != siteBeans) {
                        if (siteBeans.size() > 0) {
                            siteList.addAll(siteBeans);
                            for (SiteBean siteBean : siteList) {
                                siteName.add(siteBean.Name);
                            }
                            mBinding.spSelectStartSite.attachDataSource(siteName);
                            mBinding.spSelectEndSite.attachDataSource(siteName);
                        }
                    }
                }
            });
        }

        if (!siteViewModel.getmObservableSiteNum().hasObservers()) {
            siteViewModel.getmObservableSiteNum().observe(this, integer -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer) {
                        getSiteDatas(0, integer);
                    }
                }
            });
        }

        if (null == siteList || siteList.size() <= 0) {
            siteViewModel.reqSiteNum();
        }

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
            reqAllPlugin.setDataSource("pipe");
            pluginViewModel.reqAllPipe(reqAllPlugin);
        }

        if (!pipeCollectionsViewModel.getmObservableAllPC().hasObservers()) {
            pipeCollectionsViewModel.getmObservableAllPC().observe(this, pipeCollections -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

                    if (null != pipeCollections) {
                        if (pipeCollections.size() > 0) {
                            pipeCollectionsList.addAll(pipeCollections);
                            for (PipeCollections pipeCollections1 : pipeCollections) {
                                pcName.add(pipeCollections1.getName());
                            }
//                            mBinding.spSelectPipePc.attachDataSource(pcName);
                        }
                    }
                }
            });
        }

        if (!pipeCollectionsViewModel.getmObservableNum().hasObservers()) {
            pipeCollectionsViewModel.getmObservableNum().observe(this, integer -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer) {
                        getPcDatas(0, integer);
                    }
                }
            });
        }

        if (null == pipeCollectionsList || pipeCollectionsList.size() <= 0) {
            pipeCollectionsViewModel.reqPcNum();
        }
    }


    private void getSiteDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            return;
        }
        ReqSiteInfos reqSiteInfos = new ReqSiteInfos();
        reqSiteInfos.setSiteId("0");
        reqSiteInfos.setStartIndex(String.valueOf(formIndex));
        reqSiteInfos.setNumberOfRecords(String.valueOf(toIndex));

        siteViewModel.reqAllSite(reqSiteInfos);
    }

    private void getPcDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            return;
        }

        ReqAllPc reqAllPc = new ReqAllPc();
        reqAllPc.setStartIndex(String.valueOf(formIndex));
        reqAllPc.setNumberOfRecords(String.valueOf(toIndex));
        reqAllPc.setPipeCollectionId("0");

        pipeCollectionsViewModel.reqAllPc(reqAllPc);
    }
}
