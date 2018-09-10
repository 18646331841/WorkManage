package com.barisetech.www.workmanage.bean.pipework;

import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.site.SiteBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LJH on 2018/9/8.
 */
public class PipeWork implements Serializable{
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

    private int PipeId;
    private String Name;
    private int Length;
    private String Company;
    private String PipeMaterial;
    private int Speed;
    private boolean Algorithm;
    private boolean BallChokLocation;
    private boolean LeakageAssessment;
    private int LeakCheckGap;
    private int StartSiteId;
    private int OriginSlavePressureDifferenceLowLimit;
    private int OriginSlavePressureDifferenceHighLimit;
    private int OriginSlavePressureDifferenceAmend;
    private int OriginSlaveFlowCumulativeTime;
    private int OriginSlaveFlowThresholdLowLimit;
    private int OriginSlaveFlowThresholdHighLimit;
    private int OriginSlaveFlowThresholdAmend;
    private PipeCollections PipeCollectID;
    private int SortID;
    private String LlPluginName;
    private String LlPluginId;
    private boolean IsTestMode;
    private List<SiteBean> Sites;

    public int getPipeId() {
        return PipeId;
    }

    public void setPipeId(int PipeId) {
        this.PipeId = PipeId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int Length) {
        this.Length = Length;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public String getPipeMaterial() {
        return PipeMaterial;
    }

    public void setPipeMaterial(String PipeMaterial) {
        this.PipeMaterial = PipeMaterial;
    }

    public int getSpeed() {
        return Speed;
    }

    public void setSpeed(int Speed) {
        this.Speed = Speed;
    }

    public boolean isAlgorithm() {
        return Algorithm;
    }

    public void setAlgorithm(boolean Algorithm) {
        this.Algorithm = Algorithm;
    }

    public boolean isBallChokLocation() {
        return BallChokLocation;
    }

    public void setBallChokLocation(boolean BallChokLocation) {
        this.BallChokLocation = BallChokLocation;
    }

    public boolean isLeakageAssessment() {
        return LeakageAssessment;
    }

    public void setLeakageAssessment(boolean LeakageAssessment) {
        this.LeakageAssessment = LeakageAssessment;
    }

    public int getLeakCheckGap() {
        return LeakCheckGap;
    }

    public void setLeakCheckGap(int LeakCheckGap) {
        this.LeakCheckGap = LeakCheckGap;
    }

    public int getStartSiteId() {
        return StartSiteId;
    }

    public void setStartSiteId(int StartSiteId) {
        this.StartSiteId = StartSiteId;
    }

    public int getOriginSlavePressureDifferenceLowLimit() {
        return OriginSlavePressureDifferenceLowLimit;
    }

    public void setOriginSlavePressureDifferenceLowLimit(int OriginSlavePressureDifferenceLowLimit) {
        this.OriginSlavePressureDifferenceLowLimit = OriginSlavePressureDifferenceLowLimit;
    }

    public int getOriginSlavePressureDifferenceHighLimit() {
        return OriginSlavePressureDifferenceHighLimit;
    }

    public void setOriginSlavePressureDifferenceHighLimit(int OriginSlavePressureDifferenceHighLimit) {
        this.OriginSlavePressureDifferenceHighLimit = OriginSlavePressureDifferenceHighLimit;
    }

    public int getOriginSlavePressureDifferenceAmend() {
        return OriginSlavePressureDifferenceAmend;
    }

    public void setOriginSlavePressureDifferenceAmend(int OriginSlavePressureDifferenceAmend) {
        this.OriginSlavePressureDifferenceAmend = OriginSlavePressureDifferenceAmend;
    }

    public int getOriginSlaveFlowCumulativeTime() {
        return OriginSlaveFlowCumulativeTime;
    }

    public void setOriginSlaveFlowCumulativeTime(int OriginSlaveFlowCumulativeTime) {
        this.OriginSlaveFlowCumulativeTime = OriginSlaveFlowCumulativeTime;
    }

    public int getOriginSlaveFlowThresholdLowLimit() {
        return OriginSlaveFlowThresholdLowLimit;
    }

    public void setOriginSlaveFlowThresholdLowLimit(int OriginSlaveFlowThresholdLowLimit) {
        this.OriginSlaveFlowThresholdLowLimit = OriginSlaveFlowThresholdLowLimit;
    }

    public int getOriginSlaveFlowThresholdHighLimit() {
        return OriginSlaveFlowThresholdHighLimit;
    }

    public void setOriginSlaveFlowThresholdHighLimit(int OriginSlaveFlowThresholdHighLimit) {
        this.OriginSlaveFlowThresholdHighLimit = OriginSlaveFlowThresholdHighLimit;
    }

    public int getOriginSlaveFlowThresholdAmend() {
        return OriginSlaveFlowThresholdAmend;
    }

    public void setOriginSlaveFlowThresholdAmend(int OriginSlaveFlowThresholdAmend) {
        this.OriginSlaveFlowThresholdAmend = OriginSlaveFlowThresholdAmend;
    }

    public int getSortID() {
        return SortID;
    }

    public void setSortID(int SortID) {
        this.SortID = SortID;
    }

    public String getLlPluginName() {
        return LlPluginName;
    }

    public void setLlPluginName(String LlPluginName) {
        this.LlPluginName = LlPluginName;
    }

    public String getLlPluginId() {
        return LlPluginId;
    }

    public void setLlPluginId(String LlPluginId) {
        this.LlPluginId = LlPluginId;
    }

    public boolean isIsTestMode() {
        return IsTestMode;
    }

    public void setIsTestMode(boolean IsTestMode) {
        this.IsTestMode = IsTestMode;
    }

    public PipeCollections getPipeCollectID() {
        return PipeCollectID;
    }

    public void setPipeCollectID(PipeCollections pipeCollectID) {
        PipeCollectID = pipeCollectID;
    }

    public List<SiteBean> getSites() {
        return Sites;
    }

    public void setSites(List<SiteBean> sites) {
        Sites = sites;
    }
}
