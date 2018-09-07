package com.barisetech.www.workmanage.bean.incident;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.barisetech.www.workmanage.bean.MessageInfo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LJH on 2018/8/15.
 */
@Entity(tableName = "incident_info")
public class IncidentInfo extends MessageInfo implements Serializable {

    @Ignore
    private String[] typeToString = new String[]{"全部", "数字化仪上线", "数字化仪下线", "数字化仪时间不同步", "传感器不在线", "隔离器不在线",
            "一个查不到数据的标志"};
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
    @SerializedName("Id")
    private int Key;

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
    @SerializedName("IsTest")
    private boolean Test;
    private String Remark;

    /**
     * 数据是否已读，默认false
     */
    private boolean isRead = false;

    public int getKey() {
        return Key;
    }

    public void setKey(int key) {
        Key = key;
        id = key;//设置父类id
        messageType = TYPE_INCIDENT;//设置父类类型
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
        int index = Type - 1;
        if (index > 0 && index < typeToString.length) {
            title = typeToString[Type - 1];//设置父类title
        } else {
            title = String.valueOf(Type);
        }
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

    public boolean isTest() {
        return Test;
    }

    public void setTest(boolean test) {
        Test = test;
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

    public void toSuper() {
        id = Key;//设置父类id
        messageType = TYPE_INCIDENT;//设置父类类型
        time = TimeStamp;//设置父类时间
        int index = Type - 1;
        if (index > 0 && index < typeToString.length) {
            title = typeToString[Type - 1];//设置父类title
        } else {
            title = String.valueOf(Type);
        }
        content = toContent();//设置父类内容
    }

    public String toContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("时间：").append(TimeStamp)
                .append("\nID ").append(SiteId).append(", ")
                .append("站点名称").append(SiteName).append(", ")
                .append("管线ID ").append(PipeId).append(", ")
                .append("\n管线名称：").append(PipeName)
                .append("\n是否解除：").append(Lifted ? "已解除" : "未解除").append(", ")
                .append("解除人: ").append(LiftedUser);
        return sb.toString();
    }

    public String toDetails() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("              ID        ").append(Key).append("\n\n")
                .append("数字化仪ID          ").append(SiteId).append("\n\n")
                .append("站点名称        ").append(SiteName).append("\n\n")
                .append("所在管线ID          ").append(PipeId).append("\n\n")
                .append("所在管线        ").append(PipeName).append("\n\n")
                .append("发生时间        ").append(TimeStamp).append("\n\n")
                .append("事件类型        ").append(Type).append("\n\n")
                .append("是否解除        ").append(Lifted ? "已解除" : "未解除").append("\n\n")
                .append("操作人员        ").append(LiftedUser).append("\n\n")
                .append("提示内容        ").append(IncidentContent).append("\n\n")
                .append("        测试        ").append(Test).append("\n\n")
                .append("        备注        ").append(Remark).append("\n\n");
        return sb.toString();
    }
}
