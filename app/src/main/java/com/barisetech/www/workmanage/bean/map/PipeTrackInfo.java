
package com.barisetech.www.workmanage.bean.map;

import com.google.gson.annotations.SerializedName;

public class PipeTrackInfo {

    @SerializedName("Distance")
    private Long distance;
    @SerializedName("Latitude")
    private Double latitude;
    @SerializedName("Longitude")
    private Double longitude;
    @SerializedName("RawLatitude")
    private Long rawLatitude;
    @SerializedName("RawLongitude")
    private Long rawLongitude;

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getRawLatitude() {
        return rawLatitude;
    }

    public void setRawLatitude(Long rawLatitude) {
        this.rawLatitude = rawLatitude;
    }

    public Long getRawLongitude() {
        return rawLongitude;
    }

    public void setRawLongitude(Long rawLongitude) {
        this.rawLongitude = rawLongitude;
    }

}
