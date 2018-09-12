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
import com.barisetech.www.workmanage.bean.pipework.PipeWork;
import com.barisetech.www.workmanage.bean.pipework.ReqAddPW;
import com.barisetech.www.workmanage.databinding.FragmentPipeWorkAddBinding;
import com.barisetech.www.workmanage.databinding.FragmentPipeWorkModifyBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.PipeWorkViewModel;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

public class PipeWorkAddFragment extends BaseFragment {

    public static final String TAG = "PipeWorkAddFragment";

    FragmentPipeWorkAddBinding mBinding;
    private PipeWork curPipeWork;
    private PipeWorkViewModel pipeWorkViewModel;
    private CommonDialogFragment commonDialogFragment;
    private Disposable curDisposable;

    public static PipeWorkAddFragment newInstance() {
        PipeWorkAddFragment fragment = new PipeWorkAddFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_work_add, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_pipe_work_add));
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
        curPipeWork = new PipeWork();
        mBinding.setPw(curPipeWork);

        mBinding.addPw.setOnClickListener(view -> {
            closeDisposable();

            String id = mBinding.pwId.getText();
            String name = mBinding.pwName.getText();
            String record = mBinding.pwMarkTime.getText();
            String user = mBinding.pwUser.getText();
            String pipeId = mBinding.pwPipeId.getText();
            String pipeName = mBinding.pwPipeName.getText();
            String diameter = mBinding.pwPipeDiameter.getText();
            String thickness = mBinding.pwPipeThickness.getText();
            String material = mBinding.pwPipeMaterial.getText();
            String length = mBinding.pwPipeLength.getText();
            String speed = mBinding.pwSpeed.getText();
            String branch = mBinding.pwBranch.getText();
            String medium = mBinding.pwMedium.getText();
            String temperature = mBinding.pwMediumTemperature.getText();
            String originId = mBinding.pwOriginId.getText();
            String originName = mBinding.pwOriginName.getText();
            String oSoftVersion = mBinding.pwOriginSoftVesion.getText();
            String oHardVersion = mBinding.pwOriginHardVesion.getText();
            String oSAmplitude = mBinding.pwOriginSensorAmplitude.getText();
            String oSLevel = mBinding.pwOriginSensorLevel.getText();
            String oSType = mBinding.pwOriginSensorType.getText();
            String oSChannel = mBinding.pwOriginSensorChannel.getText();
            String oSHz = mBinding.pwOriginSensorHz.getText();
            String oIAmplitude = mBinding.pwOriginIsolatorAmplitude.getText();
            String oILevel = mBinding.pwOriginIsolatorLevel.getText();
            String oIType = mBinding.pwOriginIsolatorType.getText();
            String oIChannel = mBinding.pwOriginIsolatorChannel.getText();
            String oIHz = mBinding.pwOriginIsolatorHz.getText();
            String oIPressure = mBinding.pwOriginIsolatorPressure.getText();
            String oMomentFlow = mBinding.pwOriginMomentFlow.getText();
            String oCumulativeFlow = mBinding.pwOriginCumulativeFlow.getText();
            String slaveId = mBinding.pwSlaveId.getText();
            String slaveName = mBinding.pwSlaveName.getText();
            String sSoftVersion = mBinding.pwSlaveSoftVesion.getText();
            String sHardVersion = mBinding.pwSlaveHardVesion.getText();
            String sSAmplitude = mBinding.pwSlaveSensorAmplitude.getText();
            String sSLevel = mBinding.pwSlaveSensorLevel.getText();
            String sSType = mBinding.pwSlaveSensorType.getText();
            String sSChannel = mBinding.pwSlaveSensorChannel.getText();
            String sSHz = mBinding.pwSlaveSensorHz.getText();
            String sIAmplitude = mBinding.pwSlaveIsolatorAmplitude.getText();
            String sILevel = mBinding.pwSlaveIsolatorLevel.getText();
            String sIType = mBinding.pwSlaveIsolatorType.getText();
            String sIChannel = mBinding.pwSlaveIsolatorChannel.getText();
            String sIHz = mBinding.pwSlaveIsolatorHz.getText();
            String sIPressure = mBinding.pwSlaveIsolatorPressure.getText();
            String sMomentFlow = mBinding.pwSlaveMomentFlow.getText();
            String sCumulativeFlow = mBinding.pwSlaveCumulativeFlow.getText();
            String remark = mBinding.pwRemark.getText();

            ReqAddPW reqAddPw = new ReqAddPW();
            reqAddPw.isAdd = "true";
            reqAddPw.id = id;
            reqAddPw.Name = name;
            reqAddPw.RecordDate = record;
            reqAddPw.User = user;
            reqAddPw.PipeId = pipeId;
            reqAddPw.PipeName = pipeName;
            reqAddPw.PipeThickness = thickness;
            reqAddPw.PipeDiameter = diameter;
            reqAddPw.PipeMaterial = material;
            reqAddPw.PipeLength = length;
            reqAddPw.Speed = speed;
            reqAddPw.BranchConditions = branch;
            reqAddPw.PipeMedium = medium;
            reqAddPw.MediumTemperature = temperature;
            reqAddPw.OriginId = originId;
            reqAddPw.OriginName = originName;
            reqAddPw.OriginSoftVersion = oSoftVersion;
            reqAddPw.OriginHardVersion = oHardVersion;
            reqAddPw.OriginSensorNoiseAmplitude = oSAmplitude;
            reqAddPw.OriginSensorNoiseLevel = oSLevel;
            reqAddPw.OriginSensorType = oSType;
            reqAddPw.OriginAdcChannelPrimary = oSChannel;
            reqAddPw.OriginExtFirPrimary = oSHz;
            reqAddPw.OriginIsolatorNoiseAmplitude = oIAmplitude;
            reqAddPw.OriginIsolatorNoiseLevel = oILevel;
            reqAddPw.OriginIsolatorType = oIType;
            reqAddPw.OriginAdcChannelIsolator = oIChannel;
            reqAddPw.OriginExtFirIsolator = oIHz;
            reqAddPw.OriginPressure = oIPressure;
            reqAddPw.OriginMomentFlow = oMomentFlow;
            reqAddPw.OriginCumulativeFlow = oCumulativeFlow;
            reqAddPw.SlaveId = slaveId;
            reqAddPw.SlaveName = slaveName;
            reqAddPw.SlaveSoftVersion = sSoftVersion;
            reqAddPw.SlaveHardVersion = sHardVersion;
            reqAddPw.SlaveSensorNoiseAmplitude = sSAmplitude;
            reqAddPw.SlaveSensorNoiseLevel = sSLevel;
            reqAddPw.SlaveSensorType = sSType;
            reqAddPw.SlaveAdcChannelPrimary = sSChannel;
            reqAddPw.SlaveExtFirPrimary = sSHz;
            reqAddPw.SlaveIsolatorNoiseAmplitude = sIAmplitude;
            reqAddPw.SlaveIsolatorNoiseLevel = sILevel;
            reqAddPw.SlaveIsolatorType = sIType;
            reqAddPw.SlaveAdcChannelIsolator = sIChannel;
            reqAddPw.SlaveExtFirIsolator = sIHz;
            reqAddPw.SlavePressure = sIPressure;
            reqAddPw.SlaveMomentFlow = sMomentFlow;
            reqAddPw.SlaveCumulativeFlow = sCumulativeFlow;
            reqAddPw.Remark = remark;

            EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
            curDisposable = pipeWorkViewModel.reqAddOrModifyPw(reqAddPw);
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
        pipeWorkViewModel = ViewModelProviders.of(this).get(PipeWorkViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeWorkViewModel.getmObservableAdd().hasObservers()) {
            pipeWorkViewModel.getmObservableAdd().observe(this, s -> {
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
    }
}
