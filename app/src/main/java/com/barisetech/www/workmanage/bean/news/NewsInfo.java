package com.barisetech.www.workmanage.bean.news;

import java.util.List;

/**
 * Created by LJH on 2018/8/20.
 */
public class NewsInfo {
    /**
     * Id : 1
     * Type : 1
     * Tittle : eb581b2d-33ac-4900-b1ee-90707f579175
     * Description : eb581b2d-33ac-4900-b1ee-90707f579175
     * ReleaseTime : 2018-08-20T15:42:28
     * WebUrl : www.baid.com
     * Image : [{"Id":1,"Data":"2019-01-01 12:12:12","CreatUser":"false","CreatDate":"2018-08-20T15:42:28"}]
     */

    private int Id;
    private int Type;
    private String Tittle;
    private String Description;
    private String ReleaseTime;
    private String WebUrl;
    private List<NewsImageInfo> Image;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public String getTittle() {
        return Tittle;
    }

    public void setTittle(String Tittle) {
        this.Tittle = Tittle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getReleaseTime() {
        return ReleaseTime;
    }

    public void setReleaseTime(String ReleaseTime) {
        this.ReleaseTime = ReleaseTime;
    }

    public String getWebUrl() {
        return WebUrl;
    }

    public void setWebUrl(String WebUrl) {
        this.WebUrl = WebUrl;
    }

    public List<NewsImageInfo> getImage() {
        return Image;
    }

    public void setImage(List<NewsImageInfo> Image) {
        this.Image = Image;
    }
}
