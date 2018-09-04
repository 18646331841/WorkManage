package com.barisetech.www.workmanage.bean;

public class DataRateBean {
    /**
     * 数据点
     */
    private float data;
    /**
     * 时间
     */
    private String time;

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DataRateBean(float data, String time) {
        this.data = data;
        this.time = time;
    }
}
