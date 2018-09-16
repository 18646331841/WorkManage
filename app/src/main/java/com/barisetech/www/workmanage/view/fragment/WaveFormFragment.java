package com.barisetech.www.workmanage.view.fragment;

import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.DataRateBean;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentWaveFormBinding;
import com.barisetech.www.workmanage.utils.ChartUtil;

import java.util.ArrayList;
import java.util.List;

public class WaveFormFragment extends BaseFragment{

    public static final String TAG = "WaveFormFragment";
    FragmentWaveFormBinding mBinding;

    private List<DataRateBean> list = new ArrayList<>();

    public static WaveFormFragment newInstance() {
        WaveFormFragment fragment = new WaveFormFragment();
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wave_form, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.waveform));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        for (int i= 0; i <600; i++){
            list.add(new DataRateBean((float)(100*Math.random()),i +""));
        }
        ChartUtil.setChartViewData(list,mBinding.chart);

    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
