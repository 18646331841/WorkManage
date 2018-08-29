package com.barisetech.www.workmanage.bean;

/**
 * Created by LJH on 2018/8/20.
 */
public class FailResponse {
    /**
     * 接口类型
     */
    public int type;
    /**
     * 错误码
     */
    public int code;

    public FailResponse(int type, int code) {
        this.type = type;
        this.code = code;
    }

    @Override
    public String toString() {
        return "TypeResponse{" +
                "type=" + type +
                ", code=" + code +
                '}';
    }
}
