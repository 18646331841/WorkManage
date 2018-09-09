package com.barisetech.www.workmanage.view;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseActivity;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.RawBean;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipework.PipeWork;
import com.barisetech.www.workmanage.bean.site.SiteBean;
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
import com.barisetech.www.workmanage.view.fragment.NewsAddFragment;
import com.barisetech.www.workmanage.view.fragment.NewsDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.NewsListFragment;
import com.barisetech.www.workmanage.view.fragment.PipeCollectionAddFragment;
import com.barisetech.www.workmanage.view.fragment.PipeCollectionDetailFragment;
import com.barisetech.www.workmanage.view.fragment.PipeCollectionFragment;
import com.barisetech.www.workmanage.view.fragment.PipeFragment;
import com.barisetech.www.workmanage.view.fragment.PipeWorkFragment;
import com.barisetech.www.workmanage.view.fragment.SiteDetailFragment;
import com.barisetech.www.workmanage.view.fragment.SiteFragment;
import com.barisetech.www.workmanage.view.fragment.WaveFormFragment;

import org.greenrobot.eventbus.EventBus;

public class SecondActivity extends BaseActivity {


    private Point point = new Point();

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
                showActivityOrFragment(eventBusMessage, true);
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        point.x= (int) ev.getRawX();
        point.y= (int) ev.getRawY();
        EventBus.getDefault().post(point);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void showActivityOrFragment(EventBusMessage eventBusMessage, boolean isActivity) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String tag = eventBusMessage.message;
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
        }
    }
}
