package com.barisetech.www.workmanage.bean.alarm;

/**
 * Created by LJH on 2018/8/15.
 */
public class ReqLiftAlarm {
    /**
     * alarmId : 1
     * liftUser : admin
     */

    private String alarmId;
    private String liftUser;

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getLiftUser() {
        return liftUser;
    }

    public void setLiftUser(String liftUser) {
        this.liftUser = liftUser;
    }
}
