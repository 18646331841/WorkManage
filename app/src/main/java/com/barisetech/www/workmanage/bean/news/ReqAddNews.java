package com.barisetech.www.workmanage.bean.news;

import java.util.List;

/**
 * Created by LJH on 2018/8/20.
 */
public class ReqAddNews {
    /**
     * Tittle : eb581b2d-33ac-4900-b1ee-90707f579175
     * Description : eb581b2d-33ac-4900-b1ee-90707f579175
     * Type : 1
     * WebUrl : www.baid.com
     * ImageList : [{"Id":1,"Data":"2019-01-01 12:12:12","CreatUser":"false","CreatDate":"2018-08-20T15:42:28"}]
     * IsAdd : true, IsAdd是否添加操作, 为假是检验NewsId，根据NewId去修改对应的新闻信息
     * NewsId : 1
     */

    private String Tittle;
    private String Description;
    private String Type;
    private String WebUrl;
    private String IsAdd;
    private String NewsId;
    private List<NewsImageInfo> ImageList;

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

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getWebUrl() {
        return WebUrl;
    }

    public void setWebUrl(String WebUrl) {
        this.WebUrl = WebUrl;
    }

    public String getIsAdd() {
        return IsAdd;
    }

    public void setIsAdd(String IsAdd) {
        this.IsAdd = IsAdd;
    }

    public String getNewsId() {
        return NewsId;
    }

    public void setNewsId(String NewsId) {
        this.NewsId = NewsId;
    }

    public List<NewsImageInfo> getImageList() {
        return ImageList;
    }

    public void setImageList(List<NewsImageInfo> ImageList) {
        this.ImageList = ImageList;
    }
}
