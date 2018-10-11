package com.barisetech.www.workmanage.bean;

import java.io.Serializable;

/**
 * Created by LJH on 2018/8/3.
 */
public class EventBusMessage implements Serializable{
    public final String message;
    private Object arg1;//参数1
    private Object arg2;//参数2

    public EventBusMessage(String message) {
        this.message = message;
    }

    public Object getArg1() {
        return arg1;
    }

    public void setArg1(Object arg1) {
        this.arg1 = arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    public void setArg2(Object arg2) {
        this.arg2 = arg2;
    }
}
