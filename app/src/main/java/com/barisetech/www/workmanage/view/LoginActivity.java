package com.barisetech.www.workmanage.view;

import android.os.Bundle;
import android.text.TextUtils;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.utils.FragmentUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.view.fragment.FingerFragment;
import com.barisetech.www.workmanage.view.fragment.IpFragment;
import com.barisetech.www.workmanage.view.fragment.LoginFragment;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends BaseActivity {
    public static final String TAG = "LoginActivity";

    private boolean isUnanthorized = false;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void bindViews() {
        String ipPort = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_IP_PORT, "");
        if (TextUtils.isEmpty(ipPort)) {
            showActivityOrFragment(new EventBusMessage(IpFragment.TAG), true);
        } else {
            showActivityOrFragment(new EventBusMessage(LoginFragment.TAG), true);
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void showActivityOrFragment(EventBusMessage eventBusMessage, boolean isActivity) {
        String tag = eventBusMessage.message;
        switch (tag) {
            case LoginFragment.TAG:
                FragmentUtil.replaceSupportFragment(this, R.id.login_container, LoginFragment.class, LoginFragment.TAG,
                        false, false);
                break;
            case IpFragment.TAG:
                FragmentUtil.replaceSupportFragment(this, R.id.login_container, IpFragment.class, IpFragment.TAG,
                        false, false);
                break;
            case FingerFragment.TAG:
                FragmentUtil.replaceSupportFragment(this, R.id.login_container, FingerFragment.class, FingerFragment.TAG,
                        false, false);
                break;
            default:
                intent2Activity(MainActivity.class);
                finish();
                break;
        }

    }

}
