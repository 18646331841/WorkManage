package com.barisetech.www.workmanage.bean.digitalizer;

/**
 * Created by LJH on 2018/9/26.
 */
public class ReqModifyDigitalizer {
    /**
     * ID : 2
     * Company : BariseTech
     * Name : 测试站点B(ID00002)
     * Latitude : 0
     * Longitude : 0
     * HardVersion : V2.3.3
     * SoftVersion : V2.4.6-May 25 2018
     * SerialNumber : SI-1707-0-50-03
     * SimNumber : 1440019669000
     * PrimarySensor : {"ID":2,"SensorChannel":1,"AdcChannel":3,"SampleRate":1000,"IntGainEn":false,"IntGain":0,
     * "ExtGain":10,"IntFirEn":true,"IntFir":2,"ExtFir":500}
     * SencondarySensor : {"ID":2,"SensorChannel":2,"AdcChannel":3,"SampleRate":1000,"IntGainEn":false,"IntGain":0,
     * "ExtGain":10,"IntFirEn":true,"IntFir":2,"ExtFir":500}
     * Status : {"IsSubsonic1Exist":false,"IsSubsonic2Exist":true,"IsPressureExist":true,"IsFluxExist":true,
     * "OpenvpnAddr":"10.0.0.1","Temperature":55.2,"BatteryVoltage":2600}
     * PressureSampleRate : 100
     * FlowSampleSampleRate : 10
     * TemperatureSampleRate : 10
     */

    public int ID;
    public String Company;
    public String Name;
    public float Latitude;
    public float Longitude;
    public String HardVersion;
    public String SoftVersion;
    public String SerialNumber;
    public String SimNumber;
    public DigitalizerBean.PrimarySensorBean PrimarySensor;
    public DigitalizerBean.SencondarySensorBean SencondarySensor;
    public DigitalizerBean.StatusBean Status;
    public String PressureSampleRate = "";
    public String FlowSampleSampleRate = "";
    public String TemperatureSampleRate = "";

    public void toBean(DigitalizerBean digitalizerBean) {
        ID = digitalizerBean.ID;
        Company = digitalizerBean.Company;
        Name = digitalizerBean.Name;
        Latitude = digitalizerBean.Latitude;
        Longitude = digitalizerBean.Longitude;
        HardVersion = digitalizerBean.HardVersion;
        SoftVersion = digitalizerBean.SoftVersion;
        SerialNumber = digitalizerBean.SerialNumber;
        SimNumber = digitalizerBean.SimNumber;
        PrimarySensor = digitalizerBean.PrimarySensor;
        SencondarySensor = digitalizerBean.SencondarySensor;
        Status = digitalizerBean.Status;
    }
}
