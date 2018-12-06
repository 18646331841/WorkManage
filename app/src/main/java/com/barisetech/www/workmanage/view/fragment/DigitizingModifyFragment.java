package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
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
import com.barisetech.www.workmanage.bean.digitalizer.DigitalizerBean;
import com.barisetech.www.workmanage.bean.digitalizer.ReqModifyDigitalizer;
import com.barisetech.www.workmanage.databinding.FragmentDigitizingModifyBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.DigitalizerViewModel;

import io.reactivex.disposables.Disposable;

public class DigitizingModifyFragment extends BaseFragment {

    public static final String TAG = "DigitizingModifyFragment";

    FragmentDigitizingModifyBinding mBinding;
    private static final String DIGITIZING = "digitizing";
    private DigitalizerBean digitalizerBean;
    private DigitalizerViewModel digitalizerViewModel;
    private Disposable curDisposable;
    private CommonDialogFragment commonDialogFragment;

    public static DigitizingModifyFragment newInstance(DigitalizerBean DigitalizerBean) {
        DigitizingModifyFragment fragment = new DigitizingModifyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DIGITIZING, DigitalizerBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            digitalizerBean = (DigitalizerBean) getArguments().getSerializable(DIGITIZING);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_digitizing_modify, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.digitizing_modify));
        observableToolbar.set(toolbarInfo);
        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.setBean(digitalizerBean);

        mBinding.digitizingDetailPrimarySensorOnline.setOnItemClickListener(() -> {
            showDialog(getString(R.string.line_whether), Boolean.valueOf(digitalizerBean.Status.IsSubsonic1Exist),
                    (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        digitalizerBean.Status.IsSubsonic1Exist = true;
                        mBinding.digitizingDetailPrimarySensorOnline.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        digitalizerBean.Status.IsSubsonic1Exist = false;
                        mBinding.digitizingDetailPrimarySensorOnline.setText("否");
                        break;
                }

            });
        });
        mBinding.digitizingDetailSubSensorOnline.setOnItemClickListener(() -> {
            showDialog(getString(R.string.line_whether), Boolean.valueOf(digitalizerBean.Status.IsSubsonic2Exist),
                    (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        digitalizerBean.Status.IsSubsonic2Exist = true;
                        mBinding.digitizingDetailSubSensorOnline.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        digitalizerBean.Status.IsSubsonic2Exist = false;
                        mBinding.digitizingDetailSubSensorOnline.setText("否");
                        break;
                }

            });
        });
        mBinding.digitizingDetailPressureOnline.setOnItemClickListener(() -> {
            showDialog(getString(R.string.line_whether), Boolean.valueOf(digitalizerBean.Status.IsPressureExist),
                    (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        digitalizerBean.Status.IsPressureExist = true;
                        mBinding.digitizingDetailPressureOnline.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        digitalizerBean.Status.IsPressureExist = false;
                        mBinding.digitizingDetailPressureOnline.setText("否");
                        break;
                }

            });
        });
        mBinding.digitizingDetailFlowOnline.setOnItemClickListener(() -> {
            showDialog(getString(R.string.line_whether), Boolean.valueOf(digitalizerBean.Status.IsFluxExist),
                    (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        digitalizerBean.Status.IsFluxExist = true;
                        mBinding.digitizingDetailFlowOnline.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        digitalizerBean.Status.IsFluxExist = false;
                        mBinding.digitizingDetailFlowOnline.setText("否");
                        break;
                }

            });
        });

        mBinding.modify.setOnClickListener(view -> {
            if (curDisposable != null) {
                curDisposable.dispose();
            }
            digitalizerBean.ID = Integer.valueOf(mBinding.digitizingDetailId.getText());
            digitalizerBean.Name = mBinding.digitizingDetailName.getText();
            digitalizerBean.Company = mBinding.digitizingDetailCompany.getText();
            digitalizerBean.Longitude = Float.valueOf(mBinding.digitizingDetailLongitude.getText());
            digitalizerBean.Latitude = Float.valueOf(mBinding.digitizingDetailLatitude.getText());
//            digitalizerBean.Status.IsSubsonic1Exist = Boolean.valueOf(mBinding.digitizingDetailPrimarySensorOnline
//                    .getText());
//            digitalizerBean.Status.IsSubsonic2Exist = Boolean.valueOf(mBinding.digitizingDetailSubSensorOnline
//                    .getText());
//            digitalizerBean.Status.IsPressureExist = Boolean.valueOf(mBinding.digitizingDetailPressureOnline
// .getText());
//            digitalizerBean.Status.IsFluxExist = Boolean.valueOf(mBinding.digitizingDetailFlowOnline.getText());
            digitalizerBean.PrimarySensor.SensorChannel = Integer.valueOf(mBinding
                    .digitizingDetailPrimarySensorChannel.getText());
            digitalizerBean.SencondarySensor.SensorChannel = Integer.valueOf(mBinding
                    .digitizingDetailSubSensorChannel.getText());
            digitalizerBean.Status.Temperature = Float.valueOf(mBinding.digitizingDetailInnerTemperature.getText());
            digitalizerBean.Status.BatteryVoltage = Integer.valueOf(mBinding.digitizingDetailInputTension.getText());
            digitalizerBean.Status.OpenvpnAddr = mBinding.digitizingDetailVirtualAddress.getText();
            digitalizerBean.HardVersion = mBinding.digitizingDetailHardVersion.getText();
            digitalizerBean.SoftVersion = mBinding.digitizingDetailSoftVersion.getText();
            digitalizerBean.SerialNumber = mBinding.digitizingDetailSeriesNum.getText();
            digitalizerBean.SimNumber = mBinding.digitizingDetailSimNum.getText();

            ReqModifyDigitalizer reqModifyDigitalizer = new ReqModifyDigitalizer();
            reqModifyDigitalizer.toBean(digitalizerBean);

            curDisposable = digitalizerViewModel.reqModify(reqModifyDigitalizer);
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
        digitalizerViewModel = ViewModelProviders.of(this).get(DigitalizerViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!digitalizerViewModel.getmObservableModify().hasObservers()) {
            digitalizerViewModel.getmObservableModify().observe(this, digitalizerBean1 -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (digitalizerBean1 != null) {
                        ToastUtil.showToast("修改成功");
                        getActivity().onBackPressed();
                    } else {
                        ToastUtil.showToast("修改失败");
                    }
                }
            });
        }
    }
}
