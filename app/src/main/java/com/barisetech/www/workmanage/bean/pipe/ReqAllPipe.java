package com.barisetech.www.workmanage.bean.pipe;

/**
 * Created by LJH on 2018/8/28.
 */
public class ReqAllPipe {
    /**
     * MachineCode : 83bc3793-d6b4-4483-9c96-a1a94929dfbf
     * PipeId :
     * startIndex : 2
     * numberOfRecords : 4
     */

    private String MachineCode;
    private String PipeId;
    private String startIndex;
    private String numberOfRecords;

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String MachineCode) {
        this.MachineCode = MachineCode;
    }

    public String getPipeId() {
        return PipeId;
    }

    public void setPipeId(String PipeId) {
        this.PipeId = PipeId;
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
}
