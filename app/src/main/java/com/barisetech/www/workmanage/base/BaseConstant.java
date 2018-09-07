package com.barisetech.www.workmanage.base;

/**
 * Created by LJH on 2018/8/7.
 */
public class BaseConstant {

    /**
     * 用户角色权限，分三类
     */
    public static final String ROLE_SUPER_ADMINS = "SuperAdmins";
    public static final String ROLE_ADMINS = "Admins";
    public static final String ROLE_USERS = "Users";

    /**
     * int型用户等级
     */
    public static final int LEVEL_SUPER_ADMINS = 3;
    public static final int LEVEL_ADMINS = 2;
    public static final int LEVEL_USERS = 1;

    /**
     * 警报分析原因类型
     */
    public static final int REASON_WAIT = 1;
    public static final int REASON_TEST = 2;
    public static final int REASON_NORMAL = 3;
    public static final int REASON_MISINFO = 4;
    public static final int REASON_DEVICE_FAULT = 5;
    public static final int REASON_NET_FAULT = 6;

    /**
     * 事件类型
     */
    public static final int TYPE_INCIDENT_ALL = 1;
    public static final int TYPE_INCIDENT_DIGI_OFFLINE = 2;
    public static final int TYPE_INCIDENT_DIGI_ONLINE = 3;
    public static final int TYPE_INCIDENT_DIGI_TIME = 4;
    public static final int TYPE_INCIDENT_SENSOR_OFFLINE = 5;
    public static final int TYPE_INCIDENT_ISOLATOR_OFFLINE = 6;
    public static final int TYPE_INCIDENT_UNKNOWN = 7;

    /**
     * SharedPreferences keys
     */
    //指纹登录
    public static final String SP_LOGIN_FP = "login_FP";
    //登录用户account
    public static final String SP_ACCOUNT = "account";
    //登录用户权限
    public static final String SP_ROLE = "role";
    //用户token
    public static final String SP_TOKEN = "token";
    //Ip port, value = ip_port
    public static final String SP_IP_PORT = "ip_port";
    //上次请求新消息的时间
    public static final String SP_LAST_TIME_NEWINFO = "last_time";

    /**
     * progress
     */
    //显示progress
    public static final String PROGRESS_SHOW = "show_progress";
    //关闭progress
    public static final String PROGRESS_CLOSE = "close_progress";
}
