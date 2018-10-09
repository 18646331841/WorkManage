package com.barisetech.www.workmanage.view.fragment.my;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentEventTypeBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;

public class EventTypeFragment extends BaseFragment {


    public static final String TAG = "EventTypeFragment";

    FragmentEventTypeBinding mBinding;

    public static EventTypeFragment newInstance() {
        EventTypeFragment fragment = new EventTypeFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_type, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.event_type));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        String typeSP = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_INCIDENT_TYPES, BaseConstant
                .TYPES_INCIDENT);
        String[] types = typeSP.split(",");
        for (String type : types) {
            int t = Integer.valueOf(type);
            switch (t) {
                case BaseConstant.TYPE_INCIDENT_DIGI_OFFLINE:
                    mBinding.offlineSwitch.setOpened(true);
                    break;
                case BaseConstant.TYPE_INCIDENT_DIGI_ONLINE:
                    mBinding.onlineSwitch.setOpened(true);
                    break;
                case BaseConstant.TYPE_INCIDENT_DIGI_TIME:
                    mBinding.timeOffSwitch.setOpened(true);
                    break;
                case BaseConstant.TYPE_INCIDENT_ISOLATOR_OFFLINE:
                    mBinding.isolatorOfflineSwitch.setOpened(true);
                    break;
                case BaseConstant.TYPE_INCIDENT_SENSOR_OFFLINE:
                    mBinding.sensorOfflineSwitch.setOpened(true);
                    break;
                case BaseConstant.TYPE_INCIDENT_UNKNOWN:
                    mBinding.unknownSwitch.setOpened(true);
                    break;
            }
        }

        mBinding.eventTypeSave.setOnClickListener(view -> {
            StringBuilder sb = new StringBuilder();
            if (mBinding.isolatorOfflineSwitch.isOpened()) {
                sb
                        .append(BaseConstant.TYPE_INCIDENT_ISOLATOR_OFFLINE)
                        .append(",");
            }
            if (mBinding.offlineSwitch.isOpened()) {
                sb
                        .append(BaseConstant.TYPE_INCIDENT_DIGI_OFFLINE)
                        .append(",");
            }
            if (mBinding.onlineSwitch.isOpened()) {
                sb
                        .append(BaseConstant.TYPE_INCIDENT_DIGI_ONLINE)
                        .append(",");
            }
            if (mBinding.sensorOfflineSwitch.isOpened()) {
                sb
                        .append(BaseConstant.TYPE_INCIDENT_SENSOR_OFFLINE)
                        .append(",");
            }
            if (mBinding.timeOffSwitch.isOpened()) {
                sb
                        .append(BaseConstant.TYPE_INCIDENT_DIGI_TIME)
                        .append(",");
            }
            if (mBinding.unknownSwitch.isOpened()) {
                sb
                        .append(BaseConstant.TYPE_INCIDENT_UNKNOWN)
                        .append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);

                String value = sb.toString();
                Log.d(TAG, value);
                SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_INCIDENT_TYPES, value);
            } else {
                SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_INCIDENT_TYPES, "");
            }
            ToastUtil.showToast("设置成功");

        });
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
