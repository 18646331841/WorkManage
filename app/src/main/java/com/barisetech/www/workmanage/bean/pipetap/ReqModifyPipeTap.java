package com.barisetech.www.workmanage.bean.pipetap;

/**
 * Created by LJH on 2018/10/10.
 */
public class ReqModifyPipeTap {
    /**
     * isApprove : false
     * ID : 1
     * Remark :
     * Applicant : k
     * ApplicationTime : 2015-03-03 12:12:12
     * SiteID : 1
     * SiteName : 00
     * TapSwitch : false
     * LV : 2
     * Approver :
     * ApproverState :
     * ApproverTime :
     */

    public String isApprove;
    public String ID;
    public String Remark;
    public String Applicant;
    public String ApplicationTime;
    public String SiteID;
    public String SiteName;
    public String TapSwitch;
    public String LV;
    public String Approver;
    public String ApproverState;
    public String ApproverTime;

    public void toBean(PipeTapInfo pipeTapInfo) {
        ID = String.valueOf(pipeTapInfo.ID);
        Remark = pipeTapInfo.Remark;
        Applicant = pipeTapInfo.Applicant;
        ApplicationTime = pipeTapInfo.ApplicationTime;
        SiteID = String.valueOf(pipeTapInfo.SiteID);
        SiteName = pipeTapInfo.SiteName;
        TapSwitch = String.valueOf(pipeTapInfo.TapSwitch);
        LV = String.valueOf(pipeTapInfo.LV);
        Approver = pipeTapInfo.Approver;
        ApproverState = String.valueOf(pipeTapInfo.ApproverState);
        ApproverTime = pipeTapInfo.ApproverTime;
    }
}
