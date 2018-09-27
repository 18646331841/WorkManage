package com.barisetech.www.workmanage.bean.pipelindarea;

import android.databinding.BindingAdapter;

import com.barisetech.www.workmanage.widget.LineView;

import java.io.Serializable;

public class PipeLindAreaInfo implements Serializable {


    /**
     * Id : 2
     * PipeId : 2
     * IsEnabled : false
     * Type : 0
     * StartDistance : 0
     * EndDistance : 15
     * Remark : 0
     */

    private int Id;
    private int PipeId;
    private boolean IsEnabled;
    private int Type;
    private float StartDistance;
    private float EndDistance;
    private String Remark;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getPipeId() {
        return PipeId;
    }

    public void setPipeId(int PipeId) {
        this.PipeId = PipeId;
    }

    public boolean isIsEnabled() {
        return IsEnabled;
    }

    public void setIsEnabled(boolean IsEnabled) {
        this.IsEnabled = IsEnabled;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public float getStartDistance() {
        return StartDistance;
    }

    public void setStartDistance(float StartDistance) {
        this.StartDistance = StartDistance;
    }

    public float getEndDistance() {
        return EndDistance;
    }

    public void setEndDistance(float EndDistance) {
        this.EndDistance = EndDistance;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    @BindingAdapter("showText")
    public static void showText(LineView view, String text) {
        view.setText(text);
    }

    @BindingAdapter("showBool")
    public static void showBool(LineView view, boolean b) {
        if (b) {
            view.setText("是");
        } else {
            view.setText("否");
        }
    }
}
