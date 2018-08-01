package com.barisetech.www.workmanage.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.view.fragment.ContentFragment;
import com.barisetech.www.workmanage.view.fragment.NavigationFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (get(R.id.fragment_content) == null) {
            //小屏设备界面，单个fragment
            transaction.add(R.id.fragment_navigation, NavigationFragment.newInstance(), NavigationFragment.TAG).commit();
        } else {
            //大屏设备界面，左右两个fragment
            transaction.add(R.id.fragment_navigation, NavigationFragment.newInstance(), NavigationFragment.TAG);
            transaction
                    .addToBackStack(ContentFragment.TAG)
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

    /**
     * @param tag
     */
    private void showFragment(String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (get(R.id.fragment_content) == null) {
            //TODO 小屏幕，跳转到activity
        } else {
            //TODO 大屏幕，显示fragment
            switch (tag) {
                case ContentFragment.TAG:
                    transaction.addToBackStack(ContentFragment.TAG)
                            .replace(R.id.fragment_content, ContentFragment.newInstance(), ContentFragment.TAG).commit();
                    break;
            }
        }
    }
}
