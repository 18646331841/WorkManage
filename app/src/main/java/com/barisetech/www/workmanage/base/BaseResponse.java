package com.barisetech.www.workmanage.base;

/**
 * Created by LJH on 2018/8/3.
 */
public class BaseResponse<T> {
    public int Code;
    public String Message;
    public T Response;

    public BaseResponse(int code, String message, T response) {
        this.Code = code;
        this.Message = message;
        this.Response = response;
    }
}
