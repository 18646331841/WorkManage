package com.barisetech.www.workmanage.bean.site;

public class SiteBean {

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

    private int SiteId;
    private String Name;
    private int Longitude;
    private int Latitude;
    private boolean IsOnLine;
    private String Company;
    private int EnergyRatio;
    private boolean IsDwtEnabled;
    private boolean IsAvgEnabled;
    private int NegPeakThreshold;
    private int FreqStart;
    private int FreqEnd;
    private int FreqStep;
    private int TimeNum;
    private Object Telephone;
    private Object Manager;
    private boolean IsDualSensor;
    private boolean IsDirFilterEnabled;
    private int FilterType1;
    private int IirType1;
    private int IirBandtype1;
    private int IirN1;
    private int IirFc1;
    private int IirF01;
    private int IirAp1;
    private int IirAs1;
    private int FirN1;
    private int FirFc1;
    private int FirAs1;
    private int SensitivityOrg1;
    private int HardwareGain1;
    private int Sensitivity1;
    private Object SensorCode1;
    private int FilterType2;
    private int IirType2;
    private int IirBandtype2;
    private int IirN2;
    private int IirFc2;
    private int IirF02;
    private int IirAp2;
    private int IirAs2;
    private int FirN2;
    private int FirFc2;
    private int FirAs2;
    private int SensitivityOrg2;
    private int HardwareGain2;
    private int Sensitivity2;
    private Object SensorCode2;
    private int PressureRange;
    private int PressureLowLimit;
    private int PressureHighLimit;
    private int PressureAmend;
    private int FlowRange;
    private int FlowLowLimit;
    private int FlowHighLimit;
    private int FlowAmend;
    private String LdPluginName;
    private String LdPluginId;
    private int ShieldingRange;
    private int TimeOffset;

    public int getSiteId() {
        return SiteId;
    }

    public void setSiteId(int SiteId) {
        this.SiteId = SiteId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getLongitude() {
        return Longitude;
    }

    public void setLongitude(int Longitude) {
        this.Longitude = Longitude;
    }

    public int getLatitude() {
        return Latitude;
    }

    public void setLatitude(int Latitude) {
        this.Latitude = Latitude;
    }

    public boolean isIsOnLine() {
        return IsOnLine;
    }

    public void setIsOnLine(boolean IsOnLine) {
        this.IsOnLine = IsOnLine;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public int getEnergyRatio() {
        return EnergyRatio;
    }

    public void setEnergyRatio(int EnergyRatio) {
        this.EnergyRatio = EnergyRatio;
    }

    public boolean isIsDwtEnabled() {
        return IsDwtEnabled;
    }

    public void setIsDwtEnabled(boolean IsDwtEnabled) {
        this.IsDwtEnabled = IsDwtEnabled;
    }

    public boolean isIsAvgEnabled() {
        return IsAvgEnabled;
    }

    public void setIsAvgEnabled(boolean IsAvgEnabled) {
        this.IsAvgEnabled = IsAvgEnabled;
    }

    public int getNegPeakThreshold() {
        return NegPeakThreshold;
    }

    public void setNegPeakThreshold(int NegPeakThreshold) {
        this.NegPeakThreshold = NegPeakThreshold;
    }

    public int getFreqStart() {
        return FreqStart;
    }

    public void setFreqStart(int FreqStart) {
        this.FreqStart = FreqStart;
    }

    public int getFreqEnd() {
        return FreqEnd;
    }

    public void setFreqEnd(int FreqEnd) {
        this.FreqEnd = FreqEnd;
    }

    public int getFreqStep() {
        return FreqStep;
    }

    public void setFreqStep(int FreqStep) {
        this.FreqStep = FreqStep;
    }

    public int getTimeNum() {
        return TimeNum;
    }

    public void setTimeNum(int TimeNum) {
        this.TimeNum = TimeNum;
    }

    public Object getTelephone() {
        return Telephone;
    }

    public void setTelephone(Object Telephone) {
        this.Telephone = Telephone;
    }

    public Object getManager() {
        return Manager;
    }

    public void setManager(Object Manager) {
        this.Manager = Manager;
    }

    public boolean isIsDualSensor() {
        return IsDualSensor;
    }

    public void setIsDualSensor(boolean IsDualSensor) {
        this.IsDualSensor = IsDualSensor;
    }

    public boolean isIsDirFilterEnabled() {
        return IsDirFilterEnabled;
    }

    public void setIsDirFilterEnabled(boolean IsDirFilterEnabled) {
        this.IsDirFilterEnabled = IsDirFilterEnabled;
    }

    public int getFilterType1() {
        return FilterType1;
    }

    public void setFilterType1(int FilterType1) {
        this.FilterType1 = FilterType1;
    }

    public int getIirType1() {
        return IirType1;
    }

    public void setIirType1(int IirType1) {
        this.IirType1 = IirType1;
    }

    public int getIirBandtype1() {
        return IirBandtype1;
    }

    public void setIirBandtype1(int IirBandtype1) {
        this.IirBandtype1 = IirBandtype1;
    }

    public int getIirN1() {
        return IirN1;
    }

    public void setIirN1(int IirN1) {
        this.IirN1 = IirN1;
    }

    public int getIirFc1() {
        return IirFc1;
    }

    public void setIirFc1(int IirFc1) {
        this.IirFc1 = IirFc1;
    }

    public int getIirF01() {
        return IirF01;
    }

    public void setIirF01(int IirF01) {
        this.IirF01 = IirF01;
    }

    public int getIirAp1() {
        return IirAp1;
    }

    public void setIirAp1(int IirAp1) {
        this.IirAp1 = IirAp1;
    }

    public int getIirAs1() {
        return IirAs1;
    }

    public void setIirAs1(int IirAs1) {
        this.IirAs1 = IirAs1;
    }

    public int getFirN1() {
        return FirN1;
    }

    public void setFirN1(int FirN1) {
        this.FirN1 = FirN1;
    }

    public int getFirFc1() {
        return FirFc1;
    }

    public void setFirFc1(int FirFc1) {
        this.FirFc1 = FirFc1;
    }

    public int getFirAs1() {
        return FirAs1;
    }

    public void setFirAs1(int FirAs1) {
        this.FirAs1 = FirAs1;
    }

    public int getSensitivityOrg1() {
        return SensitivityOrg1;
    }

    public void setSensitivityOrg1(int SensitivityOrg1) {
        this.SensitivityOrg1 = SensitivityOrg1;
    }

    public int getHardwareGain1() {
        return HardwareGain1;
    }

    public void setHardwareGain1(int HardwareGain1) {
        this.HardwareGain1 = HardwareGain1;
    }

    public int getSensitivity1() {
        return Sensitivity1;
    }

    public void setSensitivity1(int Sensitivity1) {
        this.Sensitivity1 = Sensitivity1;
    }

    public Object getSensorCode1() {
        return SensorCode1;
    }

    public void setSensorCode1(Object SensorCode1) {
        this.SensorCode1 = SensorCode1;
    }

    public int getFilterType2() {
        return FilterType2;
    }

    public void setFilterType2(int FilterType2) {
        this.FilterType2 = FilterType2;
    }

    public int getIirType2() {
        return IirType2;
    }

    public void setIirType2(int IirType2) {
        this.IirType2 = IirType2;
    }

    public int getIirBandtype2() {
        return IirBandtype2;
    }

    public void setIirBandtype2(int IirBandtype2) {
        this.IirBandtype2 = IirBandtype2;
    }

    public int getIirN2() {
        return IirN2;
    }

    public void setIirN2(int IirN2) {
        this.IirN2 = IirN2;
    }

    public int getIirFc2() {
        return IirFc2;
    }

    public void setIirFc2(int IirFc2) {
        this.IirFc2 = IirFc2;
    }

    public int getIirF02() {
        return IirF02;
    }

    public void setIirF02(int IirF02) {
        this.IirF02 = IirF02;
    }

    public int getIirAp2() {
        return IirAp2;
    }

    public void setIirAp2(int IirAp2) {
        this.IirAp2 = IirAp2;
    }

    public int getIirAs2() {
        return IirAs2;
    }

    public void setIirAs2(int IirAs2) {
        this.IirAs2 = IirAs2;
    }

    public int getFirN2() {
        return FirN2;
    }

    public void setFirN2(int FirN2) {
        this.FirN2 = FirN2;
    }

    public int getFirFc2() {
        return FirFc2;
    }

    public void setFirFc2(int FirFc2) {
        this.FirFc2 = FirFc2;
    }

    public int getFirAs2() {
        return FirAs2;
    }

    public void setFirAs2(int FirAs2) {
        this.FirAs2 = FirAs2;
    }

    public int getSensitivityOrg2() {
        return SensitivityOrg2;
    }

    public void setSensitivityOrg2(int SensitivityOrg2) {
        this.SensitivityOrg2 = SensitivityOrg2;
    }

    public int getHardwareGain2() {
        return HardwareGain2;
    }

    public void setHardwareGain2(int HardwareGain2) {
        this.HardwareGain2 = HardwareGain2;
    }

    public int getSensitivity2() {
        return Sensitivity2;
    }

    public void setSensitivity2(int Sensitivity2) {
        this.Sensitivity2 = Sensitivity2;
    }

    public Object getSensorCode2() {
        return SensorCode2;
    }

    public void setSensorCode2(Object SensorCode2) {
        this.SensorCode2 = SensorCode2;
    }

    public int getPressureRange() {
        return PressureRange;
    }

    public void setPressureRange(int PressureRange) {
        this.PressureRange = PressureRange;
    }

    public int getPressureLowLimit() {
        return PressureLowLimit;
    }

    public void setPressureLowLimit(int PressureLowLimit) {
        this.PressureLowLimit = PressureLowLimit;
    }

    public int getPressureHighLimit() {
        return PressureHighLimit;
    }

    public void setPressureHighLimit(int PressureHighLimit) {
        this.PressureHighLimit = PressureHighLimit;
    }

    public int getPressureAmend() {
        return PressureAmend;
    }

    public void setPressureAmend(int PressureAmend) {
        this.PressureAmend = PressureAmend;
    }

    public int getFlowRange() {
        return FlowRange;
    }

    public void setFlowRange(int FlowRange) {
        this.FlowRange = FlowRange;
    }

    public int getFlowLowLimit() {
        return FlowLowLimit;
    }

    public void setFlowLowLimit(int FlowLowLimit) {
        this.FlowLowLimit = FlowLowLimit;
    }

    public int getFlowHighLimit() {
        return FlowHighLimit;
    }

    public void setFlowHighLimit(int FlowHighLimit) {
        this.FlowHighLimit = FlowHighLimit;
    }

    public int getFlowAmend() {
        return FlowAmend;
    }

    public void setFlowAmend(int FlowAmend) {
        this.FlowAmend = FlowAmend;
    }

    public String getLdPluginName() {
        return LdPluginName;
    }

    public void setLdPluginName(String LdPluginName) {
        this.LdPluginName = LdPluginName;
    }

    public String getLdPluginId() {
        return LdPluginId;
    }

    public void setLdPluginId(String LdPluginId) {
        this.LdPluginId = LdPluginId;
    }

    public int getShieldingRange() {
        return ShieldingRange;
    }

    public void setShieldingRange(int ShieldingRange) {
        this.ShieldingRange = ShieldingRange;
    }

    public int getTimeOffset() {
        return TimeOffset;
    }

    public void setTimeOffset(int TimeOffset) {
        this.TimeOffset = TimeOffset;
    }
}
