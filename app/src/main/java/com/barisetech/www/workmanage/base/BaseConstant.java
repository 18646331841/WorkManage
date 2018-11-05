package com.barisetech.www.workmanage.base;

/**
 * Created by LJH on 2018/8/7.
 */
public class BaseConstant {

    /**
     * 部分页面跳转时，需要从网络拿数据时的对象名
     */
    public static final String DATA_REQUEST_NAME = "requestNet";

    /**
     * 用户角色权限，分三类
     */
    public static final String ROLE_SUPER_ADMINS = "SuperAdmins";
    public static final String ROLE_ADMINS = "Admins";
    public static final String ROLE_USERS = "Users";

    public static final String AUTH_HEADER = "Authorization";
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
    public static final int TYPE_INCIDENT_DIGI_ONLINE = 2;
    public static final int TYPE_INCIDENT_DIGI_OFFLINE = 3;
    public static final int TYPE_INCIDENT_DIGI_TIME = 4;
    public static final int TYPE_INCIDENT_SENSOR_OFFLINE = 5;
    public static final int TYPE_INCIDENT_ISOLATOR_OFFLINE = 6;
    public static final int TYPE_INCIDENT_UNKNOWN = 7;
    public static final String TYPES_INCIDENT = "2,3,4,5,6,7";


    /**
     * 巡线计划类型
     */
    public static final int TYPE_PLAN_ALL = 0;
    public static final int TYPE_PLAN_UNCOMPLETED = 1;
    public static final int TYPE_PLAN_COMPLETED = 2;
    public static final int TYPE_PLAN_TIMEOUT = 3;
    public static final int TYPE_PLAN_EXCESS = 4;


    /**
     * 巡线任务打卡状态
     */
    public static final String[] STATUS_TASK = new String[]{"打卡", "补卡", "未打卡", "已打卡"};
    public static final int STATUS_COMPLETED = 1;
    public static final int STATUS_OFFLINE = 2;
    public static final int STATUS_UNCOMPLETED = 3;
    public static final int STATUS_FAIL = 4;
    /**
     * SharedPreferences keys
     */
    //指纹登录 boolean true 启用指纹
    public static final String SP_LOGIN_FP = "login_FP";
    //登录用户account
    public static final String SP_ACCOUNT = "account";
    //登录用户account 需求设计如此，只能本地保存密码
    public static final String SP_PASSWORD = "password";
    //登录用户权限
    public static final String SP_ROLE = "role";
    //账户所属公司
    public static final String SP_COMPANY = "company";
    //用户token
    public static final String SP_TOKEN = "token";
    //第一次登录时间
    public static final String SP_FIRST_LOGIN_TIME = "first_login_time";
    //access token
    public static final String SP_ACCESS_TOKEN = "access_token";
    //refresh token
    public static final String SP_REFRESH_TOKEN = "refresh_token";
    //Ip port, value = ip_port
    public static final String SP_IP_PORT = "ip_port";
    //上次请求新消息的时间
    public static final String SP_LAST_TIME_NEWINFO = "last_time";
    //免打扰开始时间
    public static final String NOT_DISTURB_START = "not_disturb_start";
    //免打扰结束时间
    public static final String NOT_DISTURB_END = "not_disturb_end";
    //免打扰开启标识
    public static final String NOT_DISTURB_OPEN = "not_disturb_open";
    //声音开启
    public static final String SOUND_OPEN = "sound_open";
    //震动开启
    public static final String SHOCK_OPEN = "shock_open";
    //事件通知类型
    public static final String SP_INCIDENT_TYPES = "incident_type";

    /**
     * progress
     */
    //显示progress
    public static final String PROGRESS_SHOW = "show_progress";
    //关闭progress
    public static final String PROGRESS_CLOSE = "close_progress";

    /**
     * 定时通知时间
     */
    //刷新token 10s
    public static final int REFRESH_TOKEN_TIME = 12000;
    //警报10s
    public static final int ALARM_TIME = 10000;
    //事件20s
    public static final int INCIDENT_TIME = 20000;
    //新闻60s
    public static final int NEWS_TIME = 60000;
    //警报分析30s
    public static final int ALARM_ANALYSIS_TIME = 30000;
    //工作计划30s
    public static final int PLAN_TIME = 30000;
    //授权10s
    public static final int AUTH_TIME = 10000;

    public static final String NOTIFY_TAG = "notify";

    public static final String ALARM_CHANNEL = "警报";
    public static final String INCIDENT_CHANNEL = "事件";
    public static final String NEWS_CHANNEL = "新闻";
    public static final String ALARM_ANALYSIS_CHANNEL = "警报分析";
    public static final String PLAN_CHANNEL = "计划";
    public static final String AUTH_CHANNEL = "授权";

    /**
     * 设备类型
     */
    public static final String TYPE_PAD = "2";
    public static final String TYPE_PHONE = "1";
}
