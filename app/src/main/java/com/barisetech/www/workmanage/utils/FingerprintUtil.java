package com.barisetech.www.workmanage.utils;

import android.app.KeyguardManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.barisetech.www.workmanage.base.BaseApplication;

/**
 * Created by LJH on 2018/8/6.
 */
public class FingerprintUtil {
    public static final String TAG = "FingerprintUtil";
    private volatile static FingerprintUtil ourInstance;

    public CancellationSignal cancellationSignal;

    public synchronized static FingerprintUtil getInstance() {
        if (null == ourInstance) {
            ourInstance = new FingerprintUtil();
        }
        return ourInstance;
    }

    private FingerprintUtil() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void callFingerprint(OnCallBackListenr listener) {
        if (null == listener) {
            return;
        }

        FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(BaseApplication.getInstance());
        if (!managerCompat.isHardwareDetected()){
            LogUtil.d(TAG, "设备不支持指纹");
            listener.onSupportFailed("设备不支持指纹");
            return;
        }
        KeyguardManager keyguardManager = (KeyguardManager) BaseApplication.getInstance().getApplicationContext()
                .getSystemService(BaseApplication.KEYGUARD_SERVICE);
        if (null == keyguardManager || !keyguardManager.isKeyguardSecure()) {
            LogUtil.d(TAG, "设备没处于安全保护中");
            listener.onSupportFailed("设备未设置指纹，请到系统中设置");
            return;
        }
        if (!managerCompat.hasEnrolledFingerprints()){
            LogUtil.d(TAG, "设备未设置指纹");
            listener.onSupportFailed("设备未设置指纹，请到系统中设置");
            return;
        }

        listener.onAuthenticationStart(); //开始指纹识别
        cancellationSignal  = new CancellationSignal(); //必须重新实例化，否则cancel 过一次就不能再使用了
        managerCompat.authenticate(null, 0, cancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
            // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息，比如华为的提示就是：尝试次数过多，请稍后再试。
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                listener.onAuthenticationError(errMsgId, errString);
            }

            // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
            @Override
            public void onAuthenticationFailed() {
                listener.onAuthenticationFailed();
            }

            //可恢复的错误发生，例如指纹传感器脏了，请擦拭
            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                listener.onAuthenticationHelp(helpMsgId, helpString);
            }

            // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                listener.onAuthenticationSucceeded(result);
            }
        }, null);
    }

    interface  OnCallBackListenr{
        void onSupportFailed(String msg);
        void onAuthenticationStart();
        void onAuthenticationError(int errMsgId, CharSequence errString);
        void onAuthenticationFailed();
        void onAuthenticationHelp(int helpMsgId, CharSequence helpString);
        void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result);
    }

    public void cancel(){
        if (cancellationSignal != null)
            cancellationSignal.cancel();
    }
}
