package com.barisetech.www.workmanage.view;

import android.content.Intent;
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
import com.barisetech.www.workmanage.bean.UserInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.digitalizer.DigitalizerBean;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipelindarea.PipeLindAreaInfo;
import com.barisetech.www.workmanage.bean.pipework.PipeWork;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.bean.workplan.PlanBean;
import com.barisetech.www.workmanage.bean.workplan.ReqAddPlan;
import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.view.fragment.DigitizingModifyFragment;
import com.barisetech.www.workmanage.view.fragment.PlanListFragment;
import com.barisetech.www.workmanage.view.fragment.SignInFragment;
import com.barisetech.www.workmanage.view.fragment.my.AboutFragment;
import com.barisetech.www.workmanage.view.fragment.AddSiteFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmAnalysisDetailFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmAnalysisFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmAnalysisListFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;
import com.barisetech.www.workmanage.view.fragment.DigitizingDetailFragment;
import com.barisetech.www.workmanage.view.fragment.DigitizingFragment;
import com.barisetech.www.workmanage.view.fragment.IncidentDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.IncidentListFragment;
import com.barisetech.www.workmanage.view.fragment.MapFragment;
import com.barisetech.www.workmanage.view.fragment.Messagefragment;
import com.barisetech.www.workmanage.view.fragment.ModifySiteFragment;
import com.barisetech.www.workmanage.view.fragment.my.AuthDetailFragment;
import com.barisetech.www.workmanage.view.fragment.my.AuthListFragment;
import com.barisetech.www.workmanage.view.fragment.my.ContactDetailFragment;
import com.barisetech.www.workmanage.view.fragment.my.ContactsFragment;
import com.barisetech.www.workmanage.view.fragment.my.EventTypeFragment;
import com.barisetech.www.workmanage.view.fragment.my.ModifyEmailFragment;
import com.barisetech.www.workmanage.view.fragment.my.ModifyPhoneFragment;
import com.barisetech.www.workmanage.view.fragment.my.MyInfoFragment;
import com.barisetech.www.workmanage.view.fragment.NavigationFragment;
import com.barisetech.www.workmanage.view.fragment.NewsAddFragment;
import com.barisetech.www.workmanage.view.fragment.NewsDetailsFragment;
import com.barisetech.www.workmanage.view.fragment.NewsListFragment;
import com.barisetech.www.workmanage.view.fragment.NullFragment;
import com.barisetech.www.workmanage.view.fragment.PadMapFragment;
import com.barisetech.www.workmanage.view.fragment.PadMapListFragment;
import com.barisetech.www.workmanage.view.fragment.my.NotDisturbFragment;
import com.barisetech.www.workmanage.view.fragment.my.PassWordFragment;
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
import com.barisetech.www.workmanage.view.fragment.my.SoundFragment;
import com.barisetech.www.workmanage.view.fragment.WaveFormFragment;
import com.barisetech.www.workmanage.view.fragment.workplan.FirstPublishFragment;
import com.barisetech.www.workmanage.view.fragment.workplan.SecondPublishFragment;
import com.barisetech.www.workmanage.view.fragment.workplan.ThirdPublishFragment;
import com.barisetech.www.workmanage.view.fragment.worktask.TaskListFragment;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends BaseActivity {
    public static final String TAG = "MainActivity";

    /**
     * 是否为大屏左右显示，true表示是
     */
    private boolean isTwoPanel = false;
    public Point point = new Point();
    private NavigationFragment navigationFragment;

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
            BaseApplication.getInstance().isTwoPanel = true;
            isTwoPanel = true;
        }

        Bundle bundle = getIntent().getExtras();
        String tag = Messagefragment.TAG;
        String arg1 = null;
        String arg2 = null;
        String notify = null;
        if (null != bundle) {
            String tag1 = bundle.getString("tag");
            if (!TextUtils.isEmpty(tag1)) {
                tag = tag1;
            }
            arg1 = bundle.getString("arg1");
            arg2 = bundle.getString("arg2");
            notify = bundle.getString(BaseConstant.NOTIFY_TAG);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (!isTwoPanel) {
//            //小屏设备界面，单个fragment
//            transaction.add(R.id.fragment_navigation, NavigationFragment.newInstance(), NavigationFragment.TAG)
// .commit();
//        } else {
//            //大屏设备界面，左右两个fragment
        EventBusMessage eventBusMessage = new EventBusMessage(tag);
        eventBusMessage.setArg1(arg1);
        eventBusMessage.setArg2(arg2);
        LogUtil.d(TAG, "tag = " + tag + " arg1 = " + arg1);
        navigationFragment = NavigationFragment.newInstance(eventBusMessage);
        transaction.add(R.id.fragment_navigation, navigationFragment, NavigationFragment.TAG);
        transaction.commit();
//        }
        if (!TextUtils.isEmpty(notify)) {
            LogUtil.d(TAG, "onCreate---" + notify);
            if (notify.equals(AuthListFragment.TAG)) {
                return;
            }
            showActivityOrFragment(new EventBusMessage(notify), true);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        String tag = Messagefragment.TAG;
        String arg1 = null;
        String arg2 = null;
        String notify = null;
        if (null != bundle) {
            String tag1 = bundle.getString("tag");
            if (!TextUtils.isEmpty(tag1)) {
                tag = tag1;
            }
            arg1 = bundle.getString("arg1");
            arg2 = bundle.getString("arg2");
            notify = bundle.getString(BaseConstant.NOTIFY_TAG);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        EventBusMessage eventBusMessage = new EventBusMessage(tag);
        eventBusMessage.setArg1(arg1);
        eventBusMessage.setArg2(arg2);
        LogUtil.d(TAG, "tag = " + tag + " arg1 = " + arg1 + " arg2 = " + arg2);
        navigationFragment = NavigationFragment.newInstance(eventBusMessage);
        transaction.replace(R.id.fragment_navigation, navigationFragment, NavigationFragment.TAG);
        transaction.commit();

        if (!TextUtils.isEmpty(notify)) {
            LogUtil.d(TAG, "onNewIntent---" + notify);
            if (notify.equals(AuthListFragment.TAG)) {
                return;
            }
            showActivityOrFragment(new EventBusMessage(notify), true);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
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
                    transaction.add(R.id.fragment_navigation, NavigationFragment.newInstance(eventBusMessage),
                            NavigationFragment
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
                            .replace(R.id.fragment_content, IncidentListFragment.newInstance(), tag)
                            .commit();
                    break;
                case IncidentDetailsFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, IncidentDetailsFragment.newInstance((IncidentInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case AlarmListFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, AlarmListFragment.newInstance(), tag)
                            .commit();
                    break;
                case AlarmDetailsFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, AlarmDetailsFragment.newInstance((AlarmInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case AlarmAnalysisFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, AlarmAnalysisFragment.newInstance((AlarmInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case AlarmAnalysisListFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, AlarmAnalysisListFragment.newInstance(), tag).commit();
                    break;
                case AlarmAnalysisDetailFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, AlarmAnalysisDetailFragment.newInstance((AlarmAnalysis)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case MapFragment.TAG:
                    EventBusMessage eventBusMessage1 = new EventBusMessage(PadMapListFragment.TAG);
                    eventBusMessage1.setArg1(eventBusMessage.getArg1());
                    transaction.replace(R.id.fragment_navigation, NavigationFragment.newInstance(eventBusMessage1),
                            NavigationFragment
                            .TAG).commit();
                    break;
                case PadMapFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PadMapFragment.newInstance((String) eventBusMessage
                                    .getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case NullFragment.TAG:
                    transaction.replace(R.id.fragment_content, NullFragment.newInstance(), tag).commit();
                    break;
                case NewsListFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, NewsListFragment.newInstance(), tag).commit();
                    break;
                case NewsDetailsFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, NewsDetailsFragment.newInstance((String) eventBusMessage
                                    .getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case NewsAddFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, NewsAddFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case SiteFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, SiteFragment.newInstance(), tag)
                            .commit();
                    break;
                case AddSiteFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, AddSiteFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case DigitizingFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, DigitizingFragment.newInstance(), tag)
                            .commit();
                    break;
                case DigitizingDetailFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, DigitizingDetailFragment.newInstance((DigitalizerBean)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case DigitizingModifyFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, DigitizingModifyFragment.newInstance((DigitalizerBean)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeFragment.newInstance(), tag).commit();
                    break;
                case PipeDetailFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeDetailFragment.newInstance((PipeInfo) eventBusMessage
                                    .getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeAddFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeAddFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeModifyFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeModifyFragment.newInstance((PipeInfo) eventBusMessage
                                    .getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeCollectionFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeCollectionFragment.newInstance(), tag).commit();
                    break;
                case PipeCollectionDetailFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeCollectionDetailFragment.newInstance((PipeCollections)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeCollectionModifyFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeCollectionModifyFragment.newInstance(
                                    (PipeCollections) eventBusMessage.getArg1()), tag).commit();
                    break;
                case PipeCollectionAddFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeCollectionAddFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeWorkFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeWorkFragment.newInstance(), tag).commit();
                    break;
                case PipeWorkDetailFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeWorkDetailFragment.newInstance((PipeWork)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeWorkModifyFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeWorkModifyFragment.newInstance((PipeWork)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeWorkAddFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeWorkAddFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case WaveFormFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, WaveFormFragment.newInstance((int) eventBusMessage
                                    .getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case SiteDetailFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, SiteDetailFragment.newInstance((SiteBean)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;

                case ModifySiteFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, ModifySiteFragment.newInstance((SiteBean)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeLindAreaFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeLindAreaFragment.newInstance(), tag).commit();
                    break;

                case PipeLindAreaAddFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeLindAreaAddFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case PipeLindAreaDetailFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeLindAreaDetailFragment.newInstance((PipeLindAreaInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;

                case PipeLindAreaModifyFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PipeLindAreaModifyFragment.newInstance((PipeLindAreaInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;

                case PlanListFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PlanListFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case FirstPublishFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, FirstPublishFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case SecondPublishFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, SecondPublishFragment.newInstance((ReqAddPlan)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case ThirdPublishFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, ThirdPublishFragment.newInstance((ReqAddPlan)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case TaskListFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, TaskListFragment.newInstance((PlanBean)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case SignInFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, SignInFragment.newInstance((TaskSiteBean)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;

                case PassWordFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, PassWordFragment.newInstance(), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;

                case SoundFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, SoundFragment.newInstance(), tag).commit();
                    break;

                case AboutFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, AboutFragment.newInstance(), tag).commit();
                    break;

                case MyInfoFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, MyInfoFragment.newInstance(), tag).commit();
                    break;
                case ModifyPhoneFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, ModifyPhoneFragment.newInstance((UserInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;

                case ModifyEmailFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, ModifyEmailFragment.newInstance((UserInfo)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case EventTypeFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, EventTypeFragment.newInstance(), tag).commit();
                    break;
                case NotDisturbFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, NotDisturbFragment.newInstance(), tag).commit();
                    break;
                case ContactsFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, ContactsFragment.newInstance(), tag).commit();
                    break;
                case AuthListFragment.TAG:
                    if (eventBusMessage.getArg1() instanceof String) {
                        transaction
                                .replace(R.id.fragment_content, AuthListFragment.newInstance((String) eventBusMessage
                                        .getArg1()), tag).commit();
                    } else {
                        transaction
                                .replace(R.id.fragment_content, AuthListFragment.newInstance(null), tag).commit();
                    }
                    break;
                case AuthDetailFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, AuthDetailFragment.newInstance((String) eventBusMessage
                                    .getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
                case ContactDetailFragment.TAG:
                    transaction
                            .replace(R.id.fragment_content, ContactDetailFragment.newInstance((ContactsBean)
                                    eventBusMessage.getArg1()), tag).commit();
                    if (!isActivity) {
                        transaction.addToBackStack(tag);
                    }
                    break;
            }
        }
    }
}
