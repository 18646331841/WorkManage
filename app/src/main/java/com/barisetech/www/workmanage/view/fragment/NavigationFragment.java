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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BottomNavigationViewHelper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.utils.LogUtil;

public class NavigationFragment extends Fragment {
    public static final String TAG = "NavigationFragment";

    private FragmentManager fm;
    private static final String CUR_TAG = "curTag";
    private BottomNavigationView navigation;
    private BottomNavigationItemView message;
    private BottomNavigationItemView map;
    private BottomNavigationItemView manage;
    private BottomNavigationItemView my;

    private boolean fromOtherView = false;

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
                return root;
            }
        }

        showNavContentFragment(new EventBusMessage(Messagefragment.TAG));

        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view) {
        navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        message = (BottomNavigationItemView) menuView.getChildAt(0);
        map = (BottomNavigationItemView) menuView.getChildAt(1);
        manage = (BottomNavigationItemView) menuView.getChildAt(2);
        my = (BottomNavigationItemView) menuView.getChildAt(3);

        View badge = LayoutInflater.from(getActivity()).inflate(R.layout.menu_badge, menuView, false);
        manage.addView(badge);
        TextView count = (TextView) badge.findViewById(R.id.tv_msg_count);
        count.setText(String.valueOf(1));
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
                        showNavContentFragment(new EventBusMessage(MapFragment.TAG));
                        return true;
                    case R.id.navigation_manage:
                        showNavContentFragment(new EventBusMessage(ManageFragment.TAG));
                        return true;
                    case R.id.navigation_myself:
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
    private void showNavContentFragment(EventBusMessage eventBusMessage) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        String tag = eventBusMessage.message;
        if (TextUtils.isEmpty(tag)) {
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
            case ManageFragment.TAG:
                showItem(R.id.navigation_manage);
                transaction.replace(R.id.navigation_content, ManageFragment.newInstance(), tag)
                        .commit();
                break;
        }
    }
}
