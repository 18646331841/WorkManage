package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.DataRateBean;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.bean.wave.ReqWave;
import com.barisetech.www.workmanage.databinding.FragmentWaveFormBinding;
import com.barisetech.www.workmanage.utils.ChartUtil;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;
import com.barisetech.www.workmanage.viewmodel.WaveViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaveFormFragment extends BaseFragment {

    public static final String TAG = "WaveFormFragment";
    FragmentWaveFormBinding mBinding;

//    private List<DataRateBean> list = new ArrayList<>();
    private Map<String, List<DataRateBean>> startMapData;
    private Map<String, List<DataRateBean>> endMapData;
    private WaveViewModel waveViewModel;
    private int curType = 1;
    private PipeInfo curPipeInfo;

    private int curPipeId;
    private static final String PIPE_ID = "pipeId";
    private PipeViewModel pipeViewModel;

    public static WaveFormFragment newInstance(int pipeId) {
        WaveFormFragment fragment = new WaveFormFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PIPE_ID, pipeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMapData = new HashMap<>();
        endMapData = new HashMap<>();
        if (null != getArguments()) {
            curPipeId = getArguments().getInt(PIPE_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
//        if (!BaseApplication.getInstance().isTwoPanel) {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wave_form, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_waveform));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        List<DataRateBean> one = new ArrayList<>();
        List<DataRateBean> two = new ArrayList<>();

        for (int i = 0; i < 600; i++) {
            one.add(new DataRateBean((float) (100 * Math.random()), i + ""));
        }
        for (int i = 0; i < 600; i++) {
            double value = i % 2 == 0 ? (-100 * Math.random()) : (100 * Math.random());
            two.add(new DataRateBean((float) value, i + ""));
        }
        startMapData.put("one", one);
        startMapData.put("two", two);

        ChartUtil.setChartViewData(startMapData, mBinding.chartStartSite);
    }

    @Override
    public void bindViewModel() {
        waveViewModel = ViewModelProviders.of(this).get(WaveViewModel.class);
        pipeViewModel = ViewModelProviders.of(this).get(PipeViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeViewModel.getmObservableAllPipe().hasObservers()) {
            pipeViewModel.getmObservableAllPipe().observe(this, pipeInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (pipeInfos != null && pipeInfos.size() > 0) {
                        curPipeInfo = pipeInfos.get(0);
                        int startSiteId = curPipeInfo.StartSiteId;
                        int endSiteId;
                        List<SiteBean> sites = curPipeInfo.Sites;
                        for (SiteBean siteBean : sites) {
                            if (siteBean.SiteId != startSiteId) {
                                endSiteId = siteBean.SiteId;
                            }
                        }

                    }
                }
            });
        }

        if (!waveViewModel.getmObservableWave().hasObservers()) {
            waveViewModel.getmObservableWave().observe(this, waveBean -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (waveBean != null) {
                        //TODO
                    }
                }
            });
        }

        if (curPipeInfo == null) {
            getPipe();
        }
    }

    private void getPipe() {
        ReqAllPipe reqAllPipe = new ReqAllPipe();
        reqAllPipe.setPipeId(String.valueOf(curPipeId));
        reqAllPipe.setStartIndex("0");
        reqAllPipe.setNumberOfRecords("1");

        pipeViewModel.reqAllPipe(reqAllPipe);
    }

    private void getData() {
        ReqWave reqWave = new ReqWave();
        reqWave.sensorType = String.valueOf(curType);
        reqWave.startTime = "2018-09-04 00:00:00";
        reqWave.endTime = "2018-09-04 00:00:30";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (!BaseApplication.getInstance().isTwoPanel) {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }
}
