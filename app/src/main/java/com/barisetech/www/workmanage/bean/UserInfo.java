package com.barisetech.www.workmanage.bean;

import java.io.Serializable;

public class UserInfo  implements Serializable{

    private String Email;
    private String Tel;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }
}
