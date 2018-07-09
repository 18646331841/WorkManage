package com.barisetech.www.workmanage.base;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.barisetech.www.workmanage.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LJH on 2017/7/9.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
        init();
    }

    public synchronized static CrashHandler getInstance() {
        if (null == instance) {
            instance = new CrashHandler();
        }
        return instance;
    }

    private void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        try {

            new Thread() {

                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(BaseApplication.getInstance(), BaseApplication.getInstance().getString(R.string
                            .crash_notice), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }.start();

            Map<String, String> infos = collectDeviceInfo();
            saveCrash2File(infos, ex);

            try {
                Thread.sleep(3000);
            } catch (Exception e) {

            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        } catch (Exception e) {

            try {

                mDefaultHandler.uncaughtException(t, ex);

            } catch (Exception ee) {

            }

        }
    }

    private Map<String, String> collectDeviceInfo() {

        Map<String, String> infos = new HashMap<String, String>();

        try {

            PackageManager pm = BaseApplication.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(BaseApplication.getInstance().getPackageName(), PackageManager
                    .GET_ACTIVITIES);

            infos.put("versionName", null != pi.versionName ? pi.versionName : "");
            infos.put("versionCode", String.valueOf(pi.versionCode));

            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {

                try {
                    field.setAccessible(true);
                    infos.put(field.getName(), field.get(null).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return infos;
    }

    private void saveCrash2File(Map<String, String> infos, Throwable throwable) {
        long timestamp = System.currentTimeMillis();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = format.format(timestamp);
        String filename = "crash-" + time + "-" + timestamp + ".log";

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        throwable.printStackTrace(printer);

        Throwable cause = throwable.getCause();
        while (null != cause) {
            cause.printStackTrace(printer);
            cause = cause.getCause();
        }
        printer.close();
        String result = writer.toString();
        sb.append(result);

        File dir = new File(makePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir.getAbsolutePath() + File.separator + filename);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String makePath() {
        String fileDir;
        boolean isSDCardExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        boolean isRootDirExist = Environment.getExternalStorageDirectory().exists();

        if (isSDCardExist && isRootDirExist) {
            fileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + BaseApplication.appDir;
        } else {
            fileDir = BaseApplication.dataDir;
        }
        return fileDir;
    }
}
