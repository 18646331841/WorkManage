package com.barisetech.www.workmanage.bean;

import android.arch.persistence.room.Ignore;

public class MessageInfo {
    public static final int TYPE_ALARM = 0; //警报类型
    public static final int TYPE_INCIDENT = 1; //事件类型

    @Ignore
    protected int id;

    /**
     * 消息类型，ALARM 或 INCIDENT
     */
    @Ignore
    protected int messageType;

    /**
     * 显示时间
     */
    @Ignore
    protected String time;

    /**
     * 显示内容
     */
    @Ignore
    protected String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
