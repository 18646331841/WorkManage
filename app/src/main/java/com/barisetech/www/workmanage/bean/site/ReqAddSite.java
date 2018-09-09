package com.barisetech.www.workmanage.bean.site;



import java.util.List;

public class ReqAddSite {


    /**
     * SiteInfo : [{"SiteId":4,"Name":"测试1站点","Longitude":0,"Latitude":0,"IsOnLine":false,"Company":"BariseTech","EnergyRatio":-10,"IsDwtEnabled":false,"IsAvgEnabled":false,"NegPeakThreshold":60000,"FreqStart":-600000,"FreqEnd":15000,"FreqStep":15,"TimeNum":2000,"Telephone":"刘鑫","Manager":"15944315559","IsDualSensor":false,"IsDirFilterEnabled":false,"FilterType1":0,"IirType1":0,"IirBandtype1":0,"IirN1":0,"IirFc1":0,"IirF01":0,"IirAp1":0,"IirAs1":0,"FirN1":0,"FirFc1":0,"FirAs1":0,"SensitivityOrg1":0,"HardwareGain1":0,"Sensitivity1":0,"SensorCode1":null,"FilterType2":0,"IirType2":0,"IirBandtype2":0,"IirN2":0,"IirFc2":0,"IirF02":0,"IirAp2":0,"IirAs2":0,"FirN2":0,"FirFc2":0,"FirAs2":0,"SensitivityOrg2":0,"HardwareGain2":0,"Sensitivity2":0,"SensorCode2":null,"PressureRange":0,"PressureLowLimit":0,"PressureHighLimit":0,"PressureAmend":0,"FlowRange":0,"FlowLowLimit":0,"FlowHighLimit":0,"FlowAmend":1,"LdPluginName":"Default","LdPluginId":"c0f607c5-fe14-4655-a125-c8fcb5dc1f4c","ShieldingRange":50,"TimeOffset":10}]
     * Operation : 1
     */

    private String Operation;
    private List<SiteBean> SiteInfo;

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String Operation) {
        this.Operation = Operation;
    }

    public List<SiteBean> getSiteInfo() {
        return SiteInfo;
    }

    public void setSiteInfo(List<SiteBean> SiteInfo) {
        this.SiteInfo = SiteInfo;
    }


}
