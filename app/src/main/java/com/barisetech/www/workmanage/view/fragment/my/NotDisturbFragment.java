package com.barisetech.www.workmanage.view.fragment.my;

import android.app.TimePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentNotDisturbBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.widget.SwitchView;

public class NotDisturbFragment extends BaseFragment {


    public static final String TAG = "NotDisturbFragment";
    FragmentNotDisturbBinding mBinding;
    private TimePickerDialog startTimePicker;
    private TimePickerDialog endTimePicker;
    private String startTime = "";
    private String endTime = "";


    public static NotDisturbFragment newInstance() {
        NotDisturbFragment fragment = new NotDisturbFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_not_disturb, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.not_disturb));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        startTimePicker = TimeUtil.getTimePicker(getActivity(), onStartTimeSetListener);
        endTimePicker = TimeUtil.getTimePicker(getActivity(), onEndTimeSetListener);

        if (SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.NOT_DISTURB_OPEN, false)){

            mBinding.tvTimeStart.setText(SharedPreferencesUtil.getInstance().getString(BaseConstant.NOT_DISTURB_START, ""));
            mBinding.tvTimeEnd.setText(SharedPreferencesUtil.getInstance().getString(BaseConstant.NOT_DISTURB_END, ""));
        }else {

        }

        mBinding.fingerSwitch.setOnClickListener(view -> {
            if (TextUtils.isEmpty(startTime)){
                ToastUtil.showToast("未设置开始时间");
            }else if (TextUtils.isEmpty(endTime)){
                ToastUtil.showToast("未设置结束时间");
            }
        });


        mBinding.fingerSwitch.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                SharedPreferencesUtil.getInstance().setString(BaseConstant.NOT_DISTURB_START, startTime);
                SharedPreferencesUtil.getInstance().setString(BaseConstant.NOT_DISTURB_END, endTime);
                SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.NOT_DISTURB_OPEN, true);

            }

            @Override
            public void toggleToOff(SwitchView view) {
                SharedPreferencesUtil.getInstance().setString(BaseConstant.NOT_DISTURB_START, "");
                SharedPreferencesUtil.getInstance().setString(BaseConstant.NOT_DISTURB_END, "");
                SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.NOT_DISTURB_OPEN, false);
                mBinding.tvTimeEnd.setText("");
                mBinding.tvTimeStart.setText("");
            }
        });
        mBinding.itemTimeStart.setOnClickListener(view -> {
            startTimePicker.show();

        });
        mBinding.itemTimeEnd.setOnClickListener(view -> {
            endTimePicker.show();

        });
    }


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
        mBinding.tvTimeStart.setText(startTime);
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
        mBinding.tvTimeEnd.setText(endTime);
    });

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
