package com.barisetech.www.workmanage.bean;

public class ReqModifyPwd {

    /**
     * MachineCode : 025f041b-b966-41d9-afce-7ea12c07169e
     * myPsd : 123456
     * myNewPsd : 123456
     */

    private String MachineCode;
    private String myPsd;
    private String myNewPsd;

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String MachineCode) {
        this.MachineCode = MachineCode;
    }

    public String getMyPsd() {
        return myPsd;
    }

    public void setMyPsd(String myPsd) {
        this.myPsd = myPsd;
    }

    public String getMyNewPsd() {
        return myNewPsd;
    }

    public void setMyNewPsd(String myNewPsd) {
        this.myNewPsd = myNewPsd;
    }
}
