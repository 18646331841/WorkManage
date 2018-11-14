package com.barisetech.www.workmanage.bean.auth;

import android.text.TextUtils;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.bean.AuthBean;

import java.util.List;

/**
 * Created by LJH on 2018/10/10.
 */
public class AuthInfo extends AuthBean {
    /**
     * ID : 1
     * Remark : 
     * LV : 3
     * ServerIP : 222.34.135.77
     * ServerPort : 8081
     * ServerName : 222.34.135.778081fc112def-52e0-48ef-be7f-052f45838929
     * SerialNumber : 89860031011580065362
     * EquipmentType : 1
     * ScreenHeight : 2118
     * ScreenWidth : 1080
     * ApplicatorName : admin
     * ApplicatorIPAdress : 10.0.0.4
     * ApplicatorTime : 2018-10-10T16:14:07
     * ApproverUserName : admin
     * ApproverState : true
     * ApproverIPAdress : 10.0.0.4
     * ApproverTime : 2018-10-10T16:14:08
     * SIMCards : [{"ID":1,"Number":"460021169370778"}]
     * IMEIList : [{"ID":1,"Number":"869071033419426"},{"ID":2,"Number":"869071033419426"}]
     */

    public int ID;
    public String Remark;
    public int LV;
    public String ServerIP;
    public String ServerPort;
    public String ServerName;
    public String SerialNumber;
    public int EquipmentType;
    public int ScreenHeight;
    public int ScreenWidth;
    public String ApplicatorName;
    public String ApplicatorIPAdress;
    public String ApplicatorTime;
    public String ApproverUserName;
    public boolean ApproverState;
    public String ApproverIPAdress;
    public String ApproverTime;
    public List<SIMCardsBean> SIMCards;
    public List<IMEIListBean> IMEIList;

    public static class SIMCardsBean {
        /**
         * ID : 1
         * Number : 460021169370778
         */

        public int ID;
        public String Number;
    }

    public static class IMEIListBean {
        /**
         * ID : 1
         * Number : 869071033419426
         */

        public int ID;
        public String Number;
    }

    /**
     * 为父类赋值
     */
    public void toSuper() {
        type = AuthBean.TYPE_USER;
        if (ApplicatorTime.contains("T")) {
            ApplicatorTime = ApplicatorTime.replace("T", " ");
        }
        title = ApplicatorTime + "  请求授权登录";
        String eType = "手机";
        if (EquipmentType == Integer.valueOf(BaseConstant.TYPE_PAD)) {
            eType = "平板";
        }
        content = "型号：" + eType + "  卡号：" + SerialNumber;
        completed = !TextUtils.isEmpty(ApproverUserName);
        state = ApproverState;
    }
}
