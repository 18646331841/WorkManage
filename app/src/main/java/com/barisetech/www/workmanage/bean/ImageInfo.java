package com.barisetech.www.workmanage.bean;

import java.io.Serializable;

/**
 * Created by LJH on 2018/9/3.
 */
public class ImageInfo implements Serializable{
    /**
     * Data : 图片string编码对象
     * CreatUser : account
     */

    private String Data;
    private String CreatUser;

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
}
