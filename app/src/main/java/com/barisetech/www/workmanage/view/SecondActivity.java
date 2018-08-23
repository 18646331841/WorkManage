package com.barisetech.www.workmanage.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.view.fragment.AlarmDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;
import com.barisetech.www.workmanage.view.fragment.FingerprintManagerFragment;
import com.barisetech.www.workmanage.view.fragment.MapFragment;
import com.barisetech.www.workmanage.view.fragment.NewsAddFragment;
import com.barisetech.www.workmanage.view.fragment.NewsListFragment;

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
                        .replace(R.id.second_framelayout, AlarmDetailsFragment.newInstance((AlarmInfo)
                                eventBusMessage.getArg1()), tag).commit();
                break;
            case MapFragment.TAG:
                Bundle bundle = new Bundle();
                bundle.putString("tag", MapFragment.TAG);
                intent2Activity(bundle, MainActivity.class);
                break;
            case NewsListFragment.TAG:
                transaction
                        .replace(R.id.second_framelayout, NewsListFragment.newInstance(), tag).commit();
                break;
            case NewsAddFragment.TAG:
                transaction
                        .replace(R.id.second_framelayout, NewsAddFragment.newInstance(), tag).commit();
                break;
        }
    }
}
