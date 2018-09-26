package com.barisetech.www.workmanage.bean.digitalizer;

/**
 * Created by LJH on 2018/9/26.
 */
public class ReqAllDigitalizer {
    /**
     * isGetAll : false
     * siteId : -1
     * startIndex : 0
     * numberOfRecords : 1
     */

    private String isGetAll;
    private String siteId;
    private String startIndex;
    private String numberOfRecords;

    public String getIsGetAll() {
        return isGetAll;
    }

    public void setIsGetAll(String isGetAll) {
        this.isGetAll = isGetAll;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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
