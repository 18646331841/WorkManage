package com.barisetech.www.workmanage.bean.pipecollections;

import java.util.List;

/**
 * Created by LJH on 2018/8/28.
 */
public class PipeCollections {
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

    private String Id;
    private String Name;
    private String SortID;
    private String Manager;
    private String Telephone;
    private String Email;
    private String Remark;
    private List<?> PipeCollects;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSortID() {
        return SortID;
    }

    public void setSortID(String SortID) {
        this.SortID = SortID;
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

    public List<?> getPipeCollects() {
        return PipeCollects;
    }

    public void setPipeCollects(List<?> PipeCollects) {
        this.PipeCollects = PipeCollects;
    }
}
