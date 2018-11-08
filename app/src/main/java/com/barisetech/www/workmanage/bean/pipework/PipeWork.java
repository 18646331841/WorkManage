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
    public double PipeLength;
    public double Speed;
    public String BranchConditions;
    public String PipeMedium;
    public double MediumTemperature;
    public int OriginId;
    public String OriginName;
    public String OriginSoftVersion;
    public String OriginHardVersion;
    public long OriginSensorNoiseAmplitude;
    public long OriginSensorNoiseLevel;
    public String OriginSensorType;
    public int OriginAdcChannelPrimary;
    public double OriginExtFirPrimary;
    public long OriginIsolatorNoiseAmplitude;
    public long OriginIsolatorNoiseLevel;
    public String OriginIsolatorType;
    public int OriginAdcChannelIsolator;
    public double OriginExtFirIsolator;
    public double OriginPressure;
    public double OriginMomentFlow;
    public double OriginCumulativeFlow;
    public int SlaveId;
    public String SlaveName;
    public String SlaveSoftVersion;
    public String SlaveHardVersion;
    public long SlaveSensorNoiseAmplitude;
    public long SlaveSensorNoiseLevel;
    public String SlaveSensorType;
    public int SlaveAdcChannelPrimary;
    public double SlaveExtFirPrimary;
    public long SlaveIsolatorNoiseAmplitude;
    public long SlaveIsolatorNoiseLevel;
    public String SlaveIsolatorType;
    public int SlaveAdcChannelIsolator;
    public double SlaveExtFirIsolator;
    public double SlavePressure;
    public double SlaveMomentFlow;
    public double SlaveCumulativeFlow;
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
    public static void setText(LineView view, long text) {
        view.setText(String.valueOf(text));
    }

    @BindingAdapter("setText")
    public static void setText(LineView view, double text) {
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
    public static void setText(FormView view, long text) {
        view.setText(String.valueOf(text));
    }
    @BindingAdapter("setText")
    public static void setText(FormView view, double text) {
        view.setText(String.valueOf(text));
    }

    @BindingAdapter("setText")
    public static void setText(FormView view, Boolean text) {
        view.setText(text ? "是" : "否");
    }
}
