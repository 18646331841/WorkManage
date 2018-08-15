package com.barisetech.www.workmanage.bean.incident;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.barisetech.www.workmanage.bean.MessageInfo;

/**
 * Created by LJH on 2018/8/15.
 */
@Entity(tableName = "incident_info")
public class IncidentInfo extends MessageInfo{
    /**
     * Id : 1
     * SiteId : 1
     * SiteName : 1
     * PipeId : 1
     * PipeName : 1
     * TimeStamp : 2012-12-12T12:12:12
     * Type : 1
     * Lifted : true
     * LiftedUser : 1
     * IncidentContent : 1
     * IsTest : true
     * Remark : 1
     */

    @PrimaryKey
    private int Id;

    private int SiteId;
    private String SiteName;
    private int PipeId;
    private String PipeName;
    private String TimeStamp;
    private int Type;
    /**
     * 是否解除
     */
    private boolean Lifted;
    private String LiftedUser;
    private String IncidentContent;
    private boolean IsTest;
    private String Remark;

    /**
     * 数据是否已读，默认false
     */
    private boolean isRead = false;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
        messageId = Id;//设置父类id
    }

    public int getSiteId() {
        return SiteId;
    }

    public void setSiteId(int SiteId) {
        this.SiteId = SiteId;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String SiteName) {
        this.SiteName = SiteName;
    }

    public int getPipeId() {
        return PipeId;
    }

    public void setPipeId(int PipeId) {
        this.PipeId = PipeId;
    }

    public String getPipeName() {
        return PipeName;
    }

    public void setPipeName(String PipeName) {
        this.PipeName = PipeName;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String TimeStamp) {
        this.TimeStamp = TimeStamp;
        time = TimeStamp;//设置父类时间
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
        type = TYPE_INCIDENT;//设置父类类型
    }

    public boolean isLifted() {
        return Lifted;
    }

    public void setLifted(boolean Lifted) {
        this.Lifted = Lifted;
    }

    public String getLiftedUser() {
        return LiftedUser;
    }

    public void setLiftedUser(String LiftedUser) {
        this.LiftedUser = LiftedUser;
    }

    public String getIncidentContent() {
        return IncidentContent;
    }

    public void setIncidentContent(String IncidentContent) {
        this.IncidentContent = IncidentContent;
    }

    public boolean isIsTest() {
        return IsTest;
    }

    public void setIsTest(boolean IsTest) {
        this.IsTest = IsTest;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
        content = toContent();//设置父类内容
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String toContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID ").append(SiteId).append(", ")
                .append("站点名称").append(SiteName).append(", ")
                .append("管线ID ").append(PipeId).append(", ")
                .append("是否解除：").append(Lifted ? "已解除" : "未解除")
                .append("\n事件类型：").append(Type).append(", ")
                .append("\n管线名称：").append(PipeName)
                .append("\n是否解除：").append(isLifted()).append(", ")
                .append("解除人: ").append(LiftedUser);
        return sb.toString();
    }
}
