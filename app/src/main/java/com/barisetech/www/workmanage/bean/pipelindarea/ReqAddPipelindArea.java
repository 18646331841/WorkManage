package com.barisetech.www.workmanage.bean.pipelindarea;

public class ReqAddPipelindArea {

    /**
     * isAdd : false
     * id : 1
     * IsEnabled : false
     * Type : 0
     * StartDistance : 0
     * EndDistance : 15
     * PipeId : 2
     * Remark : 0
     */

    private String isAdd;
    private String id;
    private String IsEnabled;
    private String Type;
    private String StartDistance;
    private String EndDistance;
    private String PipeId;
    private String Remark;

    public String getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(String isAdd) {
        this.isAdd = isAdd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsEnabled() {
        return IsEnabled;
    }

    public void setIsEnabled(String IsEnabled) {
        this.IsEnabled = IsEnabled;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getStartDistance() {
        return StartDistance;
    }

    public void setStartDistance(String StartDistance) {
        this.StartDistance = StartDistance;
    }

    public String getEndDistance() {
        return EndDistance;
    }

    public void setEndDistance(String EndDistance) {
        this.EndDistance = EndDistance;
    }

    public String getPipeId() {
        return PipeId;
    }

    public void setPipeId(String PipeId) {
        this.PipeId = PipeId;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public void toBean(PipeLindAreaInfo info) {
        id = String.valueOf(info.getId());
        IsEnabled = String.valueOf(info.isIsEnabled());
        Type = String.valueOf(info.getType());
        StartDistance = String.valueOf(info.getStartDistance());
        EndDistance = String.valueOf(info.getEndDistance());
        PipeId = String.valueOf(info.getPipeId());
        Remark = info.getRemark();
    }
}
