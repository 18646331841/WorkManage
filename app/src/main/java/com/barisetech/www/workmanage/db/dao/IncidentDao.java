package com.barisetech.www.workmanage.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.barisetech.www.workmanage.bean.incident.IncidentInfo;

import java.util.List;

/**
 * Created by LJH on 2018/8/15.
 */
public interface IncidentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<IncidentInfo> incidentInfos);

    @Query("select * from incident_info order by `Id` desc")
    LiveData<List<IncidentInfo>> getAllIncidentInfo();

    @Query("select * from incident_info where isRead = :isRead")
    LiveData<List<IncidentInfo>> getIncidentInfosByRead(boolean isRead);
}
