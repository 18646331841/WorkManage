package com.barisetech.www.workmanage.view.fragment.workplan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.workplan.ReqAddPlan;
import com.barisetech.www.workmanage.databinding.FragmentPlanPublishSecondBinding;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

public class SecondPublishFragment extends BaseFragment {
    public static final String TAG = "SecondPublishFragment";

    FragmentPlanPublishSecondBinding mBinding;
    private DatePickerDialog endDatePicker;
//    private TimePickerDialog endTimePicker;
    private String endTime = "";
    private String time = "24:00:00";
    private int curTime = 0;
    private static final int TIME_ONE_MONTH = 1;
    private static final int TIME_ONE_QUARTER = 2;
    private static final int TIME_ONE_YEAR = 3;

    private static final String PLAN_ADD = "plan";
    private ReqAddPlan curPlanAdd;

    public SecondPublishFragment() {
        // Required empty public constructor
    }

    public static SecondPublishFragment newInstance(ReqAddPlan reqAddPlan) {
        SecondPublishFragment fragment = new SecondPublishFragment();
        if (reqAddPlan != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(PLAN_ADD, reqAddPlan);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curPlanAdd = (ReqAddPlan) getArguments().getSerializable(PLAN_ADD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_publish_second, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_plan_publish_plan));
        toolbarInfo.setOneText(getString(R.string.plan_publish_next_step));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        endDatePicker = TimeUtil.getDatePicker(getActivity(), onEndDateSetListener);
//        endTimePicker = TimeUtil.getTimePicker(getActivity(), onEndTimeSetListener);

        mBinding.planListStartTime.setOnClickListener(view -> {
            if (curTime != 0) {
                return;
            }
            endDatePicker.show();
        });

        mBinding.planPublishSecondOneMonth.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                closeTimePicker();
                curTime = TIME_ONE_MONTH;
                mBinding.planPublishSecondOneQuarter.setChecked(false);
                mBinding.planPublishSecondOneYear.setChecked(false);
            } else {
                curTime = 0;
            }
        });
        mBinding.planPublishSecondOneQuarter.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (b) {
                closeTimePicker();
                curTime = TIME_ONE_QUARTER;
                mBinding.planPublishSecondOneMonth.setChecked(false);
                mBinding.planPublishSecondOneYear.setChecked(false);
            } else {
                curTime = 0;
            }
        }));
        mBinding.planPublishSecondOneYear.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (b) {
                closeTimePicker();
                curTime = TIME_ONE_YEAR;
                mBinding.planPublishSecondOneMonth.setChecked(false);
                mBinding.planPublishSecondOneQuarter.setChecked(false);
            } else {
                curTime = 0;
            }
        }));

        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            String title = mBinding.planPublishSecondTitle.getText().toString();
            String date = null;
            if (curTime > 0) {
                switch (curTime) {
                    case TIME_ONE_MONTH:
                        date = TimeUtil.ms2Date(System.currentTimeMillis() + 2592000000L);
                        break;
                    case TIME_ONE_QUARTER:
                        date = TimeUtil.ms2Date(System.currentTimeMillis() + 7776000000L);
                        break;
                    case TIME_ONE_YEAR:
                        date = TimeUtil.ms2Date(System.currentTimeMillis() + 31536000000L);
                        break;
                }
            } else {
                date = mBinding.planListStartTime.getText().toString();
            }

            String num = mBinding.planPublishSecondPleaseNum.getText().toString();

            if (TextUtils.isEmpty(title)) {
                ToastUtil.showToast("请输入标题");
                return;
            }

            if (TextUtils.isEmpty(date) || date.equals(getString(R.string.plan_publish_second_deadline))) {
                ToastUtil.showToast("请选择日期");
                return;
            }

            if (TextUtils.isEmpty(num)) {
                ToastUtil.showToast("请输入次数");
                return;
            }

            if (curPlanAdd != null) {
                curPlanAdd.Name = title;
                curPlanAdd.EndTime = date;
                curPlanAdd.TotalNumberOfTimes = num;
                curPlanAdd.TimesOfCompletion = "0";

                EventBusMessage eventBusMessage = new EventBusMessage(ThirdPublishFragment.TAG);
                eventBusMessage.setArg1(curPlanAdd);
                EventBus.getDefault().post(eventBusMessage);
            }
        });
    }

    private void closeTimePicker() {
        mBinding.planListStartTime.setText(getString(R.string.plan_publish_second_deadline));
        mBinding.planListStartTime.setChecked(false);
    }

    /**
     * 开始日期选择回调
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
                .append(dayS).append(" ")
                .append(time);
        endTime = sb.toString();
//        endTimePicker.show();
        mBinding.planListStartTime.setText(endTime);
    };

    /**
     * 开始时间选择回调
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
        mBinding.planListStartTime.setText(endTime);
    });

    @Override
    public void bindViewModel() {
    }

    @Override
    public void subscribeToModel() {
    }
}
