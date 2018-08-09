package com.barisetech.www.workmanage.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by LJH on 2018/8/9.
 */
@Entity(tableName = "token_info")
public class TokenInfo {

    /**
     * {"LoginResult":true,"Token":"0e2fb01a-3443-400a-b293-82aa37960fec","Role":"SuperAdmins","Company":"BariseTech"}
     * */
    @PrimaryKey
    private int id;
    private boolean LoginResult;
    private String Token;
    private String Role;
    private String Company;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLoginResult() {
        return LoginResult;
    }

    public void setLoginResult(boolean loginResult) {
        LoginResult = loginResult;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "id=" + id +
                ", LoginResult=" + LoginResult +
                ", Token='" + Token + '\'' +
                ", Role='" + Role + '\'' +
                ", Company='" + Company + '\'' +
                '}';
    }
}
