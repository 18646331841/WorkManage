package com.barisetech.www.workmanage.base;

import android.app.Application;

/**
 * Created by LJH on 2018/7/9.
 */
public class BaseApplication extends Application {
    private static BaseApplication Instance;
    public static String dataDir;
    public static String appDir = "/WorkManage";

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        dataDir = getFilesDir().getAbsolutePath() + appDir;

    }

    public static BaseApplication getInstance() {
        return Instance;
    }
}
