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
import com.barisetech.www.workmanage.databinding.FragmentPipeAddBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;

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

    public static PipeAddFragment newInstance() {
        PipeAddFragment fragment = new PipeAddFragment();
        return fragment;
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
        
        mBinding.pipeAlgorithm.setOnItemClickListener(() -> {
            showDialog(getString(R.string.pipe_detail_is_algorithm), Boolean.valueOf(reqPipeInfo.Algorithm), (radioGroup, i) -> {
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
            showDialog(getString(R.string.pipe_detail_is_test), Boolean.valueOf(reqPipeInfo.IsTestMode), (radioGroup, i) -> {
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
            showDialog(getString(R.string.pipe_detail_ball), Boolean.valueOf(reqPipeInfo.BallChokLocation), (radioGroup, i) -> {
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
            showDialog(getString(R.string.pipe_detail_leak_age), Boolean.valueOf(reqPipeInfo.LeakageAssessment), (radioGroup, i) -> {
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
            String pcId = mBinding.pipePc.getText();
            String length = mBinding.pipeLength.getText();
            String materail = mBinding.pipeMaterial.getText();
            String company = mBinding.pipeCompany.getText();
            String startSite = mBinding.pipeStartSite.getText();
            String speed = mBinding.pipeSpeed.getText();
            String minTime = mBinding.pipeMinTime.getText();

            reqPipeInfo.PipeId = id;
            reqPipeInfo.Name = name;
            reqPipeInfo.SortID = sortId;
            PipeCollections pc = new PipeCollections();
            pc.setId(pcId);
            List<PipeCollections> pcList = new ArrayList<>();
            pcList.add(pc);
            reqPipeInfo.PipeCollectID = pcList;
            reqPipeInfo.Length = length;
            reqPipeInfo.PipeMaterial = materail;
            reqPipeInfo.Company = company;
            reqPipeInfo.StartSiteId = startSite;
            reqPipeInfo.Speed = speed;
            reqPipeInfo.LeakCheckGap = minTime;

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
    }
}
