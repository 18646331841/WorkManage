package com.barisetech.www.workmanage.bean.alarm;

/**
 * Created by LJH on 2018/8/10.
 */
public class ReqAllAlarm {
    /**
     * MachineCode : ${token}
     * startIndex : 1
     * numberOfRecords : 1
     * isAllAlarm : false
     */

    private String MachineCode;
    private String startIndex;
    private String numberOfRecords;
    private String isAllAlarm;
    /**
     * getByTimeDiff : true
     * startTime : 2000-01-01 12:12:12
     * endTime : 2050-01-01 12:12:12
     */

    private String getByTimeDiff;
    private String startTime;
    private String endTime;

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String MachineCode) {
        this.MachineCode = MachineCode;
    }

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public String getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(String numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public String getIsAllAlarm() {
        return isAllAlarm;
    }

    public void setIsAllAlarm(String isAllAlarm) {
        this.isAllAlarm = isAllAlarm;
    }

    @Override
    public String toString() {
        return "ReqAllAlarm{" +
                "MachineCode='" + MachineCode + '\'' +
                ", startIndex='" + startIndex + '\'' +
                ", numberOfRecords='" + numberOfRecords + '\'' +
                ", isAllAlarm='" + isAllAlarm + '\'' +
                '}';
    }

    public String getGetByTimeDiff() {
        return getByTimeDiff;
    }

    public void setGetByTimeDiff(String getByTimeDiff) {
        this.getByTimeDiff = getByTimeDiff;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
