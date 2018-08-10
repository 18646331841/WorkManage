package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.MessageEvent;
import com.barisetech.www.workmanage.databinding.FragmentLoginBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.LoginViewModel;

import org.greenrobot.eventbus.EventBus;

public class LoginFragment extends Fragment {
    public static final String TAG = "LoginFragment";

    private FragmentLoginBinding mBinding;
    private LoginViewModel loginViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        initView();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        subscribeToModel(loginViewModel);
    }

    private void subscribeToModel(final LoginViewModel model) {
        model.getObservableTokenInfo().observe(this, tokenInfo -> {
            if (null != tokenInfo && tokenInfo.isLoginResult()) {
//                ToastUtil.showToast("登录成功");
                EventBus.getDefault().post(new MessageEvent(NavigationFragment.TAG));
            }
        });
    }

    private void initView() {
        mBinding.revealPassword.setOnClickListener(v -> revealPassword());

        mBinding.login.setOnClickListener(view -> {
            String account = mBinding.etAccount.getText().toString().trim();
            String password = mBinding.etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                ToastUtil.showToast("账号或密码不能为空");
            } else {
                loginViewModel.login(account, password);
            }
        });
    }

    private void revealPassword() {
        mBinding.revealPassword.setActivated(!mBinding.revealPassword.isActivated());
        mBinding.etPassword.setTransformationMethod(mBinding.revealPassword.isActivated() ? HideReturnsTransformationMethod
                .getInstance() : PasswordTransformationMethod.getInstance());
        mBinding.etPassword.setSelection(mBinding.etPassword.getText().toString().trim().length());
    }
}
