package com.barisetech.www.workmanage.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmListFragment extends Fragment {
    public static final String TAG = "AlarmListFragment";

    public AlarmListFragment() {
        // Required empty public constructor
    }

    public static AlarmListFragment newInstance() {
        AlarmListFragment fragment = new AlarmListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarm_list, container, false);
        return root;
    }

}
