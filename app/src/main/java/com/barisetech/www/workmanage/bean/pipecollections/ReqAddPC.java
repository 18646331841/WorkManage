package com.barisetech.www.workmanage.bean.pipecollections;

import java.util.List;

/**
 * Created by LJH on 2018/8/28.
 */
public class ReqAddPC {
    /**
     * PipeCollect : [{"Id":"6","Name":"bbb","SortID":"0","Manager":"ddd","Telephone":"6s","Email":"s1s",
     * "Remark":"ss1","PipeCollects":[]}]
     * Operation : 1 参数Operation为操作符 1代表添加操作，0代表修改操作
     */

    private String Operation;
    private List<PipeCollections> PipeCollect;

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String Operation) {
        this.Operation = Operation;
    }

    public List<PipeCollections> getPipeCollect() {
        return PipeCollect;
    }

    public void setPipeCollect(List<PipeCollections> PipeCollect) {
        this.PipeCollect = PipeCollect;
    }
}
