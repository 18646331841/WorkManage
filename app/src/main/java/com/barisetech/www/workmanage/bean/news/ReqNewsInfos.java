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
    /**
     * GetByTime : true
     * startTime : 1970-03-03 12:12:12
     * endTime : 2020-03-03 12:12:12
     */

    private String GetByTime = "false";
    private String startTime;
    private String endTime;

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

    public String getGetByTime() {
        return GetByTime;
    }

    public void setGetByTime(String GetByTime) {
        this.GetByTime = GetByTime;
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
}
