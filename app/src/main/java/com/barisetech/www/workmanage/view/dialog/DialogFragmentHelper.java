package com.barisetech.www.workmanage.view.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.barisetech.www.workmanage.R;

public class DialogFragmentHelper {

    private static final String DIALOG_POSITIVE = "确定";
    private static final String DIALOG_NEGATIVE = "取消";

    private static final String TAG_HEAD = DialogFragmentHelper.class.getSimpleName();

    /**
     * 加载中的弹出窗
     */
    private static final int PROGRESS_THEME = R.style.Base_AlertDialog;
    private static final String PROGRESS_TAG = TAG_HEAD + ":progress";

    public static CommonDialogFragment showProgress(FragmentManager fragmentManager, String message){
        return showProgress(fragmentManager, message, true, null);
    }

    public static CommonDialogFragment showProgress(FragmentManager fragmentManager, String message, boolean cancelable){
        return showProgress(fragmentManager, message, cancelable, null);
    }

    public static CommonDialogFragment showProgress(FragmentManager fragmentManager, final String message, boolean cancelable
            , CommonDialogFragment.OnDialogCancelListener cancelListener){

        CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance(context -> {
            ProgressDialog progressDialog = new ProgressDialog(context, PROGRESS_THEME);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            return progressDialog;
        }, cancelable, cancelListener);
        dialogFragment.show(fragmentManager, PROGRESS_TAG);
        return dialogFragment;
    }

    /**
     * 简单提示弹出窗
     */
    private static final int TIPS_THEME = R.style.Base_AlertDialog;
    private static final String TIPS_TAG = TAG_HEAD + ":tips";

    public static void showTips(FragmentManager fragmentManager, String message){
        showTips(fragmentManager, message, true, null);
    }

    public static void showTips(FragmentManager fragmentManager, String message, boolean cancelable){
        showTips(fragmentManager, message, cancelable, null);
    }

    public static void showTips(FragmentManager fragmentManager, final String message, boolean cancelable
            , CommonDialogFragment.OnDialogCancelListener cancelListener){

        CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, TIPS_THEME);
                builder.setMessage(message);
                return builder.create();
            }
        }, cancelable, cancelListener);
        dialogFragment.show(fragmentManager, TIPS_TAG);
    }

    /**
     * 取消弹出窗
     */
    private static final int CANCLE_THEME = R.style.Base_AlertDialog;
    private static final String CANCLE_TAG = TAG_HEAD + ":tips";

    public static CommonDialogFragment showCancleDialog(FragmentManager fragmentManager, final String message, final
    IDialogResultListener<Boolean> listener, boolean cancelable, CommonDialogFragment.OnDialogCancelListener
            cancelListener) {

        CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, TIPS_THEME);
                builder.setMessage(message);
                builder.setNegativeButton(DIALOG_NEGATIVE, (dialog, which) -> {
                    if(listener != null){
                        listener.onDataResult(false);
                    }
                });
                return builder.create();
            }
        }, cancelable, cancelListener);
        dialogFragment.show(fragmentManager, TIPS_TAG);
        return dialogFragment;
    }


    /**
     * 确定取消框
     */
    private static final int CONFIRM_THEME = R.style.Base_AlertDialog;
    private static final String CONfIRM_TAG = TAG_HEAD + ":confirm";

    public static void showConfirmDialog(FragmentManager fragmentManager, final String message, final IDialogResultListener<Boolean> listener
            , boolean cancelable, CommonDialogFragment.OnDialogCancelListener cancelListener){
        CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance((Context context) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, CONFIRM_THEME);
            builder.setMessage(message);
            builder.setPositiveButton(DIALOG_POSITIVE, (dialog, which) -> {
                if(listener != null){
                    listener.onDataResult(true);
                }
            });
            builder.setNegativeButton(DIALOG_NEGATIVE, (dialog, which) -> {
                if(listener != null){
                    listener.onDataResult(false);
                }
            });
            return builder.create();
        }, cancelable, cancelListener);
        dialogFragment.show(fragmentManager, CONfIRM_TAG);
    }
}

















