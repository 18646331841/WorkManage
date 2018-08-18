package com.barisetech.www.workmanage.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.databinding.FragmentIpBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class IpFragment extends BaseFragment {
    public static final String TAG = "IpFragment";

    private FragmentIpBinding mBinding;

    public IpFragment() {
        // Required empty public constructor
    }

    public static IpFragment newInstance() {
        IpFragment fragment = new IpFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ip, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        //TODO 后期去掉
        mBinding.etIp.setText("www.barisetech.com");
        mBinding.etPort.setText("8081");

        String ipAndPort = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_IP_PORT, "");
        if (!TextUtils.isEmpty(ipAndPort)) {
            String[] ipPort = ipAndPort.split("_");
            mBinding.etIp.setText(ipPort[0]);
            mBinding.etPort.setText(ipPort[1]);
        }

        mBinding.login.setOnClickListener(view -> {
            String account = mBinding.etIp.getText().toString().trim();
            String password = mBinding.etPort.getText().toString().trim();
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                ToastUtil.showToast(getString(R.string.ip_null));
            } else {
                String spValue = account + "_" + password;
                SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_IP_PORT, spValue);
                EventBus.getDefault().post(new EventBusMessage(LoginFragment.TAG));
            }
        });
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
