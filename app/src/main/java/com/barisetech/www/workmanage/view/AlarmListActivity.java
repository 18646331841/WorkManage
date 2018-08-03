package com.barisetech.www.workmanage.view;

import android.os.Bundle;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.bean.MessageEvent;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;

public class AlarmListActivity extends BaseActivity {

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_alarm_list);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.alarm_list_framelayout, AlarmListFragment.newInstance(), AlarmListFragment.TAG)
                .commit();
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

    @Override
    protected void showActivityOrFragment(MessageEvent messageEvent) {
        //TODO 跳转Activity
    }
}
