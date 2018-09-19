package com.barisetech.www.workmanage.bean.workplan;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;

import java.util.List;

/**
 * Created by LJH on 2018/9/18.
 */
public class PlanBean {
    /**
     * Id : 1
     * Name : ss
     * Range : 0.0
     * TotalNumberOfTimes : 0
     * TimesOfCompletion : 0
     * Publisher : k
     * PersonLiable : b
     * State : 2
     * BeginTime : 2013-03-03T12:12:12
     * EndTime : 2018-03-02T12:12:12
     * PlanSiteList : null
     */

    public int Id;
    public String Name;
    public double Range;
    public int TotalNumberOfTimes;
    public int TimesOfCompletion;
    public String Publisher;
    public String PersonLiable;
    public int State;
    public String BeginTime;
    public String EndTime;
    public List<PlanSiteBean> PlanSiteList;

    @BindingAdapter("planSite")
    public static void planSite(TextView view, List<PlanSiteBean> planSiteList) {
        if (planSiteList == null || planSiteList.size() <= 0) {
            return ;
        }
        StringBuilder sb = new StringBuilder();
        for (PlanSiteBean planSiteBean : planSiteList) {
            sb.append(planSiteBean.Name).append(";");
        }
        view.setText(sb.toString());
    }
}
