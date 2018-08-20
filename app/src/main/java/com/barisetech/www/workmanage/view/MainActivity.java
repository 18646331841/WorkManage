package com.barisetech.www.workmanage.view;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.view.fragment.AlarmDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;
import com.barisetech.www.workmanage.view.fragment.FingerprintManagerFragment;
import com.barisetech.www.workmanage.view.fragment.NavigationFragment;
import com.barisetech.www.workmanage.view.fragment.NewsFragment;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends BaseActivity {

    /**
     * 是否为大屏左右显示，true表示是
     */
    private boolean isTwoPanel = false;
    public Point point = new Point();

    @Override
    protected void loadViewLayout() {
        String account = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
        String ipPort = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_IP_PORT, "");
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(ipPort)) {
            //没有登录过,跳转到登录界面
            EventBusMessage eventBusMessage = new EventBusMessage(LoginActivity.TAG);
            showActivityOrFragment(eventBusMessage);
        }
        setContentView(R.layout.activity_main);
        BaseApplication.getInstance().requestPermissions(this);
        if (null != get(R.id.fragment_content)) {
            isTwoPanel = true;
        }

        Bundle bundle = getIntent().getExtras();
        String tag = null;
        if (null != bundle) {
            tag = bundle.getString("tag");
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (!isTwoPanel) {
//            //小屏设备界面，单个fragment
//            transaction.add(R.id.fragment_navigation, NavigationFragment.newInstance(), NavigationFragment.TAG).commit();
//        } else {
//            //大屏设备界面，左右两个fragment
        transaction.add(R.id.fragment_navigation, NavigationFragment.newInstance(tag), NavigationFragment.TAG);
        transaction.commit();
//        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            point.x = (int) ev.getRawX();
            point.y = (int) ev.getRawY();
        }
        EventBus.getDefault().post(point);
        return super.dispatchTouchEvent(ev);
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


    protected void showActivityOrFragment(EventBusMessage eventBusMessage) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String tag = eventBusMessage.message;
        if (!isTwoPanel) {
            //TODO 小屏幕，跳转到activity
            switch (tag) {
                case LoginActivity.TAG:
                    intent2Activity(LoginActivity.class);
                    finish();
                    break;
                default:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("tag", eventBusMessage);
                    intent2Activity(bundle, SecondActivity.class);
                    break;
            }

        } else {
            //TODO 大屏幕，显示fragment
            switch (tag) {
                case LoginActivity.TAG:
                    intent2Activity(LoginActivity.class);
                    finish();
                    break;
                case AlarmListFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, AlarmListFragment.newInstance(), tag)
                            .commit();
                    break;
                case FingerprintManagerFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.second_framelayout, FingerprintManagerFragment.newInstance(), tag).commit();
                    break;
                case AlarmDetailsFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.second_framelayout, AlarmDetailsFragment.newInstance((AlarmInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    break;
                case NewsFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.second_framelayout, NewsFragment.newInstance(), tag).commit();
                    break;
            }
        }
    }
}
