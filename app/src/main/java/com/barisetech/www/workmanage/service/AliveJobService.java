package com.barisetech.www.workmanage.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SystemUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.LoginActivity;
import com.barisetech.www.workmanage.view.MainActivity;

/**
 * Created by LJH on 2018/9/25.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AliveJobService extends JobService {
    private final static String TAG = "KeepAliveService";
    // 告知编译器，这个变量不能被优化
    private volatile static Service mKeepAliveService = null;

    public static boolean isJobServiceAlive(){
        return mKeepAliveService != null;
    }

    private static final int MESSAGE_ID_TASK = 0x01;

    private Handler mHandler = new Handler(msg -> {
        // 具体任务逻辑
        if(SystemUtil.isAPPALive(getApplicationContext(), BaseApplication.PACKAGE_NAME)){
            LogUtil.d(TAG, "APP活着的");
        }else{
            LogUtil.d(TAG, "KeepAliveService----->App被杀死，准备从启");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            ToastUtil.showToast("APP被杀死，重启...");
        }
        // 通知系统任务执行结束
        jobFinished( (JobParameters) msg.obj, false );
        return true;
    });

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtil.d(TAG, "KeepAliveService----->JobService服务被启动...");

        mKeepAliveService = this;
        // 返回false，系统假设这个方法返回时任务已经执行完毕；
        // 返回true，系统假定这个任务正要被执行
        Message msg = Message.obtain(mHandler, MESSAGE_ID_TASK, params);
        mHandler.sendMessage(msg);
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        mHandler.removeMessages(MESSAGE_ID_TASK);
        LogUtil.d(TAG, "KeepAliveService----->JobService服务被关闭");
        return false;
    }
}
