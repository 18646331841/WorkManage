package com.barisetech.www.workmanage.view.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.DataRateBean;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.bean.wave.ReqWave;
import com.barisetech.www.workmanage.bean.wave.WaveBean;
import com.barisetech.www.workmanage.databinding.FragmentWaveFormBinding;
import com.barisetech.www.workmanage.utils.ChartUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.NetworkUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;
import com.barisetech.www.workmanage.viewmodel.WaveViewModel;
import com.barisetech.www.workmanage.widget.CustomDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;

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
    private int startSiteId = -1;
    private int endSiteId = -1;
    private Disposable curDisposable;

    private CustomDialog.Builder builder;
    private CustomDialog mDialog;
    private DatePickerDialog startDatePicker;
    private TimePickerDialog startTimePicker;
    private String startT = "";

    private int timeArea = 60;//默认是1分钟
    /**
     * 每秒显示的点数
     */
    private int prePoint = 20;

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

        builder = new CustomDialog.Builder(getContext());

        initView();
        return mBinding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        startDatePicker = TimeUtil.getDatePicker(getActivity(), onStartDateSetListener);
        startTimePicker = TimeUtil.getTimePicker(getActivity(), onStartTimeSetListener);
        startT = TimeUtil.ms2Date(System.currentTimeMillis());
        mBinding.waveFormStartTime.setText(startT);

        mBinding.waveFormStartTime.setOnClickListener(view -> {
            //显示开始日期选择器
            startDatePicker.show();
        });

        mBinding.waveFormConfirm.setOnClickListener(view -> {
            if (TextUtils.isEmpty(startT)) {
                ToastUtil.showToast("请选择开始时间");
                return;
            }

//            timeArea = 180;

            closeDisposable();
            if (curPipeInfo == null) {
                getPipe();
            } else {
                if (curType == 1) {
                    getSubsonicDatas();
                } else {
                    getDatas();
                }
            }
        });

        mBinding.waveFormInputTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    int num = Integer.valueOf(charSequence.toString().trim());
                    if (num > 0 && num <= 180) {
                        timeArea = num;
                    } else {
                        mBinding.waveFormInputTime.setText("180");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBinding.chartStartSite.setOnTouchListener((view, motionEvent) -> {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBinding.waveFormScrollview.setScrollDisable(true);
                    break;
                case MotionEvent.ACTION_UP:
                    mBinding.waveFormScrollview.setScrollDisable(false);
                    break;
            }

            return false;
        });

        mBinding.chartEndSite.setOnTouchListener((view, motionEvent) -> {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBinding.waveFormScrollview.setScrollDisable(true);
                    break;
                case MotionEvent.ACTION_UP:
                    mBinding.waveFormScrollview.setScrollDisable(false);
                    break;
            }

            return false;
        });

        mBinding.flow.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                mBinding.waveFormInfo.setVisibility(View.GONE);
                closeDisposable();
                clearWaveLines();
                curType = 4;
                getDatas();
            }
        });
        mBinding.pressure.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                mBinding.waveFormInfo.setVisibility(View.GONE);
                closeDisposable();
                clearWaveLines();
                curType = 3;
                getDatas();
            }
        });
        mBinding.subsonicWave.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                mBinding.waveFormInfo.setVisibility(View.VISIBLE);
                closeDisposable();
                clearWaveLines();
                curType = 1;
                getSubsonicDatas();
            }
        });

        mBinding.chartStartSite.setViewportChangeListener(viewport -> {
            LogUtil.d(TAG, "onViewportChanged");
            EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        });
        mBinding.chartEndSite.setViewportChangeListener(viewport -> {
            EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        });
    }

    private void showTwoButtonDialog(String alertText, int ImgId,String title,String confirmText, String cancelText, View.OnClickListener conFirmListener, View.OnClickListener cancelListener) {
        mDialog = builder.setMessage(alertText)
                .setImagView(ImgId)
                .setTitle(title)
                .setPositiveButton(confirmText, conFirmListener)
                .setNegativeButton(cancelText, cancelListener)
                .createTwoButtonDialog();
        mDialog.show();
    }

    /**
     * 开始日期选择回调
     */
    private DatePickerDialog.OnDateSetListener onStartDateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
        String monthS = String.valueOf(monthOfYear + 1);
        String dayS = String.valueOf(dayOfMonth);
        if (monthS.length() == 1) {
            monthS = "0" + monthS;
        }
        if (dayS.length() == 1) {
            dayS = "0" + dayS;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-")
                .append(monthS).append("-")
                .append(dayS).append(" ");
        startT = sb.toString();
        startTimePicker.show();
    };

    /**
     * 开始时间选择回调
     */
    private TimePickerDialog.OnTimeSetListener onStartTimeSetListener = ((timePicker, hour, minute) -> {
        String hourS = String.valueOf(hour);
        String minuteS = String.valueOf(minute);
        if (hourS.length() == 1) {
            hourS = "0" + hourS;
        }
        if (minuteS.length() == 1) {
            minuteS = "0" + minuteS;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(startT)
                .append(hourS).append(":")
                .append(minuteS).append(":00");
        startT = sb.toString();
        mBinding.waveFormStartTime.setText(startT);
    });

    private void hideWaveView() {
        mBinding.chartStartSite.setVisibility(View.GONE);
        mBinding.chartEndSite.setVisibility(View.GONE);
    }

    private void showWaveView() {
        mBinding.chartStartSite.setVisibility(View.VISIBLE);
        mBinding.chartEndSite.setVisibility(View.VISIBLE);
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
                        startSiteId = curPipeInfo.StartSiteId;
                        List<SiteBean> sites = curPipeInfo.Sites;
                        for (SiteBean siteBean : sites) {
                            if (siteBean.SiteId != startSiteId) {
                                endSiteId = siteBean.SiteId;
                            }
                        }
                        getSubsonicDatas();
                    } else {
                        ToastUtil.showToast("未查找到管线");
                    }
                }
            });
        }

        if (!waveViewModel.getmObservableWaves().hasObservers()) {
            waveViewModel.getmObservableWaves().observe(this, waveBeans -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (waveBeans != null && waveBeans.size() > 0) {
                        setWaveData(waveBeans);
                    } else {
                        ToastUtil.showToast("未查找到波形");
                    }
                }
            });
        }

        if (curPipeInfo == null) {
            if (!NetworkUtil.isWifi(getContext())) {
                showTwoButtonDialog("正在使用移动流量，查看波形图可能消耗较多流量，是否继续", 0, null, "继续", "退出", v -> {
                    mDialog.dismiss();
                    getPipe();
                }, v -> {
                    mDialog.dismiss();
                    getActivity().onBackPressed();
                });
            } else {
                getPipe();
            }
        }
    }

    private void getPipe() {
        ReqAllPipe reqAllPipe = new ReqAllPipe();
        reqAllPipe.setPipeId(String.valueOf(curPipeId));
        reqAllPipe.setStartIndex("0");
        reqAllPipe.setNumberOfRecords("0");

        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
        pipeViewModel.reqAllPipe(reqAllPipe);
    }

    private void setWaveData(List<WaveBean> waveBeans) {
        for (WaveBean waveBean : waveBeans) {
            if (waveBean.Points != null && waveBean.Points.size() > 0) {
                String key = waveBean.siteId + "_" + waveBean.type;
                List<DataRateBean> dataRateBeans = new ArrayList<>();
                int size = waveBean.Points.size();
                int pointAdd = (size / timeArea) / prePoint;
                LogUtil.d(TAG, "waveBean size = " + size + " pointAdd = " + pointAdd);

                for (int i = 0; i < waveBean.Points.size(); i = i + pointAdd) {
                    WaveBean.PointsBean pointsBean = waveBean.Points.get(i);
                    float data = (float) pointsBean.Data;
//                    String time = String.valueOf(i);
                    String time = TimeUtil.ms2Hms(pointsBean.TimeStamp);
                    DataRateBean dataRateBean = new DataRateBean(data, time);
                    dataRateBeans.add(dataRateBean);
                }
                LogUtil.d(TAG, "dataRateBean size = " + dataRateBeans.size());

                if (waveBean.siteId.equals(String.valueOf(startSiteId))) {
                    startMapData.put(key, dataRateBeans);
                } else {
                    endMapData.put(key, dataRateBeans);
                }
            }
        }

        if (startMapData != null && startMapData.size() > 0) {
            mBinding.chartStartSite.setVisibility(View.VISIBLE);
        }

        if (endMapData != null && endMapData.size() > 0) {
            mBinding.chartEndSite.setVisibility(View.VISIBLE);
        }

        ChartUtil.setChartViewData(startMapData, mBinding.chartStartSite);
        ChartUtil.setChartViewData(endMapData, mBinding.chartEndSite);
    }

    /**
     * 清除波形图数据
     */
    private void clearWaveLines() {
        LineChartData startData = mBinding.chartStartSite.getLineChartData();
        LineChartData endData = mBinding.chartEndSite.getLineChartData();

        if (startData != null) {
            List<Line> lines = startData.getLines();
            if (lines != null && lines.size() > 0) {
                for (Line line : lines) {
                    line.getValues().clear();
                }
            }
            mBinding.chartStartSite.setLineChartData(null);
        }
        if (endData != null) {
            List<Line> lines = endData.getLines();
            if (lines != null && lines.size() > 0) {
                for (Line line : lines) {
                    line.getValues().clear();
                }
            }
            mBinding.chartEndSite.setLineChartData(null);
        }
    }

    private void getDatas() {
        if (startSiteId < 0 || endSiteId < 0) {
            return;
        }

        hideWaveView();

        EventBusMessage eventBusMessage = new EventBusMessage(BaseConstant.PROGRESS_SHOW);
        EventBus.getDefault().post(eventBusMessage);

        long start = TimeUtil.Date2ms(startT);
        String startTime;
        String endTime;
        if (timeArea == 60) {
            startTime = TimeUtil.ms2Date(start - 30000);
            endTime = TimeUtil.ms2Date(start + 30000);
        } else {
            startTime = startT;
            endTime = TimeUtil.ms2Date(start + timeArea * 1000);
        }

        //测试使用
//        String startTime = "2018-10-14 00:00:00";
//        String endTime = "2018-10-14 00:03:00";

        //首站两个波
        ReqWave startWave = new ReqWave();
        startWave.sensorType = String.valueOf(curType);
        startWave.startTime = startTime;
        startWave.endTime = endTime;
        startWave.siteId = String.valueOf(startSiteId);

        //末站两个波
        ReqWave endWave = new ReqWave();
        endWave.sensorType = String.valueOf(curType);
        endWave.startTime = startTime;
        endWave.endTime = endTime;
        endWave.siteId = String.valueOf(endSiteId);


        List<ReqWave> reqWaves = new ArrayList<>();
        reqWaves.add(startWave);
        reqWaves.add(endWave);

        getWaveDatas(reqWaves);
    }

    private void getSubsonicDatas() {
        if (startSiteId < 0 || endSiteId < 0) {
            return;
        }

        hideWaveView();

        EventBusMessage eventBusMessage = new EventBusMessage(BaseConstant.PROGRESS_SHOW);
        EventBus.getDefault().post(eventBusMessage);

        long start = TimeUtil.Date2ms(startT);
        String startTime;
        String endTime;
        if (timeArea == 60) {
            startTime = TimeUtil.ms2Date(start - 30000);
            endTime = TimeUtil.ms2Date(start + 30000);
        } else {
            startTime = startT;
            endTime = TimeUtil.ms2Date(start + timeArea * 1000);
        }

        //测试数据
//        String startTime = "2018-10-14 00:00:00";
//        String endTime = "2018-10-14 00:03:00";

        //首站两个波
        ReqWave startWave1 = new ReqWave();
        startWave1.sensorType = String.valueOf(1);
        startWave1.startTime = startTime;
        startWave1.endTime = endTime;
        startWave1.siteId = String.valueOf(startSiteId);

        ReqWave startWave2 = new ReqWave();
        startWave2.sensorType = String.valueOf(2);
        startWave2.startTime = startTime;
        startWave2.endTime = endTime;
        startWave2.siteId = String.valueOf(startSiteId);
        //末站两个波
        ReqWave endWave1 = new ReqWave();
        endWave1.sensorType = String.valueOf(1);
        endWave1.startTime = startTime;
        endWave1.endTime = endTime;
        endWave1.siteId = String.valueOf(endSiteId);

        ReqWave endWave2 = new ReqWave();
        endWave2.sensorType = String.valueOf(2);
        endWave2.startTime = startTime;
        endWave2.endTime = endTime;
        endWave2.siteId = String.valueOf(endSiteId);

        List<ReqWave> reqWaves = new ArrayList<>();
        reqWaves.add(startWave1);
        reqWaves.add(startWave2);
        reqWaves.add(endWave1);
        reqWaves.add(endWave2);

        getWaveDatas(reqWaves);
    }

    private void getWaveDatas(List<ReqWave> reqWaves) {
        curDisposable = waveViewModel.reqAll(reqWaves);
    }

    private void closeDisposable() {
        if (curDisposable != null) {
            curDisposable.dispose();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (!BaseApplication.getInstance().isTwoPanel) {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }
}
