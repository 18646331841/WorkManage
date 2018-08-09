package com.barisetech.www.workmanage.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.barisetech.www.workmanage.bean.AccessTokenInfo;
import com.barisetech.www.workmanage.db.dao.AccessTokenInfoDao;
import com.barisetech.www.workmanage.db.dao.TokenInfoDao;

/**
 * Created by LJH on 2018/8/8.
 */
@Database(entities = {AccessTokenInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;

    public static final String DATABASE_NAME = "work-db";

    public abstract AccessTokenInfoDao accessTokenInfoDao();
    public abstract TokenInfoDao tokenInfoDao();

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
