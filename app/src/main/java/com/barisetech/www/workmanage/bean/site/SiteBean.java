package com.barisetech.www.workmanage.bean.site;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import java.io.Serializable;

public class SiteBean implements Serializable{

    /**
     * SiteId : 0
     * Name : 测试站点A(100001)
     * Longitude : 0
     * Latitude : 0
     * IsOnLine : false
     * Company : BariseTech
     * EnergyRatio : -10
     * IsDwtEnabled : false
     * IsAvgEnabled : false
     * NegPeakThreshold : 60000
     * FreqStart : -600000
     * FreqEnd : 15000
     * FreqStep : 15
     * TimeNum : 2000
     * Telephone : null
     * Manager : null
     * IsDualSensor : true
     * IsDirFilterEnabled : false
     * FilterType1 : 0
     * IirType1 : 0
     * IirBandtype1 : 0
     * IirN1 : 0
     * IirFc1 : 0
     * IirF01 : 0
     * IirAp1 : 0
     * IirAs1 : 0
     * FirN1 : 0
     * FirFc1 : 0
     * FirAs1 : 0
     * SensitivityOrg1 : 0
     * HardwareGain1 : 0
     * Sensitivity1 : 0
     * SensorCode1 : null
     * FilterType2 : 0
     * IirType2 : 0
     * IirBandtype2 : 0
     * IirN2 : 0
     * IirFc2 : 0
     * IirF02 : 0
     * IirAp2 : 0
     * IirAs2 : 0
     * FirN2 : 0
     * FirFc2 : 0
     * FirAs2 : 0
     * SensitivityOrg2 : 0
     * HardwareGain2 : 0
     * Sensitivity2 : 0
     * SensorCode2 : null
     * PressureRange : 0
     * PressureLowLimit : 0
     * PressureHighLimit : 0
     * PressureAmend : 0
     * FlowRange : 0
     * FlowLowLimit : 0
     * FlowHighLimit : 0
     * FlowAmend : 0
     * LdPluginName : Default
     * LdPluginId : c0f607c5-fe14-4655-a125-c8fcb5dc1f4c
     * ShieldingRange : 50
     * TimeOffset : 10
     */

    public int SiteId;
    public String Name;
    public double Longitude;
    public double Latitude;
    public boolean IsOnLine;
    public String Company;
    public double EnergyRatio;
    public boolean IsDwtEnabled;
    public boolean IsAvgEnabled;
    public int NegPeakThreshold;
    public double FreqStart;
    public double FreqEnd;
    public double FreqStep;
    public int TimeNum;
    public String Telephone;
    public String Manager;
    public boolean IsDualSensor;
    public boolean IsDirFilterEnabled;
    public int FilterType1;
    public int IirType1;
    public int IirBandtype1;
    public int IirN1;
    public float IirFc1;
    public float IirF01;
    public float IirAp1;
    public float IirAs1;
    public int FirN1;
    public float FirFc1;
    public float FirAs1;
    public float SensitivityOrg1;
    public float HardwareGain1;
    public float Sensitivity1;
    public String SensorCode1 = "";
    public int FilterType2;
    public int IirType2;
    public int IirBandtype2;
    public int IirN2;
    public float IirFc2;
    public float IirF02;
    public float IirAp2;
    public float IirAs2;
    public int FirN2;
    public float FirFc2;
    public float FirAs2;
    public float SensitivityOrg2;
    public float HardwareGain2;
    public float Sensitivity2;
    public String SensorCode2 = "";
    public float PressureRange;
    public float PressureLowLimit;
    public float PressureHighLimit;
    public float PressureAmend;
    public float FlowRange;
    public float FlowLowLimit;
    public float FlowHighLimit;
    public float FlowAmend;
    public String LdPluginName;
    public String LdPluginId;
    public double ShieldingRange;
    public int TimeOffset;

    @BindingAdapter("showBool")
    public static void showBool(TextView view, Boolean b) {
        if (b) {
            view.setText("是");
        } else {
            view.setText("否");
        }
    }

}
