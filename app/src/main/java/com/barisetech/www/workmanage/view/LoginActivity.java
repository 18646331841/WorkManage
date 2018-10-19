package com.barisetech.www.workmanage.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.service.MyNotifyService;
import com.barisetech.www.workmanage.utils.FragmentUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.view.fragment.FingerFragment;
import com.barisetech.www.workmanage.view.fragment.IncidentDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.IpFragment;
import com.barisetech.www.workmanage.view.fragment.LoginFragment;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends BaseActivity {
    public static final String TAG = "LoginActivity";

    private boolean isUnanthorized = false;
    private MyNotifyService.MyBinder myBinder;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBinder != null) {
            myBinder.startInterval();
        } else {
            Intent upserviceintent = new Intent(this, MyNotifyService.class);
            bindService(upserviceintent, serviceConnection, BIND_AUTO_CREATE);
            myBinder.startInterval();
        }
        unbindService(serviceConnection);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
        BaseApplication.getInstance().requestPermissions(this);
        Intent upserviceintent = new Intent(this, MyNotifyService.class);
        bindService(upserviceintent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (MyNotifyService.MyBinder) iBinder;
            myBinder.stopInterval();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void bindViews() {
        String ipPort = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_IP_PORT, "");
        if (TextUtils.isEmpty(ipPort)) {
            showActivityOrFragment(new EventBusMessage(IpFragment.TAG), true);
        } else if (SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SP_LOGIN_FP, false)) {
            showActivityOrFragment(new EventBusMessage(FingerFragment.TAG), true);
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        String tag = eventBusMessage.message;
        switch (tag) {
            case LoginFragment.TAG:
//                FragmentUtil.replaceSupportFragment(this, R.id.login_container, LoginFragment.class, LoginFragment
// .TAG,
//                        false, false);
                transaction
                        .replace(R.id.login_container, LoginFragment.newInstance(eventBusMessage), tag)
                        .commit();
                break;
            case IpFragment.TAG:
//                FragmentUtil.replaceSupportFragment(this, R.id.login_container, IpFragment.class, IpFragment.TAG,
//                        false, false);
                transaction
                        .replace(R.id.login_container, IpFragment.newInstance(eventBusMessage), tag)
                        .commit();
                break;
            case FingerFragment.TAG:
                FragmentUtil.replaceSupportFragment(this, R.id.login_container, FingerFragment.class, FingerFragment
                                .TAG,
                        false, false);
                break;
            default:
                intent2Activity(MainActivity.class);
                finish();
                break;
        }

    }

}
