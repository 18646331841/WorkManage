package com.barisetech.www.workmanage.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LJH on 2018/8/7.
 */
@Entity(tableName = "token_info")
public class TokenInfo {
    /**
     * accessToken : N3CtNFKbYEncqDU7s3tVXvNrqKO2eLiQLbKogloGzq37_y4GfzKFtMSwQI1ZGXYSi5FCjuCxF5Md-iYzg-C3CvLhVon7_
     * -wOVTBKSRR753z_QXpM93xFK7Scnw8_D4fi18j5XeTILn_FbSLTj9sl9RBww9KDgH9YMLWhjRHK581BuqNjNE4f2lnQ6BgMEtFFrlmK
     * -L_bn70D_LEiL8o2F1Z6vE4OHEVn6XvtB6MfAnKd02in-rEDtLG8vf1g0xnj
     * tokenType : bearer
     * expiresIn : 2999999
     * refreshToken : 79ecb7015f7c4a10b8a08321e8dfa884
     */

    @PrimaryKey
    private int id;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("expires_in")
    private int expiresIn;

    @SerializedName("refresh_token")
    private String refreshToken;

    private long timestamp;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "id=" + id +
                ", accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
