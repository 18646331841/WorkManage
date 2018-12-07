package com.barisetech.www.workmanage.bean;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    /**
     * 是否已处理, true为已处理
     */
    protected boolean completed;

    /**
     * 审批结果, true为允许,false为禁止
     */
    protected boolean state;

    /**
     * 审批类型
     */
    protected boolean tapSwitch;

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isState() {
        return state;
    }

    @BindingAdapter("setAcceptText")
    public static void setAcceptText(Button button, int type) {
        if (type == TYPE_USER) {
            button.setText("允许登录");
        } else {
            button.setText("允许");
        }
    }

    @BindingAdapter("setRefuseText")
    public static void setRefuseText(Button button, int type) {
        if (type == TYPE_USER) {
            button.setText("禁止登录");
        } else {
            button.setText("禁止");
        }
    }

    @BindingAdapter({"completed", "type"})
    public static void setCompletedColor(TextView view, boolean completed, int type) {
        if (type == TYPE_PIPETAP) {
            //只有阀门需要
            if (completed) {
                view.setTextColor(Color.parseColor("#999999"));
            } else {
                view.setTextColor(Color.parseColor("#333333"));
            }
        }
    }

    @BindingAdapter({"completed", "type"})
    public static void setCompleted(Button button, boolean completed, int type) {
        if (type == TYPE_PIPETAP) {
            //只有阀门需要
            if (completed) {
                button.setVisibility(View.GONE);
            } else {
                button.setVisibility(View.VISIBLE);
            }
        }
    }

    @BindingAdapter({"completed", "state", "type", "tap"})
    public static void setCompleted(TextView view, boolean completed, boolean state, int type, boolean tap) {
        if (type == TYPE_PIPETAP) {
            //只有阀门需要
            if (completed) {
                view.setVisibility(View.VISIBLE);
                String tapS = tap ? "打开" : "关闭";
                if (state) {
                    view.setText("已允许" + tapS);
                    view.setTextColor(Color.parseColor("#999999"));
                } else {
                    view.setText("已禁止" + tapS);
                    view.setTextColor(Color.parseColor("#f36161"));
                }
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public boolean isTapSwitch() {
        return tapSwitch;
    }
}
