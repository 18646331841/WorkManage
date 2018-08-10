package com.barisetech.www.workmanage.view;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.bean.MessageEvent;
import com.barisetech.www.workmanage.utils.FragmentUtil;
import com.barisetech.www.workmanage.view.fragment.LoginFragment;

public class LoginActivity extends BaseActivity {


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void bindViews() {
        FragmentUtil.replaceSupportFragment(this, R.id.login_container, LoginFragment.class, LoginFragment.TAG,
                false, false);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void showActivityOrFragment(MessageEvent messageEvent) {
        intent2Activity(MainActivity.class);
        finish();
    }

}
