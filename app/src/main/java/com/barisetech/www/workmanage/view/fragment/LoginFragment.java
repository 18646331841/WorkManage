package com.barisetech.www.workmanage.view.fragment;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        et_ip = view.findViewById(R.id.et_ip);
        et_port = view.findViewById(R.id.et_port);
        et_count = view.findViewById(R.id.et_count);
        et_password = view.findViewById(R.id.et_password);
        revealPassword = view.findViewById(R.id.revealPassword);
        revealPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealPassword();
            }
        });

    }


    private void revealPassword() {
        revealPassword.setActivated(!revealPassword.isActivated());
        et_password.setTransformationMethod(revealPassword.isActivated() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        et_password.setSelection(et_password.getText().toString().trim().length());
    }
}
