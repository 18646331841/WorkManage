package com.barisetech.www.workmanage.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.bean.MessageEvent;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;
import com.barisetech.www.workmanage.view.fragment.ContentFragment;
import com.barisetech.www.workmanage.view.fragment.NavigationFragment;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends BaseActivity {

    /**
     * 是否为大屏左右显示，true表示是
     */
    private boolean isTwoPanel = false;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);

        if (null != get(R.id.fragment_content)) {
            isTwoPanel = true;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!isTwoPanel) {
            //小屏设备界面，单个fragment
            transaction.add(R.id.fragment_navigation, NavigationFragment.newInstance(), NavigationFragment.TAG).commit();
        } else {
            //大屏设备界面，左右两个fragment
            transaction.add(R.id.fragment_navigation, NavigationFragment.newInstance(), NavigationFragment.TAG);
            transaction
                    .replace(R.id.fragment_content, ContentFragment.newInstance(), ContentFragment.TAG);
            transaction.commit();
        }
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    @Override
    protected void setListener() {
    }


    protected void showActivityOrFragment(MessageEvent messageEvent) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!isTwoPanel) {
            //TODO 小屏幕，跳转到activity
            switch (messageEvent.message) {
                case AlarmListFragment.TAG:
                    intent2Activity(AlarmListActivity.class);
                    break;
            }
        } else {
            //TODO 大屏幕，显示fragment
            switch (messageEvent.message) {
                case ContentFragment.TAG:
                    transaction
//                            .addToBackStack(ContentFragment.TAG)
                            .replace(R.id.fragment_content, ContentFragment.newInstance(), ContentFragment.TAG).commit();
                    break;
                case AlarmListFragment.TAG:
                    transaction
                            .addToBackStack(AlarmListFragment.TAG)
                            .replace(R.id.fragment_content, AlarmListFragment.newInstance(), AlarmListFragment.TAG)
                            .commit();
                    break;
            }
        }
    }
}
