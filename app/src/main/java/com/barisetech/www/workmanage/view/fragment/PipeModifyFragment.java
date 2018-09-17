package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.barisetech.www.workmanage.bean.site.ReqSiteBean;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentPipeModifyBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class PipeModifyFragment extends BaseFragment {

    public static final String TAG = "PipeModifyFragment";

    private static final String PIPE_ID = "pipeInfo";
    private PipeInfo curPipeInfo;
    FragmentPipeModifyBinding mBinding;
    private PipeViewModel pipeViewModel;
    private CommonDialogFragment commonDialogFragment;
    private Disposable curDisposable;
    public static ObservableField<PipeInfo> pipeInfoField;

    private SiteViewModel siteViewModel;
    private List<SiteBean> siteList = new ArrayList<>();
    private List<String> siteName = new ArrayList<>();

    public static PipeModifyFragment newInstance(PipeInfo pipeInfo) {
        PipeModifyFragment fragment = new PipeModifyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PIPE_ID, pipeInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            curPipeInfo = (PipeInfo) getArguments().getSerializable(PIPE_ID);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_modify, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe_modify));
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
        pipeInfoField = new ObservableField<>();
        pipeInfoField.set(curPipeInfo);
        mBinding.setMyFragment(this);

        mBinding.pipeAlgorithm.setOnItemClickListener(() -> {
            showDialog(getString(R.string.pipe_detail_is_algorithm), curPipeInfo.Algorithm, (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        curPipeInfo.Algorithm = true;
                        break;
                    case R.id.dialog_no_rb:
                        curPipeInfo.Algorithm = false;
                        break;
                }
                pipeInfoField.notifyChange();
            });
        });
        mBinding.pipeTest.setOnItemClickListener(() -> {
            showDialog(getString(R.string.pipe_detail_is_test), curPipeInfo.IsTestMode, (radioGroup, i) -> {
                closeDialog();

                switch (i) {
                    case R.id.dialog_yes_rb:
                        curPipeInfo.IsTestMode = true;

                        break;
                    case R.id.dialog_no_rb:
                        curPipeInfo.IsTestMode = false;
                        break;
                }
                pipeInfoField.notifyChange();

            });
        });
        mBinding.pipeBall.setOnItemClickListener(() -> {
            showDialog(getString(R.string.pipe_detail_ball), curPipeInfo.BallChokLocation, (radioGroup, i) -> {
                closeDialog();

                switch (i) {
                    case R.id.dialog_yes_rb:
                        curPipeInfo.BallChokLocation = true;
                        break;
                    case R.id.dialog_no_rb:
                        curPipeInfo.BallChokLocation = false;
                        break;
                }
                pipeInfoField.notifyChange();

            });
        });
        mBinding.pipeLeak.setOnItemClickListener(() -> {
            showDialog(getString(R.string.pipe_detail_leak_age), curPipeInfo.LeakageAssessment, (radioGroup, i) -> {
                closeDialog();

                switch (i) {
                    case R.id.dialog_yes_rb:
                        curPipeInfo.LeakageAssessment = true;
                        break;
                    case R.id.dialog_no_rb:
                        curPipeInfo.LeakageAssessment = false;
                        break;
                }
                pipeInfoField.notifyChange();

            });
        });

        mBinding.modifyPipe.setOnClickListener(view -> {
            closeDisposable();

            String id = mBinding.pipeId.getText();
            String name = mBinding.pipeName.getText();
            String sortId = mBinding.pipeSortId.getText();
            String pcId = mBinding.pipePc.getText();
            String length = mBinding.pipeLength.getText();
            String materail = mBinding.pipeMaterial.getText();
            String company = mBinding.pipeCompany.getText();
            String speed = mBinding.pipeSpeed.getText();
            String minTime = mBinding.pipeMinTime.getText();

            curPipeInfo.PipeId = Integer.valueOf(id);
            curPipeInfo.Name = name;
            curPipeInfo.SortID = Integer.valueOf(sortId);
            PipeCollections pc = new PipeCollections();
            pc.setId(pcId);
            curPipeInfo.PipeCollectID = pc;
            curPipeInfo.Length = Integer.valueOf(length);
            curPipeInfo.PipeMaterial = materail;
            curPipeInfo.Company = company;
            curPipeInfo.Speed = Integer.valueOf(speed);
            curPipeInfo.LeakCheckGap = Integer.valueOf(minTime);
            List<SiteBean> siteBeans = new ArrayList<>();
            for (SiteBean bean:siteList){
                if (bean.Name.equals(mBinding.spSelectStartSite.getText().toString())) {
                    curPipeInfo.StartSiteId = bean.SiteId;
                    siteBeans.add(bean);
                } else if(bean.Name.equals(mBinding.spSelectEndSite.getText().toString())){
                    siteBeans.add(bean);
                }
            }
            curPipeInfo.Sites = siteBeans;

            ReqPipeInfo reqPipeInfo = new ReqPipeInfo();
            reqPipeInfo.toPipe(curPipeInfo);

            ReqAddPipe reqAddPipe = new ReqAddPipe();
            reqAddPipe.setOperation("0");
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
    }

    @Override
    public void subscribeToModel() {
        if (!pipeViewModel.getmObservableAdd().hasObservers()) {
            pipeViewModel.getmObservableAdd().observe(this, s -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != s) {
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

        if (!siteViewModel.getmObservableSiteInfos().hasObservers()) {
            siteViewModel.getmObservableSiteInfos().observe(this, siteBeans -> {
                if (null != siteBeans) {
                    if (siteBeans.size() > 0) {
                        siteList.addAll(siteBeans);
                        int selectStartIndex = 0;
                        int selectEndIndex = 0;
                        for (int i = 0; i < siteBeans.size(); i++) {
                            SiteBean siteBean = siteBeans.get(i);
                            siteName.add(siteBean.Name);
                            if (curPipeInfo != null && curPipeInfo.StartSiteId == siteBean.SiteId) {
                                selectStartIndex = i;
                                //查找末站的索引位置
                                List<SiteBean> sites = curPipeInfo.Sites;
                                if (sites != null && sites.size() > 0) {
                                    for (int j = 0; j < sites.size(); j++) {
                                        SiteBean siteBean1 = sites.get(j);
                                        if (siteBean1.SiteId == siteBean.SiteId && siteBean1.SiteId != selectStartIndex) {
                                            selectEndIndex = i;
                                        }
                                    }
                                }
                            }
                        }
                        mBinding.spSelectStartSite.attachDataSource(siteName);
                        mBinding.spSelectStartSite.setSelectedIndex(selectStartIndex);
                        mBinding.spSelectEndSite.attachDataSource(siteName);
                        mBinding.spSelectEndSite.setSelectedIndex(selectEndIndex);
                    }
                }
            });
        }

        if (!siteViewModel.getmObservableSiteNum().hasObservers()) {
            siteViewModel.getmObservableSiteNum().observe(this, integer -> {
                if (null != integer) {
                    getDatas(0, integer);
                }
            });
        }

        if (null == siteList || siteList.size() <= 0) {
            siteViewModel.reqSiteNum();
        }
    }

    private void getDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            return;
        }
        ReqSiteInfos reqSiteInfos = new ReqSiteInfos();
        reqSiteInfos.setSiteId("0");
        reqSiteInfos.setStartIndex(String.valueOf(formIndex));
        reqSiteInfos.setNumberOfRecords(String.valueOf(toIndex));

        curDisposable = siteViewModel.reqAllSite(reqSiteInfos);
    }
}
