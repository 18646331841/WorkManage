package com.barisetech.www.workmanage.bean.alarm;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.barisetech.www.workmanage.bean.MessageInfo;

import java.io.Serializable;

/**
 * Created by LJH on 2018/8/10.
 */
@Entity(tableName = "alarm_info")
public class AlarmInfo extends MessageInfo implements Serializable{
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
    private double Latitude;
    private double Longitude;
    private double Distance;
    private String TimeStamp;
    private int Type;
    private int TimeDiff;
    private boolean Lifted;
    private String Company;
    /**
     * 是否解除
     */
    private int LeakId;
    private String SiteName1;
    private double Distance1;
    private String SiteName2;
    private double Distance2;
    private String PipeName;
    private String LiftUser;
    private String WarningMessage;
    private String Remark;

    /**
     * 数据是否已读，默认false
     */
    private boolean isRead = false;

    /**
     * 详情内容
     */
    @Ignore
    private String details;

    public int getKey() {
        return Key;
    }

    public void setKey(int Key) {
        this.Key = Key;
        id = Key;
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

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String TimeStamp) {
        if (TimeStamp != null && TimeStamp.contains("T")) {
            TimeStamp = TimeStamp.replace("T", " ");
        }
        this.TimeStamp = TimeStamp;
        time = TimeStamp;//设置父类时间
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

    public String getSiteName2() {
        return SiteName2;
    }

    public void setSiteName2(String SiteName2) {
        this.SiteName2 = SiteName2;
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
        details = toDetails();
    }

    public String getWarningMessage() {
        return WarningMessage;
    }

    public void setWarningMessage(String WarningMessage) {
        this.WarningMessage = WarningMessage;
        content = toContent();//设置父类content
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

    public void setRead(boolean isRead) {
        this.isRead = isRead;
        read = isRead;//设置父类read
    }

    public void toSuper() {
        if (TimeStamp != null && TimeStamp.contains("T")) {
            TimeStamp = TimeStamp.replace("T", " ");
        }
        id = Key;
        read = isRead;//设置父类read
        content = toContent();//设置父类content
        details = toDetails();
        time = TimeStamp;//设置父类时间
    }

    public String toContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID ").append(DisplayId).append(", ")
                .append("管线ID ").append(PipeId).append(", ")
                .append("是否解除：").append(Lifted ? "已解除" : "未解除")
                .append("\n管线名称：").append(PipeName)
                .append("\n距离首站：").append(Distance);
        return sb.toString();
    }

    public String toDetails() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("            ID        ").append(DisplayId).append("\n")
                .append("管  线  ID        ").append(PipeId).append("\n")
                .append("管线名称        ").append(PipeName).append("\n")
                .append("纬        度        ").append(Latitude).append("\n")
                .append("经        度        ").append(Longitude).append("\n")
                .append("距        离        ").append(Distance).append("\n")
                .append("时        间        ").append(TimeStamp).append("\n")
                .append("类        型        ").append(Type).append("\n")
                .append("时  间  差        ").append(TimeDiff).append("\n")
                .append("是否解除        ").append(Lifted ? "已解除" : "未解除").append("\n")
                .append("公        司        ").append(Company).append("\n")
                .append("首站站名        ").append(SiteName1).append("\n")
                .append("首站距离        ").append(Distance1).append("\n")
                .append("末站站名        ").append(SiteName2).append("\n")
                .append("末站距离        ").append(Distance2).append("\n")
                .append("解  除  人        ").append(LiftUser).append("\n");
        return sb.toString();
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "AlarmInfo{" +
                "Key=" + Key +
                ", DisplayId=" + DisplayId +
                ", PipeId=" + PipeId +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", Distance=" + Distance +
                ", TimeStamp='" + TimeStamp + '\'' +
                ", Type=" + Type +
                ", TimeDiff=" + TimeDiff +
                ", Lifted=" + Lifted +
                ", Company='" + Company + '\'' +
                ", LeakId=" + LeakId +
                ", SiteName1='" + SiteName1 + '\'' +
                ", Distance1=" + Distance1 +
                ", SiteName2='" + SiteName2 + '\'' +
                ", Distance2=" + Distance2 +
                ", PipeName='" + PipeName + '\'' +
                ", LiftUser='" + LiftUser + '\'' +
                ", WarningMessage='" + WarningMessage + '\'' +
                ", Remark='" + Remark + '\'' +
                ", isRead=" + isRead +
                ", details='" + details + '\'' +
                '}';
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public double getDistance1() {
        return Distance1;
    }

    public void setDistance1(double distance1) {
        Distance1 = distance1;
    }

    public double getDistance2() {
        return Distance2;
    }

    public void setDistance2(double distance2) {
        Distance2 = distance2;
    }
}
