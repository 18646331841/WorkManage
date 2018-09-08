package com.barisetech.www.workmanage.view.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.IncidentListAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.incident.ReqAllIncident;
import com.barisetech.www.workmanage.bean.incident.ReqIncidentSelectItem;
import com.barisetech.www.workmanage.databinding.FragmentIncidentListBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.IncidentViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncidentListFragment extends BaseFragment {
    public static final String TAG = "IncidentListFragment";

    private IncidentViewModel incidentViewModel;
    private IncidentListAdapter incidentListAdapter;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private List<IncidentInfo> incidentInfoList;
    FragmentIncidentListBinding mBinding;

    private DatePickerDialog startDatePicker;
    private DatePickerDialog endDatePicker;
    private TimePickerDialog startTimePicker;
    private TimePickerDialog endTimePicker;

    private String startTime = "";
    private String endTime = "";
    private Disposable curDisposable;

    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private int curType = BaseConstant.TYPE_INCIDENT_ALL;
    private int selectType;//选择的类型


    public IncidentListFragment() {
        // Required empty public constructor
    }

    public static IncidentListFragment newInstance() {
        IncidentListFragment fragment = new IncidentListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incidentInfoList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_incident_list, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_incident));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        startDatePicker = TimeUtil.getDatePicker(getActivity(), onStartDateSetListener);
        endDatePicker = TimeUtil.getDatePicker(getActivity(), onEndDateSetListener);
        startTimePicker = TimeUtil.getTimePicker(getActivity(), onStartTimeSetListener);
        endTimePicker = TimeUtil.getTimePicker(getActivity(), onEndTimeSetListener);

        initRecyclerView();

        mBinding.searchLayout.tvFilter.setOnClickListener(view -> {
            transFilterLayout();
        });

        mBinding.incidentListStartTime.setOnClickListener(view -> {
            //显示开始日期选择器
            startDatePicker.show();
        });
        mBinding.incidentListEndTime.setOnClickListener(view -> {
            //显示结束日期选择器
            endDatePicker.show();
        });

        mBinding.incidentListDigitizerOffline.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_INCIDENT_DIGI_OFFLINE;
            showDateRadio();
        });
        mBinding.incidentListDigitizerOnline.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_INCIDENT_DIGI_ONLINE;
            showDateRadio();
        });
        mBinding.incidentListDigitizerTimeOff.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_INCIDENT_DIGI_TIME;
            showDateRadio();
        });
        mBinding.incidentListSensorOffline.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_INCIDENT_SENSOR_OFFLINE;
            showDateRadio();
        });
        mBinding.incidentListIsolatorOffline.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_INCIDENT_ISOLATOR_OFFLINE;
            showDateRadio();
        });
        mBinding.incidentListUnknown.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_INCIDENT_UNKNOWN;
            showDateRadio();
        });

        mBinding.incidentListConfirm.setOnClickListener(view -> {
            if (TextUtils.isEmpty(startTime)) {
                ToastUtil.showToast("请选择开始时间");
                return;
            }
            if (TextUtils.isEmpty(endTime)) {
                ToastUtil.showToast("请选择结束时间");
                return;
            }
            closeDisposable();
            transFilterLayout();
            curType = selectType;
            incidentInfoList.clear();
            if (maxNum >= PAGE_COUNT) {
                getDatas(0, PAGE_COUNT);
            } else {
                getDatas(0, maxNum);
            }
        });
    }

    private void closeDisposable() {
        if (curDisposable != null) {
            curDisposable.dispose();
        }
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
        startTime = sb.toString();
        startTimePicker.show();
    };

    /**
     * 结束日期选择回调
     */
    private DatePickerDialog.OnDateSetListener onEndDateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
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
        endTime = sb.toString();
        endTimePicker.show();
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
        sb.append(startTime)
                .append(hourS).append(":")
                .append(minuteS).append(":00");
        startTime = sb.toString();
        mBinding.incidentListStartTime.setText(startTime);
    });

    /**
     * 结束时间选择回调
     */
    private TimePickerDialog.OnTimeSetListener onEndTimeSetListener = ((timePicker, hour, minute) -> {
        String hourS = String.valueOf(hour);
        String minuteS = String.valueOf(minute);
        if (hourS.length() == 1) {
            hourS = "0" + hourS;
        }
        if (minuteS.length() == 1) {
            minuteS = "0" + minuteS;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(endTime)
                .append(hourS).append(":")
                .append(minuteS).append(":00");
        endTime = sb.toString();
        mBinding.incidentListEndTime.setText(endTime);
    });

    /**
     * 显示日期选择按钮
     */
    private void showDateRadio() {
        if (mBinding.incidentListDateLayout.getVisibility() == View.GONE) {
            mBinding.incidentListDateLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 关闭日期选择按钮
     */
    private void closeDateRadio() {
        if (mBinding.incidentListDateLayout.getVisibility() == View.VISIBLE) {
            mBinding.incidentListDateLayout.setVisibility(View.GONE);
        }
    }

    private void cleanFilterGruop() {
        closeDateRadio();

        mBinding.incidentListDigitizerOffline.setChecked(false);
        mBinding.incidentListDigitizerOnline.setChecked(false);
        mBinding.incidentListDigitizerTimeOff.setChecked(false);
        mBinding.incidentListSensorOffline.setChecked(false);
        mBinding.incidentListIsolatorOffline.setChecked(false);
        mBinding.incidentListUnknown.setChecked(false);
    }

    /**
     * 打开或关闭筛选选择框
     */
    private void transFilterLayout() {
        if (mBinding.incidentListFilterLayout.getVisibility() == View.GONE) {
            mBinding.incidentListFilterLayout.setVisibility(View.VISIBLE);
        } else {
            mBinding.incidentListFilterLayout.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {

        incidentListAdapter = new IncidentListAdapter(incidentInfoList, getContext(), itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(incidentListAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.alarmList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.alarmList.setAdapter(loadMoreWrapper);
        mBinding.alarmList.setItemAnimator(new DefaultItemAnimator());

        mBinding.alarmList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (incidentInfoList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - incidentInfoList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(incidentInfoList.size(), count);
                    } else {
                        updateRecyclerView(incidentInfoList.size(), PAGE_COUNT);
                    }
                }
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);
    }

    private void getDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqAllIncident reqAllIncident = new ReqAllIncident();
        reqAllIncident.setStartIndex(String.valueOf(formIndex));
        reqAllIncident.setNumberOfRecord(String.valueOf(toIndex));

        ReqIncidentSelectItem reqIncidentSelectItem = new ReqIncidentSelectItem();
        reqIncidentSelectItem.setSiteIdQueryChecked("false");
        reqIncidentSelectItem.setSiteID("0");
        reqIncidentSelectItem.setMIncidentType(String.valueOf(curType));
        reqIncidentSelectItem.setTimeQueryChecked("true");
        if (TextUtils.isEmpty(startTime)) {
            //接口设计原因，默认使用最小时间
            reqIncidentSelectItem.setMStartTime("1970-01-01 00:00:00");
        } else {
            reqIncidentSelectItem.setMStartTime(startTime);
        }
        if (TextUtils.isEmpty(endTime)) {
            //默认使用当前时间
            reqIncidentSelectItem.setMEndTime(TimeUtil.ms2Date(System.currentTimeMillis()));
        } else {
            reqIncidentSelectItem.setMEndTime(endTime);
        }
        reqAllIncident.setIncidentSelectItem(reqIncidentSelectItem);

        curDisposable = incidentViewModel.reqAllIncident(reqAllIncident);
    }

    private void getListNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqIncidentSelectItem reqIncidentSelectItem = new ReqIncidentSelectItem();
        reqIncidentSelectItem.setSiteIdQueryChecked("false");
        reqIncidentSelectItem.setSiteID("0");
        reqIncidentSelectItem.setMIncidentType(String.valueOf(curType));
        reqIncidentSelectItem.setTimeQueryChecked("true");
        reqIncidentSelectItem.setMStartTime("1970-01-01 00:00:00");
        reqIncidentSelectItem.setMEndTime(TimeUtil.ms2Date(System.currentTimeMillis()));

        incidentViewModel.reqIncidentNum(reqIncidentSelectItem);
    }

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof IncidentInfo) {
            IncidentInfo incidentInfo = (IncidentInfo) item;
            EventBusMessage eventBusMessage = new EventBusMessage(IncidentDetailsFragment.TAG);
            eventBusMessage.setArg1(incidentInfo);
            EventBus.getDefault().post(eventBusMessage);
        }
    };

    @Override
    public void bindViewModel() {
        incidentViewModel = ViewModelProviders.of(this).get(IncidentViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!incidentViewModel.getmObservableIncidentsList().hasObservers()) {
            incidentViewModel.getmObservableIncidentsList().observe(this, incidentInfos -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != incidentInfos) {
                        if (incidentInfos.size() > 0) {
                            incidentInfoList.addAll(incidentInfos);
                            LogUtil.d(TAG, "load complete = " + incidentInfos);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }

        if (!incidentViewModel.getmObservableIncidentNum().hasObservers()) {
            incidentViewModel.getmObservableIncidentNum().observe(this, integer -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer) {
                        maxNum = integer;
                        if (maxNum >= PAGE_COUNT) {
                            getDatas(0, PAGE_COUNT);
                        } else {
                            getDatas(0, maxNum);
                        }
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }

        if (null == incidentInfoList || incidentInfoList.size() <= 0) {
            getListNums();
        }
    }
}
