package com.barisetech.www.workmanage.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;

import java.util.List;

/**
 * Created by LJH on 2018/8/12.
 */
@Dao
public interface AlarmInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<AlarmInfo> alarmInfos);

    @Query("select * from alarm_info order by `Key` desc")
    LiveData<List<AlarmInfo>> getAllAlarmInfo();

    @Query("select * from alarm_info where isRead = :isRead")
    LiveData<List<AlarmInfo>> getAlarmInfosByRead(boolean isRead);

    @Query("select * from alarm_info where 'Key' = :key Limit 1")
    LiveData<AlarmInfo> getAlarmInfo(int key);
}
