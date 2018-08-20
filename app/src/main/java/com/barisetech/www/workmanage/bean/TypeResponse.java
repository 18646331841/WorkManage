package com.barisetech.www.workmanage.bean;

/**
 * Created by LJH on 2018/8/20.
 */
public class TypeResponse {
    /**
     * response类型
     */
    public int type;
    /**
     * response数据
     */
    public Object data;

    public TypeResponse(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return "TypeResponse{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
