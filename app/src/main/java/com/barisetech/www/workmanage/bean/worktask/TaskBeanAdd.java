package com.barisetech.www.workmanage.bean.worktask;

import java.util.List;

/**
 * 专门Add时使用
 * Created by LJH on 2018/9/18.
 */
public class TaskBeanAdd {

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

    private String Id;
    private String Name;
    private String WorkPlanId;
    private String PersonLiable;
    private String Transferor;
    private String State;
    private String NumberOfTransfers;
    private String Deadline;
    private String isAdd;
    private List<TaskSiteBean> TaskSiteList;
}
