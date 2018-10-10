package com.barisetech.www.workmanage.bean.auth;

import java.util.List;

/**
 * Created by LJH on 2018/10/10.
 */
public class ReqModifyAuth {
    /**
     * ID : 2
     * Remark :
     * ServerIP : 127.0.0.1
     * ServerPort : 55559
     * ServerName :
     * SerialNumber : 123456789
     * EquipmentType : 0
     * ScreenHeight : 100
     * ScreenWidth : 100
     * ApplicatorName : admminsss
     * ApplicatorIPAdress : 127.0.0.6
     * ApplicatorTime : 2013-03-03 12:12:12
     * ApproverUserName : user
     * ApproverIPAdress : 127.0.0.9
     * ApproverState : true
     * ApproverTime : 2013-03-03 12:12:12
     * SIMCardList : [{"ID":"2","Number":"55"}]
     * IMEIList : [{"ID":"2","Number":"55"}]
     * isAdd : false
     */

    public String ID;
    public String Remark;
    public String ServerIP;
    public String ServerPort;
    public String ServerName;
    public String SerialNumber;
    public String EquipmentType;
    public String ScreenHeight;
    public String ScreenWidth;
    public String ApplicatorName;
    public String ApplicatorIPAdress;
    public String ApplicatorTime;
    public String ApproverUserName;
    public String ApproverIPAdress;
    public String ApproverState;
    public String ApproverTime;
    public String isAdd;
    public List<SIMCardListBean> SIMCardList;
    public List<IMEIListBean> IMEIList;

    public static class SIMCardListBean {
        /**
         * ID : 2
         * Number : 55
         */

        public String ID;
        public String Number;
    }

    public static class IMEIListBean {
        /**
         * ID : 2
         * Number : 55
         */

        public String ID;
        public String Number;
    }
}
