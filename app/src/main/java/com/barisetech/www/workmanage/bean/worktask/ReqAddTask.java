package com.barisetech.www.workmanage.bean.worktask;

import com.barisetech.www.workmanage.bean.workplan.PlanSiteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LJH on 2018/9/18.
 */
public class ReqAddTask {
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

    public String Id;
    public String Name;
    public String WorkPlanId;
    public String PersonLiable;
    public String Transferor;
    public String State;
    public String NumberOfTransfers;
    public String Deadline;
    public String isAdd;
    public List<TaskSiteBeanAdd> TaskSiteList;

    public void toSiteList(List<PlanSiteBean> planSiteBeanList) {
        if (planSiteBeanList != null && planSiteBeanList.size() > 0) {
            TaskSiteList = new ArrayList<>();
            for (PlanSiteBean planSiteBean : planSiteBeanList) {
                TaskSiteBeanAdd taskSiteBeanAdd = new TaskSiteBeanAdd();
                taskSiteBeanAdd.SiteId = planSiteBean.SiteId;
                taskSiteBeanAdd.Name = planSiteBean.Name;
                taskSiteBeanAdd.Latitude = planSiteBean.Latitude;
                taskSiteBeanAdd.Longitude = planSiteBean.Longitude;
                TaskSiteList.add(taskSiteBeanAdd);
            }
        }
    }
}
