package com.barisetech.www.workmanage.bean.incident;

/**
 * Created by LJH on 2018/8/15.
 */
public class ReqAllIncident {
    /**
     * MachineCode : ${token}
     * incidentSelectItem : {"mStartTime":"2010-01-01 12:12:12","mEndTime":"2019-01-01 12:12:12",
     * "TimeQueryChecked":"true","siteIdQueryChecked":"false","siteID":"0","mIncidentType":"1"}
     * startIndex : 0
     * numberOfRecord : 3
     */

    private String MachineCode;
    private ReqIncidentSelectItem incidentSelectItem;
    private String startIndex;
    private String numberOfRecord;

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

    public String getNumberOfRecord() {
        return numberOfRecord;
    }

    public void setNumberOfRecord(String numberOfRecord) {
        this.numberOfRecord = numberOfRecord;
    }

    public ReqIncidentSelectItem getIncidentSelectItem() {
        return incidentSelectItem;
    }

    public void setIncidentSelectItem(ReqIncidentSelectItem incidentSelectItem) {
        this.incidentSelectItem = incidentSelectItem;
    }
}
