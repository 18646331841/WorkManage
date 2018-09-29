package com.barisetech.www.workmanage.bean.digitalizer;

import android.databinding.BindingAdapter;

import com.barisetech.www.workmanage.widget.FormView;
import com.barisetech.www.workmanage.widget.LineView;

import java.io.Serializable;

/**
 * Created by LJH on 2018/9/26.
 */
public class DigitalizerBean implements Serializable {

    /**
     * ID : 1
     * Company : BariseTech
     * Latitude : 0
     * Longitude : 0
     * HardVersion : V2.3.3
     * SoftVersion : V2.4.6-May 25 2018
     * SerialNumber : SI-1707-0-50-03
     * SimNumber : 1440019669000
     * PrimarySensor : {"ID":1,"SensorChannel":1,"AdcChannel":3,"SampleRate":1000,"IntGainEn":false,"IntGain":0,
     * "ExtGain":10,"IntFirEn":true,"IntFir":2,"ExtFir":500}
     * SencondarySensor : {"ID":1,"SensorChannel":2,"AdcChannel":3,"SampleRate":1000,"IntGainEn":false,"IntGain":0,
     * "ExtGain":10,"IntFirEn":true,"IntFir":2,"ExtFir":500}
     * Status : {"IsSubsonic1Exist":true,"IsSubsonic2Exist":true,"IsPressureExist":true,"IsFluxExist":true,
     * "OpenvpnAddr":"10.0.0.1","Temperature":55.2,"BatteryVoltage":2600}
     */

    public int ID;
    public String Name;
    public String Company;
    public float Latitude;
    public float Longitude;
    public String HardVersion;
    public String SoftVersion;
    public String SerialNumber;
    public String SimNumber;
    public PrimarySensorBean PrimarySensor;
    public SencondarySensorBean SencondarySensor;
    public StatusBean Status;

    @BindingAdapter("setText")
    public static void setText(LineView view, String s) {
        view.setText(s);
    }

    @BindingAdapter("setText")
    public static void setText(LineView view, int s) {
        view.setText(String.valueOf(s));
    }

    @BindingAdapter("setText")
    public static void setText(LineView view, float s) {
        view.setText(String.valueOf(s));
    }

    @BindingAdapter("setText")
    public static void setText(FormView view, String s) {
        view.setText(s);
    }

    @BindingAdapter("setText")
    public static void setText(FormView view, int s) {
        view.setText(String.valueOf(s));
    }

    @BindingAdapter("setText")
    public static void setText(FormView view, float s) {
        view.setText(String.valueOf(s));
    }

    @BindingAdapter("setBool")
    public static void setBool(LineView view, Boolean b) {
        view.setText(b ? "是" : "否");
    }
    @BindingAdapter("setBool")
    public static void setBool(FormView view, Boolean b) {
        view.setText(b ? "是" : "否");
    }

    public static class PrimarySensorBean implements Serializable{
        /**
         * ID : 1
         * SensorChannel : 1
         * AdcChannel : 3
         * SampleRate : 1000
         * IntGainEn : false
         * IntGain : 0
         * ExtGain : 10
         * IntFirEn : true
         * IntFir : 2
         * ExtFir : 500
         */

        public int ID;
        public int SensorChannel;
        public int AdcChannel;
        public int SampleRate;
        public boolean IntGainEn;
        public int IntGain;
        public int ExtGain;
        public boolean IntFirEn;
        public int IntFir;
        public int ExtFir;
    }

    public static class SencondarySensorBean implements Serializable{
        /**
         * ID : 1
         * SensorChannel : 2
         * AdcChannel : 3
         * SampleRate : 1000
         * IntGainEn : false
         * IntGain : 0
         * ExtGain : 10
         * IntFirEn : true
         * IntFir : 2
         * ExtFir : 500
         */

        public int ID;
        public int SensorChannel;
        public int AdcChannel;
        public int SampleRate;
        public boolean IntGainEn;
        public int IntGain;
        public int ExtGain;
        public boolean IntFirEn;
        public int IntFir;
        public int ExtFir;
    }

    public static class StatusBean implements Serializable{
        /**
         * IsSubsonic1Exist : true
         * IsSubsonic2Exist : true
         * IsPressureExist : true
         * IsFluxExist : true
         * OpenvpnAddr : 10.0.0.1
         * Temperature : 55.2
         * BatteryVoltage : 2600
         */

        public boolean IsSubsonic1Exist;
        public boolean IsSubsonic2Exist;
        public boolean IsPressureExist;
        public boolean IsFluxExist;
        public String OpenvpnAddr;
        public float Temperature;
        public int BatteryVoltage;
    }
}
