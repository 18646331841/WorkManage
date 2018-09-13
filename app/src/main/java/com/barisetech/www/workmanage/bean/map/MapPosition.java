package com.barisetech.www.workmanage.bean.map;

/**
 * Created by LJH on 2018/8/2.
 */
public class MapPosition {
    /**
     * 纬度
     */
    public double latitude;

    /**
     * 经度
     */
    public double longitude;

    public MapPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
