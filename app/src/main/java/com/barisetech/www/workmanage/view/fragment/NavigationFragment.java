package com.barisetech.www.workmanage.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BottomNavigationViewHelper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.view.fragment.my.MyFragment;

import org.greenrobot.eventbus.EventBus;

public class NavigationFragment extends Fragment {
    public static final String TAG = "NavigationFragment";

    private FragmentManager fm;
    private static final String CUR_TAG = "curTag";
    private BottomNavigationView navigation;

    private boolean fromOtherView = false;
    private String curFragment = "";
    private String auth;
    private TextView authCount;

    public NavigationFragment() {
        // Required empty public constructor
    }

    public static NavigationFragment newInstance(EventBusMessage tag) {
        NavigationFragment fragment = new NavigationFragment();
        if (null != tag) {
            LogUtil.d(TAG, "newInstance = " + tag.message);
            Bundle bundle = new Bundle();
            bundle.putSerializable(CUR_TAG, tag);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_navigation, container, false);
        initView(root);

        Bundle bundle = getArguments();
        if (null != bundle) {
            EventBusMessage eventBusMessage = (EventBusMessage) bundle.getSerializable(CUR_TAG);
            if(eventBusMessage != null){
                fromOtherView = true;
                showNavContentFragment(eventBusMessage);

                if (eventBusMessage.getArg2() instanceof String) {
                    //授权通知信息
                    auth = (String) eventBusMessage.getArg2();
                    if (!TextUtils.isEmpty(auth)) {
                        String[] count = auth.split(",");
                        authCount.setText(count.length);
                        authCount.setVisibility(View.VISIBLE);
                    }
                }
                return root;
            }
        }

        showNavContentFragment(new EventBusMessage(Messagefragment.TAG));

        return root;
    }

    private void clearCount() {
        auth = "";
        authCount.setText("");
        authCount.setVisibility(View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view) {
        navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        BottomNavigationItemView my = (BottomNavigationItemView) menuView.getChildAt(3);

        View badge = LayoutInflater.from(getActivity()).inflate(R.layout.menu_badge, menuView, false);
        my.addView(badge);
        authCount = (TextView) badge.findViewById(R.id.tv_msg_count);
        authCount.setVisibility(View.GONE);
        BottomNavigationViewHelper.disableShiftMode(navigation);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        if (fromOtherView) {
            fromOtherView = false;
            return true;
        }
        LogUtil.d(TAG, "item = " + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.navigation_message:
                        showNavContentFragment(new EventBusMessage(Messagefragment.TAG));
                        return true;
                    case R.id.navigation_map:
                        if (BaseApplication.getInstance().isTwoPanel) {
                            showNavContentFragment(new EventBusMessage(PadMapListFragment.TAG));
                        } else {
                            showNavContentFragment(new EventBusMessage(MapFragment.TAG));
                        }
                        return true;
                    case R.id.navigation_manage:
                        showNavContentFragment(new EventBusMessage(ManageFragment.TAG));
                        return true;
                    case R.id.navigation_myself:
                        EventBusMessage my = new EventBusMessage(MyFragment.TAG);
                        my.setArg1(auth);
                        showNavContentFragment(my);
                        clearCount();
                        return true;
                }
                return false;
            };

    private void showItem(int id) {
        if (!fromOtherView) {
            return;
        }
        switch (id) {
            case R.id.navigation_message:
                navigation.setSelectedItemId(navigation.getMenu().getItem(0).getItemId());
                break;
            case R.id.navigation_map:
                LogUtil.d(TAG, "map is selected");
                navigation.setSelectedItemId(navigation.getMenu().getItem(1).getItemId());

                break;
            case R.id.navigation_manage:
                navigation.setSelectedItemId(navigation.getMenu().getItem(2).getItemId());

                break;
            case R.id.navigation_myself:
                navigation.setSelectedItemId(navigation.getMenu().getItem(3).getItemId());
                break;
        }
    }

    /**
     * 显示内容中的fragment
     * @param eventBusMessage
     */
    @SuppressLint("RestrictedApi")
    public void showNavContentFragment(EventBusMessage eventBusMessage) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        String tag = eventBusMessage.message;
        if (TextUtils.isEmpty(tag) || curFragment.equals(tag)) {
            return;
        }
        LogUtil.d(TAG, "show = " + tag);
        switch (tag) {
            case Messagefragment.TAG:
                showItem(R.id.navigation_message);
                transaction.replace(R.id.navigation_content, Messagefragment.newInstance(), tag)
                        .commit();
                break;
            case MapFragment.TAG:
                showItem(R.id.navigation_map);
                if (eventBusMessage.getArg1() != null) {
                    transaction.replace(R.id.navigation_content, MapFragment.newInstance((String) eventBusMessage
                            .getArg1()), tag)
                            .commit();
                } else {
                    transaction.replace(R.id.navigation_content, MapFragment.newInstance(null), tag)
                            .commit();
                }
                break;
            case PadMapListFragment.TAG:
                showItem(R.id.navigation_map);
                if (eventBusMessage.getArg1() != null) {
                    transaction.replace(R.id.navigation_content, PadMapListFragment.newInstance((String) eventBusMessage
                            .getArg1()), tag)
                            .commit();
                } else {
                    transaction.replace(R.id.navigation_content, PadMapListFragment.newInstance(null), tag)
                            .commit();
                }
                break;
            case ManageFragment.TAG:
                showItem(R.id.navigation_manage);
                transaction.replace(R.id.navigation_content, ManageFragment.newInstance(), tag)
                        .commit();
                break;
            case MyFragment.TAG:
                showItem(R.id.navigation_manage);
                if (eventBusMessage.getArg1() instanceof String) {
                    transaction.replace(R.id.navigation_content, MyFragment.newInstance((String) eventBusMessage
                            .getArg1()), tag)
                            .commit();
                } else {
                    transaction.replace(R.id.navigation_content, MyFragment.newInstance(null), tag)
                            .commit();
                }
                break;
        }
        curFragment = tag;
        if (BaseApplication.getInstance().isTwoPanel) {
            EventBusMessage eventBusMessage1 = new EventBusMessage(NullFragment.TAG);
            EventBus.getDefault().post(eventBusMessage1);
        }
    }
}
