package com.barisetech.www.workmanage.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BottomNavigationViewHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NavigationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends Fragment {
    public static final String TAG = "NavigationFragment";

    private OnFragmentInteractionListener mListener;
    private FragmentManager fm;

    public NavigationFragment() {
        // Required empty public constructor
    }

    public static NavigationFragment newInstance() {
        NavigationFragment fragment = new NavigationFragment();
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

        showNavContentFragment(Messagefragment.TAG);

        return root;
    }

    private void initView(View view) {
        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_message:
                    showNavContentFragment(Messagefragment.TAG);
                    return true;
                case R.id.navigation_map:
                    showNavContentFragment(MapFragment.TAG);
                    return true;
                case R.id.navigation_manage:
                    return true;
                case R.id.navigation_myself:
                    return true;
            }
            return false;
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

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
        }
        if (null != fragment) {
            fm.beginTransaction()
                    .replace(R.id.navigation_content, fragment, tag)
                    .commit();
        }
    }
}
