package com.barisetech.www.workmanage.bean.site;

/**
 * 上传要转成String类型，专门写的这个类
 * Created by LJH on 2018/9/11.
 */
public class ReqSiteBean {
    /**
     * siteId : 3
     * name : 22
     * longitude : 22
     * latitude : 22
     * company : 
     * isAlive : true
     * EnergyRatio : 11
     * IsDwtEnabled : true
     * IsAvgEnabled : false
     * NegPeakThreshold : 1
     * FreqStart : 1
     * FreqEnd : 1
     * FreqStep : 1
     * TimeNum : 1
     * Telephone : 1237354534
     * Manager : oweir
     * IsDualSensor : false
     * IsEnabled1 : true
     * IirType1 : 1
     * IirBandtype1 : 1
     * IirN1 : 1
     * IirFc1 : 1
     * IirF01 : 1
     * IirAp1 : 1
     * IirAs1 : 1
     * IsEnabled2 : false
     * IirType2 : 1
     * IirBandtype2 : 1
     * IirN2 : 1
     * IirFc2 : 1
     * IirF02 : 1
     * IirAp2 : 1
     * IirAs2 : 1
     */

    public String siteId;
    public String name;
    public String longitude;
    public String latitude;
    public String company;
    public String isAlive;
    public String EnergyRatio;
    public String IsDwtEnabled;
    public String IsAvgEnabled;
    public String NegPeakThreshold;
    public String FreqStart;
    public String FreqEnd;
    public String FreqStep;
    public String TimeNum;
    public String Telephone;
    public String Manager;
    public String IsDualSensor;
    public String IsEnabled1;
    public String IirType1;
    public String IirBandtype1;
    public String IirN1;
    public String IirFc1;
    public String IirF01;
    public String IirAp1;
    public String IirAs1;
    public String IsEnabled2;
    public String IirType2;
    public String IirBandtype2;
    public String IirN2;
    public String IirFc2;
    public String IirF02;
    public String IirAp2;
    public String IirAs2;

    public void toSiteBean(SiteBean siteBean) {
        siteId = String.valueOf(siteBean.SiteId);
        name = String.valueOf(siteBean.Name);
        longitude = String.valueOf(siteBean.Longitude);
        latitude = String.valueOf(siteBean.Latitude);
        company = String.valueOf(siteBean.Company);
        isAlive = String.valueOf(true);
        EnergyRatio = String.valueOf(siteBean.EnergyRatio);
        IsDwtEnabled = String.valueOf(siteBean.IsDwtEnabled);
        IsAvgEnabled = String.valueOf(siteBean.IsAvgEnabled);
        NegPeakThreshold = String.valueOf(siteBean.NegPeakThreshold);
        FreqStart = String.valueOf(siteBean.FreqStart);
        FreqEnd = String.valueOf(siteBean.FreqEnd);
        FreqStep = String.valueOf(siteBean.FreqStep);
        TimeNum = String.valueOf(siteBean.TimeNum);
        Telephone = String.valueOf(siteBean.Telephone);
        Manager = String.valueOf(siteBean.Manager);
        IsDualSensor = String.valueOf(siteBean.IsDualSensor);
        IsEnabled1 = String.valueOf(true);
        IirType1 = String.valueOf(siteBean.IirType1);
        IirBandtype1 = String.valueOf(siteBean.IirBandtype1);
        IirN1 = String.valueOf(siteBean.IirN1);
        IirFc1 = String.valueOf(siteBean.IirFc1);
        IirF01 = String.valueOf(siteBean.IirF01);
        IirAp1 = String.valueOf(siteBean.IirAp1);
        IirAs1 = String.valueOf(siteBean.IirAs1);
        IsEnabled2 = String.valueOf(true);
        IirType2 = String.valueOf(siteBean.IirType2);
        IirBandtype2 = String.valueOf(siteBean.IirBandtype2);
        IirN2 = String.valueOf(siteBean.IirN2);
        IirFc2 = String.valueOf(siteBean.IirFc2);
        IirF02 = String.valueOf(siteBean.IirF02);
        IirAp2 = String.valueOf(siteBean.IirAp2);
        IirAs2 = String.valueOf(siteBean.IirAs2);
    }
}
