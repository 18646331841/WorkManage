package com.barisetech.www.workmanage.view.fragment;

import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BottomNavigationViewHelper;

public class NavigationFragment extends Fragment {
    public static final String TAG = "NavigationFragment";

    private FragmentManager fm;
    private static final String CUR_TAG = "curTag";
    private BottomNavigationView navigation;

    public NavigationFragment() {
        // Required empty public constructor
    }

    public static NavigationFragment newInstance(String tag) {
        NavigationFragment fragment = new NavigationFragment();
        if (null != tag) {
            Bundle bundle = new Bundle();
            bundle.putString(CUR_TAG, tag);
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
            String tag = bundle.getString(CUR_TAG);
            if( !TextUtils.isEmpty(tag)){
                navigation.setSelectedItemId(R.id.navigation_map);
                showNavContentFragment(tag);
                return root;
            }
        }

        showNavContentFragment(Messagefragment.TAG);

        return root;
    }

    private void initView(View view) {
        navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        View tab = menuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
        View badge = LayoutInflater.from(getActivity()).inflate(R.layout.menu_badge, menuView, false);
        itemView.addView(badge);
        TextView count = (TextView) badge.findViewById(R.id.tv_msg_count);
        count.setText(String.valueOf(1));
        BottomNavigationViewHelper.disableShiftMode(navigation);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_message:
                        showNavContentFragment(Messagefragment.TAG);
                        return true;
                    case R.id.navigation_map:
                        showNavContentFragment(MapFragment.TAG);
                        return true;
                    case R.id.navigation_manage:
                        showNavContentFragment(ManageFragment.TAG);
                        return true;
                    case R.id.navigation_myself:
                        return true;
                }
                return false;
            };

    /**
     * 显示内容中的fragment
     * @param tag fragment's TAG
     */
    private void showNavContentFragment(String tag) {
        Fragment fragment = fm.findFragmentByTag(tag);
        switch (tag) {
            case Messagefragment.TAG:
                if (null == fragment) {
                    fragment = Messagefragment.newInstance();
                }
                break;

            case MapFragment.TAG:
                if (null == fragment) {
                    fragment = MapFragment.newInstance();
                }
                break;
            case ManageFragment.TAG:
                if (null == fragment) {
                    fragment = ManageFragment.newInstance();
                }
                break;
        }
        if (null != fragment) {
            fm.beginTransaction()
                    .replace(R.id.navigation_content, fragment, tag)
                    .commit();
        }
    }
}
