package com.barisetech.www.workmanage.bean.pipework;

/**
 * Created by LJH on 2018/9/8.
 */
public class ReqAllPW {
    /**
     * MachineCode : ${token}
     * isGetAll : true
     * mStartTime : 2010-10-10 12:12:12
     * mEndTime : 2020-10-10 12:12:12
     * startIndex : 0
     * numberOfRecords : 2
     * PipeIdQueryChecked : false
     * TimeQueryChecked : false
     * PipeId : 0
     * Type : 0
     */

    private String MachineCode;
    private String isGetAll;
    private String mStartTime;
    private String mEndTime;
    private String startIndex;
    private String numberOfRecords;
    private String PipeIdQueryChecked;
    private String TimeQueryChecked;
    private String PipeId;
    private String Type;

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

    public String getMStartTime() {
        return mStartTime;
    }

    public void setMStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getMEndTime() {
        return mEndTime;
    }

    public void setMEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
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

    public String getPipeIdQueryChecked() {
        return PipeIdQueryChecked;
    }

    public void setPipeIdQueryChecked(String PipeIdQueryChecked) {
        this.PipeIdQueryChecked = PipeIdQueryChecked;
    }

    public String getTimeQueryChecked() {
        return TimeQueryChecked;
    }

    public void setTimeQueryChecked(String TimeQueryChecked) {
        this.TimeQueryChecked = TimeQueryChecked;
    }

    public String getPipeId() {
        return PipeId;
    }

    public void setPipeId(String PipeId) {
        this.PipeId = PipeId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
}
