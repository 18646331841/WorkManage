package com.barisetech.www.workmanage.bean.news;

/**
 * Created by LJH on 2018/8/20.
 */
public class ReqNewsInfos {
    /**
     * Type :
     *      Type为“”时返回一个不分页的全部新闻，
     *      Type为1为默认ALL类型
     *      2为公司新闻
     *      3为行业新闻
     *
     * StartIndex : 1
     * NumberOfRecords : 1
     */

    private String Type;
    private String StartIndex;
    private String NumberOfRecords;

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getStartIndex() {
        return StartIndex;
    }

    public void setStartIndex(String StartIndex) {
        this.StartIndex = StartIndex;
    }

    public String getNumberOfRecords() {
        return NumberOfRecords;
    }

    public void setNumberOfRecords(String NumberOfRecords) {
        this.NumberOfRecords = NumberOfRecords;
    }
}
