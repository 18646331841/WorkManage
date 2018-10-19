package com.barisetech.www.workmanage.bean.alarmanalysis;

import com.barisetech.www.workmanage.bean.ImageInfo;

import java.util.List;

/**
 * Created by LJH on 2018/8/30.
 */
public class ReqAddAlarmAnalysis {
    /**
     * Id : 0
     * isNewReader : true
     * ReadLV : 3
     * Tittle : 测试
     * ReleaseTime : 2018-8-31 12:12:12
     * AlarmID : 1
     * AlarmCause : 1
     * AlarmDetail : 1
     * Analyst : 1
     * ReadQuantity : 0
     * isAdd : true
     * ReadPeopleList : [{"Name":"1"},{"Name":""}]
     * ImageList : [{"Data":"2018-01-01 12:12:12","CreatUser":"false"}]
     */

    private String Id;
    private String isNewReader;
    private String ReadLV;
    private String Tittle;
    private String ReleaseTime;
    private String AlarmID;
    private String AlarmCause;
    private String AbstractContent;
    private String AlarmDetail;
    private String Analyst;
    private String ReadQuantity;
    private String isAdd;
    private List<ReadList> ReadPeopleList;
    private List<ImageInfo> ImageList;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getIsNewReader() {
        return isNewReader;
    }

    public void setIsNewReader(String isNewReader) {
        this.isNewReader = isNewReader;
    }

    public String getReadLV() {
        return ReadLV;
    }

    public void setReadLV(String ReadLV) {
        this.ReadLV = ReadLV;
    }

    public String getTittle() {
        return Tittle;
    }

    public void setTittle(String Tittle) {
        this.Tittle = Tittle;
    }

    public String getReleaseTime() {
        return ReleaseTime;
    }

    public void setReleaseTime(String ReleaseTime) {
        this.ReleaseTime = ReleaseTime;
    }

    public String getAlarmID() {
        return AlarmID;
    }

    public void setAlarmID(String AlarmID) {
        this.AlarmID = AlarmID;
    }

    public String getAlarmCause() {
        return AlarmCause;
    }

    public void setAlarmCause(String AlarmCause) {
        this.AlarmCause = AlarmCause;
    }

    public String getAlarmDetail() {
        return AlarmDetail;
    }

    public void setAlarmDetail(String AlarmDetail) {
        this.AlarmDetail = AlarmDetail;
    }

    public String getAnalyst() {
        return Analyst;
    }

    public void setAnalyst(String Analyst) {
        this.Analyst = Analyst;
    }

    public String getReadQuantity() {
        return ReadQuantity;
    }

    public void setReadQuantity(String ReadQuantity) {
        this.ReadQuantity = ReadQuantity;
    }

    public String getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(String isAdd) {
        this.isAdd = isAdd;
    }

    public List<ReadList> getReadPeopleList() {
        return ReadPeopleList;
    }

    public void setReadPeopleList(List<ReadList> ReadPeopleList) {
        this.ReadPeopleList = ReadPeopleList;
    }

    public List<ImageInfo> getImageList() {
        return ImageList;
    }

    public void setImageList(List<ImageInfo> ImageInfos) {
        this.ImageList = ImageInfos;
    }

    public String getAbstractContent() {
        return AbstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        AbstractContent = abstractContent;
    }
}
