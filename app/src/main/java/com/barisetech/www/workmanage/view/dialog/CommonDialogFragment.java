package com.barisetech.www.workmanage.view.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.Window;
import android.view.WindowManager;

import com.barisetech.www.workmanage.utils.FingerprintUtil;

import static com.barisetech.www.workmanage.utils.ToastUtil.showToast;

public class CommonDialogFragment extends DialogFragment {
    public static final String TAG = "CommonDialogFragment";

    /**
     * 监听弹出窗是否被取消
     */
    private OnDialogCancelListener mCancelListener;

    /**
     * 回调获得需要显示的 dialog
     */
    private OnCallDialog mOnCallDialog;

    public interface OnDialogCancelListener {
        void onCancel();
    }

    public interface OnCallDialog {
        Dialog getDialog(Context context);
    }

    public static CommonDialogFragment newInstance(OnCallDialog callDialog, boolean cancelable) {
        return newInstance(callDialog, cancelable, null);
    }

    public static CommonDialogFragment newInstance(OnCallDialog callDialog, boolean cancelable, OnDialogCancelListener cancelListener) {
        CommonDialogFragment instance = new CommonDialogFragment();
        instance.setCancelable(cancelable);
        instance.mCancelListener = cancelListener;
        instance.mOnCallDialog = callDialog;
        return instance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (null == mOnCallDialog) {
            super.onCreate(savedInstanceState);
        }
        return mOnCallDialog.getDialog(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {

            // 在 5.0 以下的版本会出现白色背景边框，若在 5.0 以上设置则会造成文字部分的背景也变成透明
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                // 目前只有这两个 dialog 会出现边框
                if (dialog instanceof ProgressDialog || dialog instanceof DatePickerDialog) {
                    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }

            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.0f;
            window.setAttributes(windowParams);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mCancelListener != null) {
            mCancelListener.onCancel();
        }
    }

    public void confirmFingerPrint() {
        FingerprintUtil.getInstance().callFingerprint(new FingerprintUtil.OnCallBackListenr() {
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
                getDialog().dismiss();
            }
        });

    }
}

