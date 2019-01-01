package com.barisetech.www.workmanage.bean.pipecollections;

import android.databinding.BindingAdapter;

import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.widget.LineView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LJH on 2018/8/28.
 */
public class PipeCollections implements Serializable{
    /**
     * Id : 6
     * Name : bbb
     * SortID : 0
     * Manager : ddd
     * Telephone : 6s
     * Email : s1s
     * Remark : ss1
     * PipeCollects : []
     */

    private int Id;
    private String Name;
    private int SortID;
    private String Manager;
    private String Telephone;
    private String Email;
    private String Remark;
    private List<PipeInfo> PipeCollects;

    @BindingAdapter("setText")
    public static void setText(LineView view, String s) {
        view.setText(s);
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getManager() {
        return Manager;
    }

    public void setManager(String Manager) {
        this.Manager = Manager;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public List<PipeInfo> getPipeCollects() {
        return PipeCollects;
    }

    public void setPipeCollects(List<PipeInfo> PipeCollects) {
        this.PipeCollects = PipeCollects;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getSortID() {
        return SortID;
    }

    public void setSortID(int sortID) {
        SortID = sortID;
    }
}
