package com.barisetech.www.workmanage.base;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.barisetech.www.workmanage.utils.LogUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

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

    public void requestPermissions(Activity activity){
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEach(Manifest.permission.BROADCAST_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted){
//                          用户已经同意该权限
                            Log.e("",permission.name+"is granted");
                        }else if (permission.shouldShowRequestPermissionRationale){
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d("", permission.name + " is denied. More info should be provided.");
                        }else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d("", permission.name + " is denied.");
                        }
                    }
                });


    }


    public static BaseApplication getInstance() {
        return Instance;
    }
}
