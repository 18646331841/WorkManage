package com.barisetech.www.workmanage.base;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;

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

    public void requestPermissions(Activity activity) {
        String tag = activity.getLocalClassName();
        RxPermissions rxPermission = new RxPermissions(activity);
        Disposable disposable = rxPermission
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 用户已经同意该权限
                        LogUtil.d(tag, permission.name + " is granted.");
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        LogUtil.d(tag, permission.name + " is denied. More info should be provided.");
                    } else {
                        // 用户拒绝了该权限，并且选中『不再询问』
                        LogUtil.d(tag, permission.name + " is denied.");
                    }
                });
    }

    public static BaseApplication getInstance() {
        return Instance;
    }
}
