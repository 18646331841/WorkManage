package com.barisetech.www.workmanage.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.view.fragment.AlarmDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;
import com.barisetech.www.workmanage.view.fragment.ContentFragment;
import com.barisetech.www.workmanage.view.fragment.FingerprintManagerFragment;

public class SecondActivity extends BaseActivity {

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void bindViews() {
        Bundle bundleExtra = getIntent().getExtras();
        if (null != bundleExtra) {
            EventBusMessage eventBusMessage = (EventBusMessage) bundleExtra.getSerializable("tag");
            if (null != eventBusMessage) {
                showActivityOrFragment(eventBusMessage);
            }
        }

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void showActivityOrFragment(EventBusMessage eventBusMessage) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String tag = eventBusMessage.message;
        switch (tag) {
            case LoginActivity.TAG:
                intent2Activity(LoginActivity.class);
                finish();
                break;
            case ContentFragment.TAG:
                transaction
                        .replace(R.id.second_framelayout, ContentFragment.newInstance(), tag).commit();
                break;
            case AlarmListFragment.TAG:
                transaction
                        .replace(R.id.second_framelayout, AlarmListFragment.newInstance(), tag)
                        .commit();
                break;
            case FingerprintManagerFragment.TAG:
                transaction
                        .replace(R.id.second_framelayout, FingerprintManagerFragment.newInstance(), tag).commit();
                break;
            case AlarmDetailsFragment.TAG:
                transaction
                        .addToBackStack(tag)
                        .replace(R.id.second_framelayout, AlarmDetailsFragment.newInstance((int)eventBusMessage.getArg1()), tag).commit();
                break;
        }
    }
}
