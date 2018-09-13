package com.barisetech.www.workmanage.bean.map.pipe;

/**
 * Created by LJH on 2018/9/12.
 */
public class ReqPipeTrack {
    /**
     * MachineCode : ${token}
     * PipeId : 0
     */

    private String MachineCode;
    private String PipeId;

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String MachineCode) {
        this.MachineCode = MachineCode;
    }

    public String getPipeId() {
        return PipeId;
    }

    public void setPipeId(String PipeId) {
        this.PipeId = PipeId;
    }
}
