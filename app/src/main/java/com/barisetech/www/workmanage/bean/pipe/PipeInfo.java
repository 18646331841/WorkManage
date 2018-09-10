package com.barisetech.www.workmanage.bean.pipe;

import android.databinding.BindingAdapter;
import android.view.View;

import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.widget.LineView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LJH on 2018/8/28.
 */
public class PipeInfo implements Serializable{
    /**
     * PipeId : 0
     * Name : 测试线路
     * Length : 100
     * Sites : [{"SiteId":3,"Name":"测试0站点","Longitude":0,"Latitude":0,"IsOnLine":false,"Company":"BariseTech",
     * "EnergyRatio":-10,"IsDwtEnabled":false,"IsAvgEnabled":false,"NegPeakThreshold":60000,"FreqStart":-600000,
     * "FreqEnd":15000,"FreqStep":15,"TimeNum":2000,"Telephone":"12555551236","Manager":"刘铮","IsDualSensor":false,
     * "IsDirFilterEnabled":false,"FilterType1":0,"IirType1":0,"IirBandtype1":0,"IirN1":0,"IirFc1":0,"IirF01":0,
     * "IirAp1":0,"IirAs1":0,"FirN1":0,"FirFc1":0,"FirAs1":0,"SensitivityOrg1":0,"HardwareGain1":0,"Sensitivity1":0,
     * "SensorCode1":null,"FilterType2":0,"IirType2":0,"IirBandtype2":0,"IirN2":0,"IirFc2":0,"IirF02":0,"IirAp2":0,
     * "IirAs2":0,"FirN2":0,"FirFc2":0,"FirAs2":0,"SensitivityOrg2":0,"HardwareGain2":0,"Sensitivity2":0,
     * "SensorCode2":null,"PressureRange":0,"PressureLowLimit":0,"PressureHighLimit":0,"PressureAmend":0,
     * "FlowRange":0,"FlowLowLimit":0,"FlowHighLimit":0,"FlowAmend":0,"LdPluginName":"Default",
     * "LdPluginId":"c0f607c5-fe14-4655-a125-c8fcb5dc1f4c","ShieldingRange":50,"TimeOffset":10},{"SiteId":4,
     * "Name":"测试1站点","Longitude":0,"Latitude":0,"IsOnLine":false,"Company":"BariseTech","EnergyRatio":-10,
     * "IsDwtEnabled":false,"IsAvgEnabled":false,"NegPeakThreshold":60000,"FreqStart":-600000,"FreqEnd":15000,
     * "FreqStep":15,"TimeNum":2000,"Telephone":"刘鑫","Manager":"15944315559","IsDualSensor":false,
     * "IsDirFilterEnabled":false,"FilterType1":0,"IirType1":0,"IirBandtype1":0,"IirN1":0,"IirFc1":0,"IirF01":0,
     * "IirAp1":0,"IirAs1":0,"FirN1":0,"FirFc1":0,"FirAs1":0,"SensitivityOrg1":0,"HardwareGain1":0,"Sensitivity1":0,
     * "SensorCode1":null,"FilterType2":0,"IirType2":0,"IirBandtype2":0,"IirN2":0,"IirFc2":0,"IirF02":0,"IirAp2":0,
     * "IirAs2":0,"FirN2":0,"FirFc2":0,"FirAs2":0,"SensitivityOrg2":0,"HardwareGain2":0,"Sensitivity2":0,
     * "SensorCode2":null,"PressureRange":0,"PressureLowLimit":0,"PressureHighLimit":0,"PressureAmend":0,
     * "FlowRange":0,"FlowLowLimit":0,"FlowHighLimit":0,"FlowAmend":1,"LdPluginName":"Default",
     * "LdPluginId":"c0f607c5-fe14-4655-a125-c8fcb5dc1f4c","ShieldingRange":50,"TimeOffset":10}]
     * Company : BariseTech
     * PipeMaterial : null
     * Speed : 400
     * Algorithm : false
     * BallChokLocation : false
     * LeakageAssessment : false
     * LeakCheckGap : 0
     * StartSiteId : 3
     * OriginSlavePressureDifferenceLowLimit : 0
     * OriginSlavePressureDifferenceHighLimit : 0
     * OriginSlavePressureDifferenceAmend : 0
     * OriginSlaveFlowCumulativeTime : 0
     * OriginSlaveFlowThresholdLowLimit : 0
     * OriginSlaveFlowThresholdHighLimit : 0
     * OriginSlaveFlowThresholdAmend : 0
     * PipeCollectID : {"Id":0,"Name":"测试集合","SortID":0,"Manager":"王五","Telephone":"15244441234",
     * "Email":"n@barisetech.com","Remark":"wu","PipeCollects":[]}
     * SortID : 0
     * LlPluginName : Default
     * LlPluginId : 5c92fe1b-11f6-4c42-b9c0-f71c5aee9b18
     * IsTestMode : false
     */

    public int PipeId;
    public String Name;
    public int Length;
    public String Company;
    public String PipeMaterial;
    public int Speed;
    public boolean Algorithm;
    public boolean BallChokLocation;
    public boolean LeakageAssessment;
    public int LeakCheckGap;
    public int StartSiteId;
    public int OriginSlavePressureDifferenceLowLimit;
    public int OriginSlavePressureDifferenceHighLimit;
    public int OriginSlavePressureDifferenceAmend;
    public int OriginSlaveFlowCumulativeTime;
    public int OriginSlaveFlowThresholdLowLimit;
    public int OriginSlaveFlowThresholdHighLimit;
    public int OriginSlaveFlowThresholdAmend;
    public PipeCollections PipeCollectID;
    public int SortID;
    public String LlPluginName;
    public String LlPluginId;
    public boolean IsTestMode;
    public List<SiteBean> Sites;

    @BindingAdapter("setText")
    public static void setText(LineView view, String text) {
        view.setText(text);
    }
    @BindingAdapter("setText")
    public static void setText(LineView view, int text) {
        view.setText(String.valueOf(text));
    }
}
