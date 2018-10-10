package com.barisetech.www.workmanage.bean;

/**
 * Created by LJH on 2018/10/10.
 */
public class AuthBean {
    public static final int TYPE_USER = 0; //用户授权类型
    public static final int TYPE_PIPETAP = 1; //阀门授权类型

    /**
     * 授权类型
     */
    protected int type;

    /**
     * 显示标题
     */
    protected String title;

    /**
     * 显示内容
     */
    protected String content;

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
