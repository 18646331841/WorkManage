package com.barisetech.www.workmanage.bean;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

/**
 * Created by LJH on 2018/8/17.
 */
public class ToolbarInfo {

    /**
     * back图片
     */
    private int backImg;

    /**
     * back文字
     */
    private String backText;

    /**
     * 标题
     */
    private String title;

    /**
     * 右一图片按钮
     */
    private int oneImg;
    /**
     * 右二图片按钮
     */
    private int twoImg;

    /**
     * 右一文字按钮
     */
    private String oneText;

    /**
     * 右二文字按钮
     */
    private String twoText;

    @BindingAdapter("show")
    public static void showIcon(ImageView iv, int imgId) {
        if (imgId > 0) {
            iv.setBackgroundResource(imgId);
        }
    }

    public int getBackImg() {
        return backImg;
    }

    public void setBackImg(int backImg) {
        this.backImg = backImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOneImg() {
        return oneImg;
    }

    public void setOneImg(int oneImg) {
        this.oneImg = oneImg;
    }

    public int getTwoImg() {
        return twoImg;
    }

    public void setTwoImg(int twoImg) {
        this.twoImg = twoImg;
    }

    public String getOneText() {
        return oneText;
    }

    public void setOneText(String oneText) {
        this.oneText = oneText;
    }

    public String getTwoText() {
        return twoText;
    }

    public void setTwoText(String twoText) {
        this.twoText = twoText;
    }

    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }
}
