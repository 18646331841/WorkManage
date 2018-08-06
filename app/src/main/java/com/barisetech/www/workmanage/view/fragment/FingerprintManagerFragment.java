package com.barisetech.www.workmanage.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.utils.FingerprintUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;

/**
 * Created by LJH on 2018/8/6.
 */
public class FingerprintManagerFragment extends Fragment {
    public static final String TAG = "FingerprintManagerFragment";

    private Switch fpSwitch;

    public FingerprintManagerFragment() {
        // Required empty public constructor
    }

    public static FingerprintManagerFragment newInstance() {
        FingerprintManagerFragment fragment = new FingerprintManagerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint_manager, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        fpSwitch = view.findViewById(R.id.fp_switch);
        fpSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                FingerprintUtil.getInstance().callFingerprint(onCallBackListenr);
            } else {
                FingerprintUtil.getInstance().cancel();
            }
        });
    }

    private FingerprintUtil.OnCallBackListenr onCallBackListenr = new FingerprintUtil.OnCallBackListenr() {
        @Override
        public void onSupportFailed(String msg) {
            ToastUtil.showToast(msg);
        }

        @Override
        public void onAuthenticationStart() {
            ToastUtil.showToast("开始指纹识别");
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            ToastUtil.showToast(errString);
        }

        @Override
        public void onAuthenticationFailed() {
            ToastUtil.showToast("指纹识别失败");
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            ToastUtil.showToast(helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            ToastUtil.showToast("指纹识别成功");
        }
    };
}
