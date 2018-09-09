package com.barisetech.www.workmanage.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.barisetech.www.workmanage.bean.incident.IncidentInfo;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by LJH on 2018/8/15.
 */
@Dao
public interface IncidentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<IncidentInfo> incidentInfos);

    @Query("select * from incident_info order by `Key` desc")
    LiveData<List<IncidentInfo>> getAllIncidentInfo();

    @Query("select * from incident_info where isRead = :isRead")
    LiveData<List<IncidentInfo>> getIncidentInfosByRead(boolean isRead);

    @Query("select * from incident_info where `Key` = :key Limit 1")
    IncidentInfo getIncidentInfoSync(int key);

    @Query("select * from incident_info where `Key` = :key Limit 1")
    Maybe<IncidentInfo> getIncidentInfoRxjava(int key);

    @Update
    void updateIncident(IncidentInfo incidentInfo);
}
