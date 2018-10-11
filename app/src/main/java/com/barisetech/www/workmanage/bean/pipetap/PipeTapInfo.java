package com.barisetech.www.workmanage.bean.pipetap;

import android.text.TextUtils;

import com.barisetech.www.workmanage.bean.AuthBean;

/**
 * Created by LJH on 2018/10/10.
 */
public class PipeTapInfo extends AuthBean {
    /**
     * ID : 2
     * Remark :
     * Company : null
     * Applicant : user
     * ApplicationTime : 2018-10-10T17:36:25
     * SiteID : 1
     * SiteName : 00
     * TapSwitch : true
     * LV : 2
     * Approver : null
     * ApproverState : false
     * ApproverTime : 2013-03-03T12:12:12
     */

    public int ID;
    public String Remark;
    public String Company;
    public String Applicant;
    public String ApplicationTime;
    public int SiteID;
    public String SiteName;
    public boolean TapSwitch;
    public int LV = 2;
    public String Approver;
    public boolean ApproverState;
    public String ApproverTime;

    /**
     * 给父类赋值
     */
    public void toSuper() {
        type = AuthBean.TYPE_PIPETAP;
        String tapSwitch = TapSwitch ? "打开" : "关闭";
        title = ApplicationTime + "  请求" + tapSwitch + SiteName + "阀门";
        content = "备注：" + Remark;
        completed = !TextUtils.isEmpty(Approver);
        state = ApproverState;
    }
}
