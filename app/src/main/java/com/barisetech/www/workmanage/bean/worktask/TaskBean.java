package com.barisetech.www.workmanage.bean.worktask;

import java.util.List;

/**
 * Created by LJH on 2018/9/18.
 */
public class TaskBean {

    /**
     * Id : 1
     * Name : tx
     * WorkPlanId : 2
     * PersonLiable : b
     * Transferor : admin
     * State : 2
     * NumberOfTransfers : 1
     * Deadline : 2013-03-03
     * isAdd : true
     * TaskSiteList : [{"Name":"ttx","SiteId":"1","Longitude":"0.1","Latitude":"0.2","WorkImageList":""}]
     */

    public int Id;
    public String Name;
    public int WorkPlanId;
    public String PersonLiable = "";
    public String Transferor;
    public int State;
    public int NumberOfTransfers;
    public String Deadline;
    public String isAdd;
    public List<TaskSiteBean> TaskSiteList;
}
