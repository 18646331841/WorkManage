package com.barisetech.www.workmanage.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.barisetech.www.workmanage.bean.signin.ReqSignIn;

import java.io.Serializable;

/**
 * Created by LJH on 2018/9/3.
 */
@Entity(tableName = "image_info", indices = {@Index("signId")}, foreignKeys = {@ForeignKey(entity = ReqSignIn.class,
        parentColumns = "id", childColumns = "signId")})
public class ImageInfo implements Serializable {
    /**
     * Data : 图片string编码对象
     * CreatUser : account
     */

    @PrimaryKey
    public int id;
    /**
     * 专门打卡时使用
     */
    public int signId;

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
