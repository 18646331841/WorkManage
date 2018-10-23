package com.barisetech.www.workmanage.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LJH on 2018/8/21.
 */
public class TimeUtil {

    /**
     * 将毫秒转换为标准日期格式
     *
     * @param _ms
     * @return
     */
    public static String ms2Date(long _ms) {
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    /**
     * 将毫秒转换为标准年月日
     *
     * @param _ms
     * @return
     */
    public static String ms2YMD(long _ms) {
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

    /**
     * 将毫秒转换为标准时分秒
     *
     * @param _ms
     * @return
     */
    public static String ms2Hms(long _ms) {
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    public static long Date2ms(String _data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = format.parse(_data);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 日期选择器
     *
     * @param activity
     * @param onDateSetListener
     */
    public static DatePickerDialog getDatePicker(Activity activity, DatePickerDialog.OnDateSetListener
            onDateSetListener) {
        Calendar ca = Calendar.getInstance();
        int mYear = ca.get(Calendar.YEAR);
        int mMonth = ca.get(Calendar.MONTH);
        int mDay = ca.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(activity, onDateSetListener, mYear, mMonth, mDay);
    }

    /**
     * 时间选择器
     *
     * @param activity
     * @param onTimeSetListener
     * @return
     */
    public static TimePickerDialog getTimePicker(Activity activity, TimePickerDialog.OnTimeSetListener
            onTimeSetListener) {
        Calendar ca = Calendar.getInstance();
        int hour = ca.get(Calendar.HOUR_OF_DAY);
        int minute = ca.get(Calendar.MINUTE);
        return new TimePickerDialog(activity, onTimeSetListener, hour, minute, true);
    }

    /***
     * 获取指定一天中指定的时间的时间戳
     *
     * @param hour
     * @param minute
     * @param second
     * @param milliSecond
     * @return
     */
    public static long getSpeDate(int hour, int minute, int second, int milliSecond) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.MILLISECOND, milliSecond);
        return cal.getTimeInMillis();
    }

    /**
     * 替换时间中的T字符
     * @param time
     * @return
     */
    public static String replaceTimeT(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        if (time.contains("T")) {
            time = time.replace("T", " ");
        }
        return time;
    }
}
