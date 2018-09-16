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
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipelindarea.PipeLindAreaInfo;
import com.barisetech.www.workmanage.bean.pipework.PipeWork;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.view.fragment.AddSiteFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmAnalysisDetailFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmAnalysisFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmAnalysisListFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;
import com.barisetech.www.workmanage.view.fragment.DigitizingDetailFragment;
import com.barisetech.www.workmanage.view.fragment.DigitizingFragment;
import com.barisetech.www.workmanage.view.fragment.FingerprintManagerFragment;
import com.barisetech.www.workmanage.view.fragment.IncidentDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.IncidentListFragment;
import com.barisetech.www.workmanage.view.fragment.MapFragment;
import com.barisetech.www.workmanage.view.fragment.ModifySiteFragment;
import com.barisetech.www.workmanage.view.fragment.NavigationFragment;
import com.barisetech.www.workmanage.view.fragment.NewsAddFragment;
import com.barisetech.www.workmanage.view.fragment.NewsDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.NewsListFragment;
import com.barisetech.www.workmanage.view.fragment.PipeAddFragment;
import com.barisetech.www.workmanage.view.fragment.PipeCollectionAddFragment;
import com.barisetech.www.workmanage.view.fragment.PipeCollectionDetailFragment;
import com.barisetech.www.workmanage.view.fragment.PipeCollectionFragment;
import com.barisetech.www.workmanage.view.fragment.PipeCollectionModifyFragment;
import com.barisetech.www.workmanage.view.fragment.PipeDetailFragment;
import com.barisetech.www.workmanage.view.fragment.PipeFragment;
import com.barisetech.www.workmanage.view.fragment.PipeLindAreaAddFragment;
import com.barisetech.www.workmanage.view.fragment.PipeLindAreaDetailFragment;
import com.barisetech.www.workmanage.view.fragment.PipeLindAreaFragment;
import com.barisetech.www.workmanage.view.fragment.PipeLindAreaModifyFragment;
import com.barisetech.www.workmanage.view.fragment.PipeModifyFragment;
import com.barisetech.www.workmanage.view.fragment.PipeWorkAddFragment;
import com.barisetech.www.workmanage.view.fragment.PipeWorkDetailFragment;
import com.barisetech.www.workmanage.view.fragment.PipeWorkFragment;
import com.barisetech.www.workmanage.view.fragment.PipeWorkModifyFragment;
import com.barisetech.www.workmanage.view.fragment.SiteDetailFragment;
import com.barisetech.www.workmanage.view.fragment.SiteFragment;
import com.barisetech.www.workmanage.view.fragment.WaveFormFragment;

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
                            .replace(R.id.second_framelayout, IncidentListFragment.newInstance(), tag)
                            .commit();
                    break;
                case IncidentDetailsFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, IncidentDetailsFragment.newInstance((IncidentInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
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
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case AlarmAnalysisFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, AlarmAnalysisFragment.newInstance((AlarmInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case AlarmAnalysisListFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, AlarmAnalysisListFragment.newInstance(), tag).commit();
                    break;
                case AlarmAnalysisDetailFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, AlarmAnalysisDetailFragment.newInstance((AlarmAnalysis)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
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
                            .replace(R.id.second_framelayout, NewsDetailsFragment.newInstance((String) eventBusMessage
                                    .getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case NewsAddFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, NewsAddFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case SiteFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, SiteFragment.newInstance(), tag)
                            .commit();
                    break;
                case AddSiteFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, AddSiteFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case DigitizingFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, DigitizingFragment.newInstance(), tag)
                            .commit();
                    break;
                case DigitizingDetailFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, DigitizingDetailFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeFragment.newInstance(), tag).commit();
                    break;
                case PipeDetailFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeDetailFragment.newInstance((PipeInfo) eventBusMessage
                                    .getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeAddFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeAddFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeModifyFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeModifyFragment.newInstance((PipeInfo) eventBusMessage
                                    .getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeCollectionFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeCollectionFragment.newInstance(), tag).commit();
                    break;
                case PipeCollectionDetailFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeCollectionDetailFragment.newInstance((PipeCollections)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeCollectionModifyFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeCollectionModifyFragment.newInstance(
                                    (PipeCollections) eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeCollectionAddFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeCollectionAddFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeWorkFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeWorkFragment.newInstance(), tag).commit();
                    break;
                case PipeWorkDetailFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeWorkDetailFragment.newInstance((PipeWork)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeWorkModifyFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeWorkModifyFragment.newInstance((PipeWork)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeWorkAddFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeWorkAddFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case WaveFormFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, WaveFormFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case SiteDetailFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, SiteDetailFragment.newInstance((SiteBean)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;

                case ModifySiteFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, ModifySiteFragment.newInstance((SiteBean)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeLindAreaFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeLindAreaFragment.newInstance(), tag).commit();
                    break;

                case PipeLindAreaAddFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeLindAreaAddFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeLindAreaDetailFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeLindAreaDetailFragment.newInstance((PipeLindAreaInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;

                case PipeLindAreaModifyFragment.TAG:
                    transaction
                            .replace(R.id.second_framelayout, PipeLindAreaModifyFragment.newInstance((PipeLindAreaInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
            }
        }
    }
}
