package com.barisetech.www.workmanage.bean.news;

/**
 * Created by LJH on 2018/8/20.
 */
public class NewsImageInfo {
    /**
     * Id : 1
     * Data : 2019-01-01 12:12:12
     * CreatUser : false
     * CreatDate : 2018-08-20T15:42:28
     */

    private int Id;
    private String Data;
    private String CreatUser;
    private String CreatDate;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }

    public String getCreatUser() {
        return CreatUser;
    }

    public void setCreatUser(String CreatUser) {
        this.CreatUser = CreatUser;
    }

    public String getCreatDate() {
        return CreatDate;
    }

    public void setCreatDate(String CreatDate) {
        this.CreatDate = CreatDate;
    }
}
