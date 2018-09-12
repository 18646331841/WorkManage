package com.barisetech.www.workmanage.bean.pipework;

import android.databinding.BindingAdapter;

import com.barisetech.www.workmanage.widget.FormView;
import com.barisetech.www.workmanage.widget.LineView;

import java.io.Serializable;

/**
 * Created by LJH on 2018/9/8.
 */
public class PipeWork implements Serializable{

    /**
     * Id : 1
     * Name : ss
     * RecordDate : 2015-12-12T12:12:12
     * User : 0
     * PipeId : 0
     * PipeName : 0
     * PipeDiameter : 0
     * PipeThickness : 0
     * PipeMaterial : 0
     * PipeLength : 0
     * Speed : 0
     * BranchConditions : 0
     * PipeMedium : 0
     * MediumTemperature : 0
     * OriginId : 0
     * OriginName : 0
     * OriginSoftVersion : 0
     * OriginHardVersion : 0
     * OriginSensorNoiseAmplitude : 0
     * OriginSensorNoiseLevel : 0
     * OriginSensorType : 0
     * OriginAdcChannelPrimary : 0
     * OriginExtFirPrimary : 0
     * OriginIsolatorNoiseAmplitude : 0
     * OriginIsolatorNoiseLevel : 0
     * OriginIsolatorType : null
     * OriginAdcChannelIsolator : 0
     * OriginExtFirIsolator : 0
     * OriginPressure : 0
     * OriginMomentFlow : 0
     * OriginCumulativeFlow : 0
     * SlaveId : 0
     * SlaveName : 0
     * SlaveSoftVersion : 0
     * SlaveHardVersion : 0
     * SlaveSensorNoiseAmplitude : 0
     * SlaveSensorNoiseLevel : 0
     * SlaveSensorType : 0
     * SlaveAdcChannelPrimary : 0
     * SlaveExtFirPrimary : 0
     * SlaveIsolatorNoiseAmplitude : 0
     * SlaveIsolatorNoiseLevel : 0
     * SlaveIsolatorType : 0
     * SlaveAdcChannelIsolator : 0
     * SlaveExtFirIsolator : 0
     * SlavePressure : 0
     * SlaveMomentFlow : 0
     * SlaveCumulativeFlow : 0
     * Remark : 0
     */

    public int Id;
    public String Name;
    public String RecordDate;
    public String User;
    public int PipeId;
    public String PipeName;
    public String PipeDiameter;
    public String PipeThickness;
    public String PipeMaterial;
    public int PipeLength;
    public int Speed;
    public String BranchConditions;
    public String PipeMedium;
    public int MediumTemperature;
    public int OriginId;
    public String OriginName;
    public String OriginSoftVersion;
    public String OriginHardVersion;
    public int OriginSensorNoiseAmplitude;
    public int OriginSensorNoiseLevel;
    public String OriginSensorType;
    public int OriginAdcChannelPrimary;
    public int OriginExtFirPrimary;
    public int OriginIsolatorNoiseAmplitude;
    public int OriginIsolatorNoiseLevel;
    public String OriginIsolatorType;
    public int OriginAdcChannelIsolator;
    public int OriginExtFirIsolator;
    public int OriginPressure;
    public int OriginMomentFlow;
    public int OriginCumulativeFlow;
    public int SlaveId;
    public String SlaveName;
    public String SlaveSoftVersion;
    public String SlaveHardVersion;
    public int SlaveSensorNoiseAmplitude;
    public int SlaveSensorNoiseLevel;
    public String SlaveSensorType;
    public int SlaveAdcChannelPrimary;
    public int SlaveExtFirPrimary;
    public int SlaveIsolatorNoiseAmplitude;
    public int SlaveIsolatorNoiseLevel;
    public String SlaveIsolatorType;
    public int SlaveAdcChannelIsolator;
    public int SlaveExtFirIsolator;
    public int SlavePressure;
    public int SlaveMomentFlow;
    public int SlaveCumulativeFlow;
    public String Remark;

    @BindingAdapter("setText")
    public static void setText(LineView view, String text) {
        view.setText(text);
    }

    @BindingAdapter("setText")
    public static void setText(LineView view, int text) {
        view.setText(String.valueOf(text));
    }

    @BindingAdapter("setText")
    public static void setText(LineView view, Boolean text) {
        view.setText(text ? "是" : "否");
    }

    @BindingAdapter("setText")
    public static void setText(FormView view, String text) {
        view.setText(text);
    }

    @BindingAdapter("setText")
    public static void setText(FormView view, int text) {
        view.setText(String.valueOf(text));
    }

    @BindingAdapter("setText")
    public static void setText(FormView view, Boolean text) {
        view.setText(text ? "是" : "否");
    }
}
