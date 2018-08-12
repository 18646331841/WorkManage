package com.barisetech.www.workmanage.bean.alarm;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by LJH on 2018/8/10.
 */
@Entity(tableName = "alarm_info")
public class AlarmInfo {
    /**
     * Key : 8
     * DisplayId : 0
     * PipeId : 0
     * Latitude : 0
     * Longitude : 0
     * Distance : 0
     * TimeStamp : 2018-12-11T03:11:11
     * Type : 0
     * TimeDiff : 0
     * Lifted : true
     * Company : BariseTech
     * LeakId : 0
     * SiteName1 : null
     * Distance1 : 0
     * SiteName2 : null
     * Distance2 : 0
     * PipeName : 测试管线
     * LiftUser : null
     * WarningMessage : 0
     * Remark : null
     */

    @PrimaryKey
    private int Key;
    private int DisplayId;
    private int PipeId;
    private int Latitude;
    private int Longitude;
    private int Distance;
    private String TimeStamp;
    private int Type;
    private int TimeDiff;
    private boolean Lifted;
    private String Company;
    private int LeakId;
    private String SiteName1;
    private int Distance1;
    private String SiteName2;
    private int Distance2;
    private String PipeName;
    private String LiftUser;
    private String WarningMessage;
    private String Remark;

    /**
     * 数据是否已读，默认false
     */
    private boolean isRead = false;

    public int getKey() {
        return Key;
    }

    public void setKey(int Key) {
        this.Key = Key;
    }

    public int getDisplayId() {
        return DisplayId;
    }

    public void setDisplayId(int DisplayId) {
        this.DisplayId = DisplayId;
    }

    public int getPipeId() {
        return PipeId;
    }

    public void setPipeId(int PipeId) {
        this.PipeId = PipeId;
    }

    public int getLatitude() {
        return Latitude;
    }

    public void setLatitude(int Latitude) {
        this.Latitude = Latitude;
    }

    public int getLongitude() {
        return Longitude;
    }

    public void setLongitude(int Longitude) {
        this.Longitude = Longitude;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int Distance) {
        this.Distance = Distance;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getTimeDiff() {
        return TimeDiff;
    }

    public void setTimeDiff(int TimeDiff) {
        this.TimeDiff = TimeDiff;
    }

    public boolean isLifted() {
        return Lifted;
    }

    public void setLifted(boolean Lifted) {
        this.Lifted = Lifted;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public int getLeakId() {
        return LeakId;
    }

    public void setLeakId(int LeakId) {
        this.LeakId = LeakId;
    }

    public String getSiteName1() {
        return SiteName1;
    }

    public void setSiteName1(String SiteName1) {
        this.SiteName1 = SiteName1;
    }

    public int getDistance1() {
        return Distance1;
    }

    public void setDistance1(int Distance1) {
        this.Distance1 = Distance1;
    }

    public String getSiteName2() {
        return SiteName2;
    }

    public void setSiteName2(String SiteName2) {
        this.SiteName2 = SiteName2;
    }

    public int getDistance2() {
        return Distance2;
    }

    public void setDistance2(int Distance2) {
        this.Distance2 = Distance2;
    }

    public String getPipeName() {
        return PipeName;
    }

    public void setPipeName(String PipeName) {
        this.PipeName = PipeName;
    }

    public String getLiftUser() {
        return LiftUser;
    }

    public void setLiftUser(String LiftUser) {
        this.LiftUser = LiftUser;
    }

    public String getWarningMessage() {
        return WarningMessage;
    }

    public void setWarningMessage(String WarningMessage) {
        this.WarningMessage = WarningMessage;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
