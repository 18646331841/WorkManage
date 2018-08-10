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
}
