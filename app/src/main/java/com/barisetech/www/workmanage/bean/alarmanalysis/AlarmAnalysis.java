package com.barisetech.www.workmanage.bean.alarmanalysis;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.barisetech.www.workmanage.bean.ImageInfo;

import java.util.List;

/**
 * Created by LJH on 2018/8/30.
 */
public class AlarmAnalysis {
    /**
     * Id : 1
     * Tittle : 测试警报分析标题
     * ReleaseTime : 2018-09-04T10:56:55
     * AlarmID : 3
     * AlarmDetail : test
     * AlarmCause : 2
     * Image : null
     * AbstractContent : null
     * Analyst : k
     * ReadQuantity : 0
     * ReadPeopleList :
     * ReadLV : 3
     */

    private int Id;
    private String Tittle;
    private String ReleaseTime;
    private int AlarmID;
    private String AlarmDetail;
    private int AlarmCause;
    private List<ImageInfo> Image;
    private String AbstractContent;
    private String Analyst;
    private int ReadQuantity;
    private String ReadPeopleList;
    private int ReadLV;

    @BindingAdapter("showId")
    public static void showId(TextView view, int id) {
        view.setText("ID:" + id);
    }

    @BindingAdapter("showReadNum")
    public static void showReadNum(TextView view, int num) {
        view.setText("阅读 " + num);
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
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

    public int getAlarmID() {
        return AlarmID;
    }

    public void setAlarmID(int AlarmID) {
        this.AlarmID = AlarmID;
    }

    public String getAlarmDetail() {
        return AlarmDetail;
    }

    public void setAlarmDetail(String AlarmDetail) {
        this.AlarmDetail = AlarmDetail;
    }

    public int getAlarmCause() {
        return AlarmCause;
    }

    public void setAlarmCause(int AlarmCause) {
        this.AlarmCause = AlarmCause;
    }

    public String getAnalyst() {
        return Analyst;
    }

    public void setAnalyst(String Analyst) {
        this.Analyst = Analyst;
    }

    public int getReadQuantity() {
        return ReadQuantity;
    }

    public void setReadQuantity(int ReadQuantity) {
        this.ReadQuantity = ReadQuantity;
    }

    public String getReadPeopleList() {
        return ReadPeopleList;
    }

    public void setReadPeopleList(String ReadPeopleList) {
        this.ReadPeopleList = ReadPeopleList;
    }

    public int getReadLV() {
        return ReadLV;
    }

    public void setReadLV(int ReadLV) {
        this.ReadLV = ReadLV;
    }

    public List<ImageInfo> getImage() {
        return Image;
    }

    public void setImage(List<ImageInfo> image) {
        Image = image;
    }

    public String getAbstractContent() {
        return AbstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        AbstractContent = abstractContent;
    }
}
