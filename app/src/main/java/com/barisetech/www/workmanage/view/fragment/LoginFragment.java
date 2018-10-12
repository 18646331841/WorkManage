package com.barisetech.www.workmanage.view.fragment;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import com.barisetech.www.workmanage.databinding.FragmentLoginBinding;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.NetworkUtil;
import com.barisetech.www.workmanage.utils.OsUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.LoginViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class LoginFragment extends BaseFragment {
    public static final String TAG = "LoginFragment";

    private FragmentLoginBinding mBinding;
    private LoginViewModel loginViewModel;
    private boolean isFirst = true;

    private Disposable curDisposable;
    private boolean fingerFlag;
    private boolean fingerOpen = true;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTwoText(getString(R.string.toolbar_setting));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        //TODO
//        mBinding.etAccount.setText("admin");
//        mBinding.etPassword.setText("123456");
        checkFingerPrint();
        mBinding.revealPassword.setOnClickListener(v -> revealPassword());
        mBinding.toolbar.tvTwo.setOnClickListener(view -> {
            EventBus.getDefault().post(new EventBusMessage(IpFragment.TAG));
        });
        mBinding.login.setOnClickListener(view -> {
            String account = mBinding.etAccount.getText().toString().trim();
            String password = mBinding.etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                ToastUtil.showToast(getString(R.string.account_null));
            } else {
                loginViewModel.removeDisposable(curDisposable);
                isFirst = false;
                EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
//                curDisposable = loginViewModel.login(account, password);

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

                curDisposable = loginViewModel.login(reqAuth, account, password);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void checkFingerPrint() {
        FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(BaseApplication.getInstance()
                .getApplicationContext());

        if (!managerCompat.isHardwareDetected()) {
            LogUtil.d(TAG, "设备不支持指纹");
            fingerOpen = false;
            return;
        }
        KeyguardManager keyguardManager = (KeyguardManager) BaseApplication.getInstance().getApplicationContext()
                .getSystemService(BaseApplication.KEYGUARD_SERVICE);
        if (null == keyguardManager || !keyguardManager.isKeyguardSecure()) {
            LogUtil.d(TAG, "设备没处于安全保护中");
            fingerOpen = false;
            return;
        }
        if (!managerCompat.hasEnrolledFingerprints()) {
            LogUtil.d(TAG, "设备未设置指纹");
            fingerOpen = false;
            return;
        }
    }

    private void revealPassword() {
        mBinding.revealPassword.setActivated(!mBinding.revealPassword.isActivated());
        mBinding.etPassword.setTransformationMethod(mBinding.revealPassword.isActivated() ?
                HideReturnsTransformationMethod
                        .getInstance() : PasswordTransformationMethod.getInstance());
        mBinding.etPassword.setSelection(mBinding.etPassword.getText().toString().trim().length());
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
                        //登录成功，设置token到sp
//                BaseApplication.getInstance().curTokenInfo = tokenInfo;
                        SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_TOKEN, tokenInfo.getToken());
                        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
                        if (fingerOpen) {
                            fingerFlag = SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SP_LOGIN_FP, true);
                            if (fingerFlag) {
                                EventBus.getDefault().post(new EventBusMessage(FingerFragment.TAG));
                            } else {
                                EventBus.getDefault().post(new EventBusMessage(NavigationFragment.TAG));
                            }
                        } else {
                            EventBus.getDefault().post(new EventBusMessage(NavigationFragment.TAG));
                        }


                    }
                }
            });
        }

        if (!loginViewModel.getObservableLoginFail().hasObservers()) {
            loginViewModel.getObservableLoginFail().observe(this, errorCode -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

                    if (null != errorCode) {

                        if (errorCode == Config.ERROR_LOGIN_FAILED) {
                            ToastUtil.showToast(getString(R.string.account_mistake));
                        } else if (errorCode == Config.ERROR_NETWORK) {
                            if (!NetworkUtil.isNetworkAvailable(BaseApplication.getInstance().getApplicationContext()
                            )) {
                                ToastUtil.showToast(getString(R.string.network_error));
                            }
                        } else {
                            ToastUtil.showToast(getString(R.string.login_fail));
                        }
                    }
                }
            });
        }
    }
}
