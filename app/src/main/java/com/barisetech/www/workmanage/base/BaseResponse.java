package com.barisetech.www.workmanage.base;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LJH on 2018/8/3.
 */
public class BaseResponse<T> {
    @SerializedName("Code")
    public int code;
    @SerializedName("Message")
    public String message;
    @SerializedName("Response")
    public T response;

    public BaseResponse(int code, String message, T response) {
        this.code = code;
        this.message = message;
        this.response = response;
    }
}
