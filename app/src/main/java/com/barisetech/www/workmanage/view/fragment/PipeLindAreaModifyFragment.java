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
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipelindarea.PipeLindAreaInfo;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqAddPipelindArea;
import com.barisetech.www.workmanage.bean.pipelindarea.ReqDeletePipeLindArea;
import com.barisetech.www.workmanage.databinding.FragmentPipeLindAreaModifyBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.PipeblindAreaViewModel;

import org.greenrobot.eventbus.EventBus;

public class PipeLindAreaModifyFragment extends BaseFragment {

    public static final String TAG = "PipeLindAreaModifyFragment";
    FragmentPipeLindAreaModifyBinding mBinding;
    private static final String LIND_AREA_ID = "pipeLindAreaInfo";
    private PipeLindAreaInfo pipeLindAreaInfo;
    private PipeblindAreaViewModel pipeblindAreaViewModel;
    private CommonDialogFragment commonDialogFragment;
    private ReqAddPipelindArea info = new ReqAddPipelindArea();


    public static PipeLindAreaModifyFragment newInstance(PipeLindAreaInfo pipeLindAreaInfo) {
        PipeLindAreaModifyFragment fragment = new PipeLindAreaModifyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LIND_AREA_ID, pipeLindAreaInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            pipeLindAreaInfo = (PipeLindAreaInfo) getArguments().getSerializable(LIND_AREA_ID);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pipe_lind_area_modify, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.modify_pipe_lind_area));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.modifyLindId.setText(String.valueOf(pipeLindAreaInfo.getId()));
        mBinding.modifyLindPipeId.setText(String.valueOf(pipeLindAreaInfo.getPipeId()));
        mBinding.modifyLindIsenable.setText(pipeLindAreaInfo.isIsEnabled() ? "是" : "否");
        mBinding.modifyLindType.setText(String.valueOf(pipeLindAreaInfo.getType()));
        mBinding.modifyLindStart.setText(String.valueOf(pipeLindAreaInfo.getStartDistance()));
        mBinding.modifyLindEnd.setText(String.valueOf(pipeLindAreaInfo.getEndDistance()));
        mBinding.modifyLindRemark.setText(pipeLindAreaInfo.getRemark());

        mBinding.modifyLindIsenable.setOnItemClickListener(() -> {
            showDialog(getString(R.string.is_enable), Boolean.valueOf(info.getIsEnabled()), (radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_yes_rb:
                        info.setIsEnabled("true");
                        mBinding.modifyLindIsenable.setText("是");
                        break;
                    case R.id.dialog_no_rb:
                        info.setIsEnabled("false");
                        mBinding.modifyLindIsenable.setText("否");
                        break;
                }

            });
        });
        mBinding.modifyLindArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setIsAdd("false");
                info.setId(mBinding.modifyLindId.getText().toString());
                info.setPipeId(mBinding.modifyLindPipeId.getText().toString());
                info.setRemark(mBinding.modifyLindRemark.getText().toString());
                info.setType(mBinding.modifyLindType.getText().toString());
                info.setEndDistance(mBinding.modifyLindEnd.getText().toString());
                info.setStartDistance(mBinding.modifyLindStart.getText().toString());
                info.setIsEnabled(mBinding.modifyLindIsenable.getText().toString().equals("是") ? "true" : "false");

                EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
                pipeblindAreaViewModel.reqAddOrModifyPipeLindArea(info);
            }
        });

        mBinding.delLindArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReqDeletePipeLindArea info = new ReqDeletePipeLindArea();
                info.setPipeBlindAreaId(String.valueOf(pipeLindAreaInfo.getId()));

                EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
                pipeblindAreaViewModel.reqDeletePipeLindArea(info);
            }
        });
    }

    @Override
    public void bindViewModel() {
        pipeblindAreaViewModel = ViewModelProviders.of(this).get(PipeblindAreaViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeblindAreaViewModel.getMeObservableAddOrModifyLindArea().hasObservers()) {
            pipeblindAreaViewModel.getMeObservableAddOrModifyLindArea().observe(this, s -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != s) {
                        if (s.equals("成功修改")) {
                            ToastUtil.showToast("成功修改");
                            getActivity().onBackPressed();
                        } else {
                            ToastUtil.showToast(s);
                        }
                    } else {
                        ToastUtil.showToast("失败修改");
                    }
                }
            });
        }

        if (!pipeblindAreaViewModel.getmObservableLindAreaDel().hasObservers()) {
            pipeblindAreaViewModel.getmObservableLindAreaDel().observe(this, flag -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != flag) {
                        if (flag) {
                            ToastUtil.showToast("删除成功");
                            getActivity().onBackPressed();
                        } else {
                            ToastUtil.showToast("删除失败");
                        }
                    } else {
                        ToastUtil.showToast("删除失败");
                    }
                }
            });
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
