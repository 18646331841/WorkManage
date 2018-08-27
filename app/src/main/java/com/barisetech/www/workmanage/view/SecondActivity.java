package com.barisetech.www.workmanage.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.RawBean;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.view.fragment.AddSiteFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;
import com.barisetech.www.workmanage.view.fragment.DigitizingDetailFragment;
import com.barisetech.www.workmanage.view.fragment.DigitizingFragment;
import com.barisetech.www.workmanage.view.fragment.FingerprintManagerFragment;
import com.barisetech.www.workmanage.view.fragment.MapFragment;
import com.barisetech.www.workmanage.view.fragment.NewsAddFragment;
import com.barisetech.www.workmanage.view.fragment.NewsDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.NewsListFragment;
import com.barisetech.www.workmanage.view.fragment.SiteFragment;

import org.greenrobot.eventbus.EventBus;

public class SecondActivity extends BaseActivity {

    private int rawX;
    private int rawY;

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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        rawX= (int) ev.getRawX();
        rawY= (int) ev.getRawY();
        EventBus.getDefault().post(new RawBean(rawX,rawY));
        return super.dispatchTouchEvent(ev);
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
            case NewsDetailsFragment.TAG:
                transaction
                        .addToBackStack(NewsDetailsFragment.TAG)
                        .replace(R.id.second_framelayout, NewsDetailsFragment.newInstance((String) eventBusMessage
                                .getArg1()), tag).commit();
                break;
            case NewsAddFragment.TAG:
                transaction
                        .addToBackStack(NewsAddFragment.TAG)
                        .replace(R.id.second_framelayout, NewsAddFragment.newInstance(), tag).commit();
                break;
            case SiteFragment.TAG:
                transaction
                        .replace(R.id.second_framelayout, SiteFragment.newInstance(), tag)
                        .commit();
                break;
            case AddSiteFragment.TAG:
                transaction
                        .addToBackStack(AddSiteFragment.TAG)
                        .replace(R.id.second_framelayout, AddSiteFragment.newInstance(), tag).commit();
                break;
            case DigitizingFragment.TAG:
                transaction
                        .replace(R.id.second_framelayout, DigitizingFragment.newInstance(), tag)
                        .commit();
                break;
            case DigitizingDetailFragment.TAG:
                transaction
                        .addToBackStack(DigitizingDetailFragment.TAG)
                        .replace(R.id.second_framelayout, DigitizingDetailFragment.newInstance(), tag).commit();
                break;
        }
    }
}
