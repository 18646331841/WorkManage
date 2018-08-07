package com.barisetech.www.workmanage.bean;

/**
 * Created by LJH on 2018/8/7.
 */
public class TokenInfo {
    /**
     * access_token : N3CtNFKbYEncqDU7s3tVXvNrqKO2eLiQLbKogloGzq37_y4GfzKFtMSwQI1ZGXYSi5FCjuCxF5Md-iYzg-C3CvLhVon7_
     * -wOVTBKSRR753z_QXpM93xFK7Scnw8_D4fi18j5XeTILn_FbSLTj9sl9RBww9KDgH9YMLWhjRHK581BuqNjNE4f2lnQ6BgMEtFFrlmK
     * -L_bn70D_LEiL8o2F1Z6vE4OHEVn6XvtB6MfAnKd02in-rEDtLG8vf1g0xnj
     * token_type : bearer
     * expires_in : 2999999
     * refresh_token : 79ecb7015f7c4a10b8a08321e8dfa884
     */

    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                '}';
    }
}
