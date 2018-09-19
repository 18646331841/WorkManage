package com.barisetech.www.workmanage.bean.contacts;

/**
 * Created by LJH on 2018/9/19.
 */
public class ReqContactsNum {
    /**
     * MachineCode : ${token}
     * selectItem : 0  *0代表全部1管线集合2站点3用户 searchString作用于分类下查找
     * searchString :
     */

    private String MachineCode;
    private String selectItem;
    private String searchString;

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
}
