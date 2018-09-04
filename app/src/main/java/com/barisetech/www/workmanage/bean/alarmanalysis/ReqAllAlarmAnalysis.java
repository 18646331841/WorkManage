package com.barisetech.www.workmanage.bean.alarmanalysis;

/**
 * Created by LJH on 2018/8/30.
 */
public class ReqAllAlarmAnalysis {
    /**
     * MachineCode : ${token}
     * isGetAll : false
     * getByid : false
     * alarmId : -1
     * alarmAnalysisId : -1
     * getByMy : false
     * startTime : 2009-12-12 12:12:12
     * endTime : 2019-12-12 12:12:12
     * startIndex : 0
     * numberOfRecords : 5
     * type : 0
     */

    private String MachineCode;
    private String isGetAll;
    private String getByid;
    private String alarmId;
    private String alarmAnalysisId;
    private String getByMy;
    private String startTime;
    private String endTime;
    private String startIndex;
    private String numberOfRecords;
    private String type;

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String MachineCode) {
        this.MachineCode = MachineCode;
    }

    public String getIsGetAll() {
        return isGetAll;
    }

    public void setIsGetAll(String isGetAll) {
        this.isGetAll = isGetAll;
    }

    public String getGetByid() {
        return getByid;
    }

    public void setGetByid(String getByid) {
        this.getByid = getByid;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmAnalysisId() {
        return alarmAnalysisId;
    }

    public void setAlarmAnalysisId(String alarmAnalysisId) {
        this.alarmAnalysisId = alarmAnalysisId;
    }

    public String getGetByMy() {
        return getByMy;
    }

    public void setGetByMy(String getByMy) {
        this.getByMy = getByMy;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
