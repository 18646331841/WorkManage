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
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.databinding.FragmentPipeModifyBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;

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

            curPipeInfo.PipeId = Integer.valueOf(id);
            curPipeInfo.Name = name;
            curPipeInfo.SortID = Integer.valueOf(sortId);
            PipeCollections pc = new PipeCollections();
            pc.setId(pcId);
            List<PipeCollections> pcList = new ArrayList<>();
            pcList.add(pc);
            curPipeInfo.PipeCollectID = pc;
            curPipeInfo.Length = Integer.valueOf(length);
            curPipeInfo.PipeMaterial = materail;
            curPipeInfo.Company = company;
            curPipeInfo.StartSiteId = Integer.valueOf(startSite);
            curPipeInfo.Speed = Integer.valueOf(speed);
            curPipeInfo.LeakCheckGap = Integer.valueOf(minTime);

            ReqAddPipe reqAddPipe = new ReqAddPipe();
            reqAddPipe.setOperation("0");
            List<PipeInfo> pipeInfos = new ArrayList<>();
            pipeInfos.add(curPipeInfo);
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
