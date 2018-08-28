package com.barisetech.www.workmanage.bean.pipecollections;

/**
 * Created by LJH on 2018/8/28.
 */
public class ReqAllPc {
    /**
     * MachineCode : 54989d59-be4b-4699-93b1-bb2621a21adc
     * pipeCollectionId : 7
     * startIndex : 0
     * numberOfRecords : 3  给定id、startIndex=0、numberOfRecords=0时为查询某个
     */

    private String MachineCode;
    private String pipeCollectionId;
    private String startIndex;
    private String numberOfRecords;

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String MachineCode) {
        this.MachineCode = MachineCode;
    }

    public String getPipeCollectionId() {
        return pipeCollectionId;
    }

    public void setPipeCollectionId(String pipeCollectionId) {
        this.pipeCollectionId = pipeCollectionId;
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
