package com.barisetech.www.workmanage.bean.workplan;

/**
 * 服务器接口定义的url地址要传的东西
 * 36位guid后，第一位是否按时间取，0为假
 *   随后一位是否按人员取，0为假
 *   后8位为开始时间20100306
 *   后8位为结束时间
 *   后一位：状态，0为假
 *   后一位：是否是责任人，0为假
 *   后位string用户名
 *
 * http://localhost:54725/api/PipeWorkPlan/f646e0e1-28b6-4b67-9af1-0111c9d9d670 11201103032020030320admin
 *
 * Created by LJH on 2018/9/18.
 */
public class ReqPlanNum {

    /**
     * 实际填token
     */
    public String guid;

    /**
     * 按时间取 0为假，1为真
     */
    public int isTime;

    /**
     * 是否按人员取 0为假，1为真
     */
    public int isUser;

    /**
     * 开始时间， 格式20100306
     */
    public String startTime;

    /**
     * 结束时间， 格式与开始时间一样
     */
    public String endTime;

    /**
     * 状态，1.已完成 2.未完成 3.超时 4.超额
     */
    public String status;

    /**
     * 是否责任人 0为假， 1为真
     */
    public int isPersonliable;

    /**
     * 责任人名
     */
    public String personliable;

    public String toUrlString() {
        StringBuilder sb = new StringBuilder();
        sb.append(guid)
                .append(isTime)
                .append(isUser)
                .append(startTime)
                .append(endTime)
                .append(status)
                .append(isPersonliable)
                .append(personliable);

        return sb.toString();
    }
}
