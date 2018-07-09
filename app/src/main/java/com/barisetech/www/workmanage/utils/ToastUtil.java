package com.barisetech.www.workmanage.utils;

import android.widget.Toast;

import com.barisetech.www.workmanage.base.BaseApplication;

/**
 * Created by LJH on 2018/7/9.
 */
public class ToastUtil {
    private static Toast mToast;

    /**
     * 显示Toast
     */
    public static void showToast(CharSequence text) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}
