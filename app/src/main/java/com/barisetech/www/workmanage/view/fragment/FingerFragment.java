package com.barisetech.www.workmanage.view.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentFingerBinding;
import com.barisetech.www.workmanage.utils.FingerprintUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import static com.barisetech.www.workmanage.utils.ToastUtil.showToast;

public class FingerFragment extends BaseFragment{
    public static final String TAG = "FingerFragment";

    private FragmentFingerBinding mBinding;

    public FingerFragment() {
    }

    public static FingerFragment newInstance() {
        FingerFragment fragment = new FingerFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_finger, container, false);
        FingerprintUtil.getInstance().callFingerprint(onCallBackListenr);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.finger_manager));
        toolbarInfo.setTwoText(getString(R.string.skip));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        if (SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SP_LOGIN_FP,true)){
            mBinding.toolbar.tvTwo.setVisibility(View.GONE);
        }else {
            mBinding.toolbar.tvTwo.setVisibility(View.VISIBLE);
        }

        mBinding.toolbar.tvTwo.setOnClickListener(view -> {
            EventBus.getDefault().post(new EventBusMessage(NavigationFragment.TAG));
            SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SP_LOGIN_FP, false);
        });
    }


    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }

    private FingerprintUtil.OnCallBackListenr onCallBackListenr = new FingerprintUtil.OnCallBackListenr() {
        @Override
        public void onSupportFailed(String msg) {
            showToast(msg);
        }

        @Override
        public void onAuthenticationStart() {
            showToast("开始指纹识别");
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            showToast(errString);
        }

        @Override
        public void onAuthenticationFailed() {
            showToast("指纹识别失败");

        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            showToast(helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            showToast("指纹识别成功");
            SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SP_LOGIN_FP, true);
            EventBus.getDefault().post(new EventBusMessage(NavigationFragment.TAG));
        }
    };
}
