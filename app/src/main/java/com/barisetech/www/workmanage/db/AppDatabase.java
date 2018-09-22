package com.barisetech.www.workmanage.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.barisetech.www.workmanage.bean.AccessTokenInfo;
import com.barisetech.www.workmanage.bean.ImageInfo;
import com.barisetech.www.workmanage.bean.TokenInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.signin.ReqSignIn;
import com.barisetech.www.workmanage.db.dao.AccessTokenInfoDao;
import com.barisetech.www.workmanage.db.dao.AlarmInfoDao;
import com.barisetech.www.workmanage.db.dao.ImageInfoDao;
import com.barisetech.www.workmanage.db.dao.IncidentDao;
import com.barisetech.www.workmanage.db.dao.ReqSignInDao;
import com.barisetech.www.workmanage.db.dao.TokenInfoDao;

/**
 * Created by LJH on 2018/8/8.
 */
@Database(entities = {AccessTokenInfo.class, TokenInfo.class, AlarmInfo.class, IncidentInfo.class, ReqSignIn.class,
        ImageInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;

    public static final String DATABASE_NAME = "work-db";

    public abstract AccessTokenInfoDao accessTokenInfoDao();
    public abstract TokenInfoDao tokenInfoDao();
    public abstract AlarmInfoDao alarmInfoDao();
    public abstract IncidentDao incidentDao();
    public abstract ImageInfoDao imageInfoDao();
    public abstract ReqSignInDao reqSignInDao();

    public static AppDatabase getsInstance(final Context context) {
        if (null == sInstance) {
            synchronized (AppDatabase.class) {
                if (null == sInstance) {
                    sInstance = buildDatabase(context);
                }
            }
        }
        return sInstance;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }
}
