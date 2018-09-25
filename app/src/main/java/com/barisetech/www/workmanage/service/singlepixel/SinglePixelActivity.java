package com.barisetech.www.workmanage.service.singlepixel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.service.MyNotifyService;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SystemUtil;

/**
 * Created by LJH on 2018/9/25.
 */
public class SinglePixelActivity extends AppCompatActivity {
    private static final String TAG = "SinglePixelActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate--->启动1像素保活");
        // 获得activity的Window对象，设置其属性
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
        // 绑定SinglePixelActivity到ScreenManager
        ScreenManager.getScreenManagerInstance(this).setSingleActivity(this);
    }


    @Override
    protected void onDestroy() {
        LogUtil.d(TAG, "onDestroy--->1像素保活被终止");
        if(! SystemUtil.isAPPALive(this, BaseApplication.PACKAGE_NAME)){
//            Intent intentAlive = new Intent(this, SportsActivity.class);
//            intentAlive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intentAlive);
            startService(new Intent(this, MyNotifyService.class));
            LogUtil.d(TAG, "SinglePixelActivity---->APP被干掉了，我要重启它");
        }
        super.onDestroy();
    }
}
