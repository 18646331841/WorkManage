package com.barisetech.www.workmanage.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentAlarmDetailsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmDetailsFragment extends BaseFragment {
    public static final String TAG = "AlarmDetailsFragment";

    private FragmentAlarmDetailsBinding mBinding;

    public AlarmDetailsFragment() {
        // Required empty public constructor
    }

    public static AlarmDetailsFragment newInstance() {
        AlarmDetailsFragment fragment = new AlarmDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_details, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_alarm_details));
        observableToolbar.set(toolbarInfo);

        return mBinding.getRoot();
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
