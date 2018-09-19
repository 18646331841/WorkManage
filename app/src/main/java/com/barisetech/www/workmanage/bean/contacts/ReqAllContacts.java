package com.barisetech.www.workmanage.bean.contacts;

/**
 * Created by LJH on 2018/9/19.
 */
public class ReqAllContacts {

    /**
     * MachineCode : ${token}
     * selectItem : 0  *0代表全部1管线集合2站点3用户 searchString作用于分类下查找
     * searchString :
     * startIndex : 0
     * numberOfRecords : 3
     */

    private String MachineCode;
    private String selectItem;
    private String searchString;
    private String startIndex;
    private String numberOfRecords;

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String MachineCode) {
        this.MachineCode = MachineCode;
    }

    public String getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(String selectItem) {
        this.selectItem = selectItem;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
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
