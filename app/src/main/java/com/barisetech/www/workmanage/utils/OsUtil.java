package com.barisetech.www.workmanage.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.barisetech.www.workmanage.base.BaseApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by LJH on 2018/10/9.
 */
public class OsUtil {

    public static List<String> getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        List<String> imeiList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int slot = 0; slot < telephonyManager.getPhoneCount(); slot++) {
                String imei = telephonyManager.getDeviceId(slot);
                if (!TextUtils.isEmpty(imei)) {
                    imeiList.add(imei);
                }
            }
        } else {
            String imei = telephonyManager.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                imeiList.add(imei);
            }
        }
        return imeiList;
    }

    /**
     * 获取网络IP，先获取GPRS，没有获取到再获取wifi
     *
     * @return
     */
    public static String getIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = inetAddress.getHostAddress().toString();
                        if (!TextUtils.isEmpty(ip)) {
                            return ip;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //获取wifi服务
        WifiManager wifiManager = (WifiManager) BaseApplication.getInstance().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        }
        return "";
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }
}
