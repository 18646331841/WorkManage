package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAuth;
import com.barisetech.www.workmanage.databinding.FragmentFingerBinding;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.utils.FingerprintUtil;
import com.barisetech.www.workmanage.utils.NetworkUtil;
import com.barisetech.www.workmanage.utils.OsUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.LoginViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.barisetech.www.workmanage.utils.ToastUtil.showToast;
import static com.barisetech.www.workmanage.utils.ToastUtil.showToastLong;

public class FingerFragment extends BaseFragment {
    public static final String TAG = "FingerFragment";

    private FragmentFingerBinding mBinding;
    private LoginViewModel loginViewModel;
    private boolean isFirst = true;

    public FingerFragment() {
    }

    public static FingerFragment newInstance() {
        FingerFragment fragment = new FingerFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_finger, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.finger_manager));
        if (SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SP_LOGIN_FP, false)) {
//            mBinding.toolbar.tvTwo.setVisibility(View.GONE);
            toolbarInfo.setTwoText(getString(R.string.toolbar_setting));
        } else {
            toolbarInfo.setTwoText(getString(R.string.skip));
        }
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        FingerprintUtil.getInstance().callFingerprint(onCallBackListenr);
    }

    @Override
    public void onPause() {
        super.onPause();
        FingerprintUtil.getInstance().cancel();
    }

    private void initView() {
        mBinding.toolbar.tvTwo.setOnClickListener(view -> {
            if (mBinding.toolbar.tvTwo.getText().toString().equals(getString(R.string.skip))) {
                FingerprintUtil.getInstance().cancel();
                EventBus.getDefault().post(new EventBusMessage(NavigationFragment.TAG));
                SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SP_LOGIN_FP, false);
            } else {
                EventBusMessage eventBusMessage = new EventBusMessage(IpFragment.TAG);
                eventBusMessage.setArg1(TAG);
                EventBus.getDefault().post(eventBusMessage);
            }
        });
    }


    @Override
    public void bindViewModel() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!loginViewModel.getObservableTokenInfo().hasObservers()) {
            loginViewModel.getObservableTokenInfo().observe(this, tokenInfo -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (!isFirst && null != tokenInfo && tokenInfo.isLoginResult()) {

                        ToastUtil.showToast(getString(R.string.login_success));
                        SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_TOKEN, tokenInfo.getToken());
                        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
                        EventBus.getDefault().post(new EventBusMessage(NavigationFragment.TAG));
                    }
                }
            });
        }

        if (!loginViewModel.getObservableLoginFail().hasObservers()) {
            loginViewModel.getObservableLoginFail().observe(this, errorCode -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != errorCode) {
                        ToastUtil.showToast(getString(R.string.login_fail_finger));
                        FingerprintUtil.getInstance().callFingerprint(onCallBackListenr);
                    }
                }
            });
        }
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
            if (errMsgId == 7) {

                if (mBinding.toolbar.tvTwo.getText().toString().equals(getString(R.string.skip))) {
                    showToastLong(errString);
                    EventBus.getDefault().post(new EventBusMessage(NavigationFragment.TAG));
                    SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SP_LOGIN_FP, false);
                } else {
                    showToastLong("指纹识别失败，使用账号密码登陆");
                    EventBusMessage eventBusMessage = new EventBusMessage(LoginFragment.TAG);
                    eventBusMessage.setArg1(TAG);
                    EventBus.getDefault().post(eventBusMessage);
                }
            }
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
            if (mBinding.toolbar.tvTwo.getText().toString().equals(getString(R.string.skip))) {
                SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SP_LOGIN_FP, true);
                EventBus.getDefault().post(new EventBusMessage(NavigationFragment.TAG));
            } else {
                login();
            }
        }
    };

    private void login() {
        isFirst = false;
        String account = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
        String password = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_PASSWORD, "");

        ReqAuth reqAuth = new ReqAuth();
        reqAuth.ID = "0";
        String ipPort = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_IP_PORT, "");
        if (!TextUtils.isEmpty(ipPort)) {
            String[] split = ipPort.split("_");
            if (split.length > 1) {
                reqAuth.ServerIP = split[0];
                reqAuth.ServerPort = split[1];
                reqAuth.ServerName = ipPort;
            }
        }

        String serialNumber = OsUtil.getSerialNumber(getContext());
        if (!TextUtils.isEmpty(serialNumber)) {
            reqAuth.SerialNumber = serialNumber;
        }

        boolean pad = OsUtil.isPad(getContext());
        if (pad) {
            reqAuth.EquipmentType = BaseConstant.TYPE_PAD;
        } else {
            reqAuth.EquipmentType = BaseConstant.TYPE_PHONE;
        }

        Point screenMetrics = OsUtil.getScreenMetrics(getContext());
        reqAuth.ScreenHeight = String.valueOf(screenMetrics.y);
        reqAuth.ScreenWidth = String.valueOf(screenMetrics.x);

        reqAuth.ApplicatorName = account;

        String ip = OsUtil.getIp();
        if (!TextUtils.isEmpty(ip)) {
            reqAuth.ApplicatorIPAdress = ip;
        }

        reqAuth.ApplicatorTime = TimeUtil.ms2Date(System.currentTimeMillis());

        List<ReqAuth.SIMCardListBean> simCardListBeans = new ArrayList<>();
        String imsi = OsUtil.getIMSI(getContext());
        if (!TextUtils.isEmpty(imsi)) {
            ReqAuth.SIMCardListBean simCardListBean = new ReqAuth.SIMCardListBean("0", imsi);
            simCardListBeans.add(simCardListBean);
        }
        reqAuth.SIMCardList = simCardListBeans;

        List<ReqAuth.IMEIListBean> imeiListBeans = new ArrayList<>();
        List<String> imeis = OsUtil.getIMEI(getContext());
        if (imeis != null && imeis.size() > 0) {
            for (int i = 0; i < imeis.size(); i++) {
                String imei = imeis.get(i);
                ReqAuth.IMEIListBean imeiListBean = new ReqAuth.IMEIListBean(String.valueOf(i), imei);
                imeiListBeans.add(imeiListBean);
            }
        }
        reqAuth.IMEIList = imeiListBeans;

        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
        loginViewModel.login(reqAuth, account, password);
    }
}
