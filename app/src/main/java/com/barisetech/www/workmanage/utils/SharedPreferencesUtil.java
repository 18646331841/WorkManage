package com.barisetech.www.workmanage.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.barisetech.www.workmanage.base.BaseApplication;

/**
 * Created by LJH on 2018/7/9.
 */
public class SharedPreferencesUtil {
    private static SharedPreferencesUtil INSTANCE;
    private SharedPreferences sharedPreferences;
    private static final String DEFAULT_NAME = "MySp";

    private SharedPreferencesUtil(String fileName) {
        sharedPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesUtil getInstance() {
        if (null == INSTANCE) {
            synchronized (SharedPreferencesUtil.class) {
                if (null == INSTANCE) {
                    INSTANCE = new SharedPreferencesUtil(DEFAULT_NAME);
                }
            }
        }
        return INSTANCE;
    }

    public int getInt(String key,int defaultValue)
    {
        return sharedPreferences.getInt(key, defaultValue);
    }
    public void setInt(String key,int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }
    public boolean getBoolean(String key,boolean defaultValue)
    {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    public void setBoolean(String key,boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String getString(String key,String defaultValue)
    {
        if(sharedPreferences ==null)
            return defaultValue;
        return sharedPreferences.getString(key, defaultValue);
    }

    public void setString(String key,String value) {
        if(sharedPreferences ==null)
            return ;
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void clearAll(){
        if(sharedPreferences ==null)
            return ;
        sharedPreferences.edit().clear().apply();
    }
}
