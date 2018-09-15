package com.barisetech.www.workmanage.bean.plugin;

import java.io.Serializable;

public class PluginInfo implements Serializable{

    /**
     * Name : Default
     * Id : 5c92fe1b-11f6-4c42-b9c0-f71c5aee9b18
     * FullName : Default-5c92fe1b-11f6-4c42-b9c0-f71c5aee9b18
     */

    private String Name;
    private String Id;
    private String FullName;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }
}
