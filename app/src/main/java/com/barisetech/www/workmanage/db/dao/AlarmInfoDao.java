package com.barisetech.www.workmanage.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;

import java.util.List;

/**
 * Created by LJH on 2018/8/12.
 */
@Dao
public interface AlarmInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AlarmInfo> alarmInfos);

}
