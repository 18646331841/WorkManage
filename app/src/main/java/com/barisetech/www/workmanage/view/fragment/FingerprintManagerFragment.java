package com.barisetech.www.workmanage.view.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.utils.FingerprintUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;
import com.barisetech.www.workmanage.view.dialog.IDialogResultListener;

import static com.barisetech.www.workmanage.utils.ToastUtil.showToast;

/**
 * Created by LJH on 2018/8/6.
 */
public class FingerprintManagerFragment extends Fragment {
    public static final String TAG = "FingerprintManagerFragment";

    private Switch fpSwitch;
    private DialogFragment dialogFragment;

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
        Boolean isChecked = SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SP_LOGIN_FP, false);
        fpSwitch.setChecked(isChecked);

        fpSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                showCancleDialog();
                FingerprintUtil.getInstance().callFingerprint(onCallBackListenr);
            } else {
                showConfirmDialog();
            }
        });
    }

    private FingerprintUtil.OnCallBackListenr onCallBackListenr = new FingerprintUtil.OnCallBackListenr() {
        @Override
        public void onSupportFailed(String msg) {
            showToast(msg);
        }

        @Override
        public void onAuthenticationStart() {
            showToast("开始指纹识别");
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            showToast(errString);
        }

        @Override
        public void onAuthenticationFailed() {
            showToast("指纹识别失败");
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            showToast(helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            showToast("指纹识别成功");
            SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SP_LOGIN_FP, true);
            if (null != dialogFragment) {
                dialogFragment.getDialog().dismiss();
                fpSwitch.setChecked(true);
            }
        }
    };

    /**
     * 确认和取消的弹出窗
     */
    private void showConfirmDialog() {
        DialogFragmentHelper.showConfirmDialog(getActivity().getSupportFragmentManager(), getString(R.string
                .dialog_close_fingerprint), result -> {
            if (result) {
                SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SP_LOGIN_FP, false);
                fpSwitch.setChecked(false);
            }
        }, true, null);
    }

    /**
     * 取消弹出窗
     */
    private void showCancleDialog() {
        dialogFragment = DialogFragmentHelper.showCancleDialog(getActivity().getSupportFragmentManager
                (), getString(R.string.dialog_verify_fingerprint), result -> {
            if (!result) {
                FingerprintUtil.getInstance().cancel();
            }
        }, true, () -> {
            FingerprintUtil.getInstance().cancel();
        });
    }
}
