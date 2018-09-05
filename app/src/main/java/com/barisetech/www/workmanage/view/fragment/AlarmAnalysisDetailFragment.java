package com.barisetech.www.workmanage.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmAnalysisDetailFragment extends BaseFragment {
    public static final String TAG = "NewsDetailsFragment";

    private static final String ARG_ANALYSIS = "analysis";

    public AlarmAnalysisDetailFragment() {
        // Required empty public constructor
    }

    public static AlarmAnalysisDetailFragment newInstance(String url) {
        AlarmAnalysisDetailFragment fragment = new AlarmAnalysisDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ANALYSIS, url);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
//            curUrl = getArguments().getString(ARG_ANALYSIS, "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_analysis_detail, container, false);
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
