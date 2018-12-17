package com.barisetech.www.workmanage.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import com.barisetech.www.workmanage.base.BaseConstant;

import java.io.File;
import java.util.List;

/**
 * Created by LJH on 2018/9/25.
 */
public class SystemUtil {
    /**
     * 判断本应用是否存活
     * 如果需要判断本应用是否在后台还是前台用getRunningTask
     * */
    public static boolean isAPPALive(Context mContext, String packageName){
        boolean isAPPRunning = false;
        // 获取activity管理对象
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取所有正在运行的app
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        // 遍历，进程名即包名
        for(ActivityManager.RunningAppProcessInfo appInfo : appProcessInfoList){
            if(packageName.equals(appInfo.processName)){
                isAPPRunning = true;
                break;
            }
        }
        return isAPPRunning;
    }

    /**
     * 判断是否为管理员权限
     * @return
     */
    public static boolean isAdmin() {
        String role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
        if (role.equals(BaseConstant.ROLE_ADMINS) || role.equals(BaseConstant.ROLE_SUPER_ADMINS)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为超级管理员用户
     *
     * @return true 是， false 不是
     */
    public static boolean isSuperAdmin() {
        String role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
        if (role.equals(BaseConstant.ROLE_SUPER_ADMINS)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getBearer() {
        return "Bearer " + SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCESS_TOKEN, "");
    }
}
