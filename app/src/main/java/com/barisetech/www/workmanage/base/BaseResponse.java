package com.barisetech.www.workmanage.base;

/**
 * Created by LJH on 2018/8/3.
 */
public class BaseResponse<T> {
    public int code;
    public String message;
    public T response;

    public BaseResponse(int code, String message, T response) {
        this.code = code;
        this.message = message;
        this.response = response;
    }
}
