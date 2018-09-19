package com.barisetech.www.workmanage.bean.contacts;

/**
 * Created by LJH on 2018/9/19.
 */
public class ContactsBean {
    /**
     * Name : 王五
     * Telephone : 15244441234
     * Email : wu
     * Source : 管线集合：测试集合
     */

    private String Name;
    private String Telephone;
    private String Email;
    private String Source;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
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

    public String getSource() {
        return Source;
    }

    public void setSource(String Source) {
        this.Source = Source;
    }
}
