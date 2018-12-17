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
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.digitalizer.DigitalizerBean;
import com.barisetech.www.workmanage.bean.pipetap.ReqModifyPipeTap;
import com.barisetech.www.workmanage.databinding.FragmentDigitizingDetailBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.SystemUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.viewmodel.PipeTapViewModel;
import com.barisetech.www.workmanage.viewmodel.PipeWorkViewModel;

import org.greenrobot.eventbus.EventBus;

public class DigitizingDetailFragment extends BaseFragment {

    public static final String TAG = "DigitizingDetailFragment";

    FragmentDigitizingDetailBinding mBinding;
    private static final String DIGITIZING = "digitizing";
    private DigitalizerBean digitalizerBean;

    private PipeTapViewModel pipeTapViewModel;
    private CommonDialogFragment commonDialogFragment;

    public static DigitizingDetailFragment newInstance(DigitalizerBean DigitalizerBean) {
        DigitizingDetailFragment fragment = new DigitizingDetailFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_digitizing_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.digitizing_detail));
//        if (SystemUtil.isAdmin()) {
//            toolbarInfo.setOneText("修改");
//        } else {
            mBinding.addSite.setVisibility(View.GONE);
//        }

        observableToolbar.set(toolbarInfo);
        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.setBean(digitalizerBean);

        String role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
        if (role.equals(BaseConstant.ROLE_SUPER_ADMINS) || role.equals(BaseConstant.ROLE_ADMINS)) {
            mBinding.seniorCard.setVisibility(View.VISIBLE);
        } else {
            mBinding.seniorCard.setVisibility(View.GONE);
        }
//        mBinding.toolbar.tvOne.setOnClickListener(view -> {
//            EventBusMessage eventBusMessage = new EventBusMessage(DigitizingModifyFragment.TAG);
//            eventBusMessage.setArg1(digitalizerBean);
//            EventBus.getDefault().post(eventBusMessage);
//        });

//        mBinding.addSite.setOnClickListener(view -> {
//            EventBusMessage eventBusMessage = new EventBusMessage(AddSiteFragment.TAG);
//            eventBusMessage.setArg1(String.valueOf(digitalizerBean.ID));
//            EventBus.getDefault().post(eventBusMessage);
//        });

        mBinding.openPipeTab.setOnClickListener(view -> {
            showDialog("申请阀门", ((radioGroup, i) -> {
                closeDialog();
                switch (i) {
                    case R.id.dialog_open_rb:
                        requestPipeTap(true);
                        break;
                    case R.id.dialog_close_rb:
                        requestPipeTap(false);
                        break;
                }
            }));
        });
    }

    private void showDialog(String title, RadioGroup.OnCheckedChangeListener
            onCheckedChangeListener) {
        commonDialogFragment = DialogFragmentHelper.showOpenDialog(getFragmentManager(), title,
                onCheckedChangeListener, true);
    }

    private void closeDialog() {
        if (commonDialogFragment != null) {
            commonDialogFragment.dismiss();
        }
    }

    /**
     * 申请阀门
     *
     * @param b true:打开, false:关闭
     */
    private void requestPipeTap(boolean b) {
        ReqModifyPipeTap reqModifyPipeTap = new ReqModifyPipeTap();
        reqModifyPipeTap.isApprove = "false";
        reqModifyPipeTap.ID = "0";
        reqModifyPipeTap.Remark = "";
        String account = SharedPreferencesUtil.getInstance().getString(BaseConstant
                .SP_ACCOUNT, "");
        reqModifyPipeTap.Applicant = account;
        String time = TimeUtil.ms2Date(System.currentTimeMillis());
        reqModifyPipeTap.ApplicationTime = time;
        reqModifyPipeTap.SiteID = String.valueOf(digitalizerBean.ID);
        reqModifyPipeTap.SiteName = digitalizerBean.Name;
        reqModifyPipeTap.TapSwitch = String.valueOf(b);
        reqModifyPipeTap.LV = "2";
        String role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
        if (role.equals(BaseConstant.ROLE_ADMINS) || role.equals(BaseConstant.ROLE_SUPER_ADMINS)) {
            reqModifyPipeTap.Approver = account;
            reqModifyPipeTap.ApproverState = String.valueOf(b);
            reqModifyPipeTap.ApproverTime = time;
        } else {
            reqModifyPipeTap.Approver = "";
            reqModifyPipeTap.ApproverState = "";
            reqModifyPipeTap.ApproverTime = "";
        }

        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
        pipeTapViewModel.modify(reqModifyPipeTap);
    }

    @Override
    public void bindViewModel() {
        pipeTapViewModel = ViewModelProviders.of(this).get(PipeTapViewModel.class);

    }

    @Override
    public void subscribeToModel() {
        if (!pipeTapViewModel.getmObservableModify().hasObservers()) {
            pipeTapViewModel.getmObservableModify().observe(this, s -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (!TextUtils.isEmpty(s)) {
                        ToastUtil.showToast(s);
                    } else {
                        ToastUtil.showToast("申请失败");
                    }
                }
            });
        }


        if (!pipeTapViewModel.getmObservableModify().hasObservers()) {
            pipeTapViewModel.getmObservableModify().observe(this, s -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (!TextUtils.isEmpty(s)) {
                        ToastUtil.showToast(s);
                    } else {
                        ToastUtil.showToast("申请失败");
                    }
                }
            });
        }
    }
}
