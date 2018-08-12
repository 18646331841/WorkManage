package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.MessageEvent;
import com.barisetech.www.workmanage.databinding.FragmentLoginBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.LoginViewModel;

import org.greenrobot.eventbus.EventBus;

public class LoginFragment extends BaseFragment {
    public static final String TAG = "LoginFragment";

    private FragmentLoginBinding mBinding;
    private LoginViewModel loginViewModel;
    private boolean isFirst = true;

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

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.revealPassword.setOnClickListener(v -> revealPassword());

        mBinding.login.setOnClickListener(view -> {
            String account = mBinding.etAccount.getText().toString().trim();
            String password = mBinding.etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                ToastUtil.showToast("账号或密码不能为空");
            } else {
                isFirst = false;
                loginViewModel.login(account, password);
            }
        });
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
        loginViewModel.getObservableTokenInfo().observe(this, tokenInfo -> {
            if (!isFirst && null != tokenInfo && tokenInfo.isLoginResult()) {
                ToastUtil.showToast("登录成功");
                //登录成功，设置token到Application中
                BaseApplication.getInstance().curTokenInfo = tokenInfo;
                EventBus.getDefault().post(new MessageEvent(NavigationFragment.TAG));
            }
        });
    }
}
