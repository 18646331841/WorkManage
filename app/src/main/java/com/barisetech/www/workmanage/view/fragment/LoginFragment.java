package com.barisetech.www.workmanage.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.barisetech.www.workmanage.R;

public class LoginFragment extends Fragment {
    public static final String TAG = "LoginFragment";
    private Button login;
    private EditText et_ip;
    private EditText et_port;
    private EditText et_count;
    private EditText et_password;
    private ImageView revealPassword;

//    private FragmentLoginBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);

//        initView(view);
//        return mBinding.getRoot();
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    private void initView(View view) {

//        mBinding.revealPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                revealPassword();
//            }
//        });

    }

    public void loginCallBack() {

    }

    private void revealPassword() {
        revealPassword.setActivated(!revealPassword.isActivated());
        et_password.setTransformationMethod(revealPassword.isActivated() ? HideReturnsTransformationMethod
                .getInstance() : PasswordTransformationMethod.getInstance());
        et_password.setSelection(et_password.getText().toString().trim().length());
    }
}
