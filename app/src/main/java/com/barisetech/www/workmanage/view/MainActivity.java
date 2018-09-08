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
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.view.fragment.AlarmAnalysisFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmAnalysisListFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;
import com.barisetech.www.workmanage.view.fragment.DigitizingFragment;
import com.barisetech.www.workmanage.view.fragment.FingerprintManagerFragment;
import com.barisetech.www.workmanage.view.fragment.IncidentDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.IncidentListFragment;
import com.barisetech.www.workmanage.view.fragment.MapFragment;
import com.barisetech.www.workmanage.view.fragment.NavigationFragment;
import com.barisetech.www.workmanage.view.fragment.NewsAddFragment;
import com.barisetech.www.workmanage.view.fragment.NewsDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.NewsListFragment;
import com.barisetech.www.workmanage.view.fragment.PipeCollectionFragment;
import com.barisetech.www.workmanage.view.fragment.PipeFragment;
import com.barisetech.www.workmanage.view.fragment.PipeWorkFragment;
import com.barisetech.www.workmanage.view.fragment.SiteDetailFragment;
import com.barisetech.www.workmanage.view.fragment.SiteFragment;

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
            showActivityOrFragment(eventBusMessage, true);
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

    protected void showActivityOrFragment(EventBusMessage eventBusMessage, boolean isActivity) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String tag = eventBusMessage.message;
        if (!isTwoPanel) {
            //TODO 小屏幕，跳转到activity
            switch (tag) {
                case LoginActivity.TAG:
                    intent2Activity(LoginActivity.class);
                    finish();
                    break;
                case MapFragment.TAG:
                    transaction.add(R.id.fragment_navigation, NavigationFragment.newInstance(tag), NavigationFragment
                            .TAG).commit();
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
                case IncidentListFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, IncidentListFragment.newInstance(), tag)
                            .commit();
                    break;
                case IncidentDetailsFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.second_framelayout, IncidentDetailsFragment.newInstance((IncidentInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                case AlarmListFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, AlarmListFragment.newInstance(), tag)
                            .commit();
                    break;
                case FingerprintManagerFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, FingerprintManagerFragment.newInstance(), tag).commit();
                    break;
                case AlarmDetailsFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, AlarmDetailsFragment.newInstance((AlarmInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    break;
                case AlarmAnalysisFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, AlarmAnalysisFragment.newInstance((AlarmInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    break;
                case AlarmAnalysisListFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, AlarmAnalysisListFragment.newInstance(), tag).commit();
                    break;
                case NewsListFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, NewsListFragment.newInstance(), tag).commit();
                    break;
                case NewsAddFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, NewsAddFragment.newInstance(), tag).commit();
                    break;
                case NewsDetailsFragment.TAG:
                transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, NewsDetailsFragment.newInstance((String) eventBusMessage
                                    .getArg1()), tag).commit();
                    break;
                case SiteFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, SiteFragment.newInstance(), tag).commit();
                    break;
                case SiteDetailFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, SiteDetailFragment.newInstance((SiteBean) eventBusMessage
                                    .getArg1()), tag).commit();
                    break;
                case DigitizingFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, DigitizingFragment.newInstance(), tag).commit();
                    break;
                case PipeFragment.TAG:
                    transaction
                            .addToBackStack(tag)
                            .replace(R.id.fragment_content, PipeFragment.newInstance(), tag).commit();
                    break;
                case PipeCollectionFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeCollectionFragment.newInstance(), tag).commit();
                    break;
                case PipeWorkFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeWorkFragment.newInstance(), tag).commit();
                    break;
            }
        }
    }
}
