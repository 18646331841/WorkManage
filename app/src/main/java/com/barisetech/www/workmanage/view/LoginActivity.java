package com.barisetech.www.workmanage.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.bean.MessageEvent;
import com.barisetech.www.workmanage.utils.FragmentUtil;
import com.barisetech.www.workmanage.view.fragment.LoginFragment;

public class LoginActivity extends BaseActivity {
    public static final String TAG = "LoginActivity";

    private boolean isUnanthorized = false;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void bindViews() {
        MessageEvent messageEvent = new MessageEvent(LoginFragment.TAG);
        showActivityOrFragment(messageEvent);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void showActivityOrFragment(MessageEvent messageEvent) {
        String tag = messageEvent.message;
        switch (tag) {
            case LoginFragment.TAG:
                FragmentUtil.replaceSupportFragment(this, R.id.login_container, LoginFragment.class, LoginFragment.TAG,
                        false, false);
                break;
            default:
                intent2Activity(MainActivity.class);
                finish();
                break;
        }

    }

}
