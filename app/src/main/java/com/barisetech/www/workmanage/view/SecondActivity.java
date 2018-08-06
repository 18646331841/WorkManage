package com.barisetech.www.workmanage.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.bean.MessageEvent;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;
import com.barisetech.www.workmanage.view.fragment.ContentFragment;
import com.barisetech.www.workmanage.view.fragment.FingerprintManagerFragment;
import com.barisetech.www.workmanage.view.fragment.Messagefragment;

public class SecondActivity extends BaseActivity {

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void bindViews() {
        Bundle bundleExtra = getIntent().getExtras();
        if (null != bundleExtra) {
            String tag = bundleExtra.getString("tag", "");
            MessageEvent messageEvent = new MessageEvent(tag);
            showActivityOrFragment(messageEvent);
        }

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void showActivityOrFragment(MessageEvent messageEvent) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String tag = messageEvent.message;
        switch (tag) {
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
        }
    }
}
