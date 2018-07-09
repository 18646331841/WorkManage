package com.barisetech.www.workmanage.utils;

import android.util.Log;

import com.barisetech.www.workmanage.BuildConfig;

/**
 * Created by LJH on 2018/7/9.
 */
public class LogUtil {
    public static final String FILEPRESENTER = "FilePresenter";
    public static final String FILEACTIVITY = "FileActivity";
    public static final String FILEUTIL = "FileUtil";

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }
}
