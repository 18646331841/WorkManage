package com.barisetech.www.workmanage.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.AccessTokenInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAllAlarmAnalysis;
import com.barisetech.www.workmanage.bean.auth.AuthInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAllAuth;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.incident.ReqAllIncident;
import com.barisetech.www.workmanage.bean.incident.ReqIncidentSelectItem;
import com.barisetech.www.workmanage.bean.pipetap.PipeTapInfo;
import com.barisetech.www.workmanage.bean.pipetap.ReqAllPipeTap;
import com.barisetech.www.workmanage.bean.workplan.PlanBean;
import com.barisetech.www.workmanage.bean.workplan.ReqAllPlan;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.AlarmAnalysisService;
import com.barisetech.www.workmanage.http.api.AlarmService;
import com.barisetech.www.workmanage.http.api.AuthService;
import com.barisetech.www.workmanage.http.api.IncidentService;
import com.barisetech.www.workmanage.http.api.NewsService;
import com.barisetech.www.workmanage.http.api.PipeTapService;
import com.barisetech.www.workmanage.http.api.PlanService;
import com.barisetech.www.workmanage.http.api.TokenService;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.SystemUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.view.MainActivity;
import com.barisetech.www.workmanage.view.fragment.AlarmAnalysisListFragment;
import com.barisetech.www.workmanage.view.fragment.AlarmListFragment;
import com.barisetech.www.workmanage.view.fragment.IncidentListFragment;
import com.barisetech.www.workmanage.view.fragment.Messagefragment;
import com.barisetech.www.workmanage.view.fragment.NewsListFragment;
import com.barisetech.www.workmanage.view.fragment.PlanListFragment;
import com.barisetech.www.workmanage.view.fragment.my.AuthListFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Created by LJH on 2018/9/25.
 */
public class MyNotifyService extends Service {
    private static final String TAG = "MyNotifyService";
    private TokenService tokenService;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    private NotificationManager notificationManager;
    private String channelId = "work_manager";
    //    private final String channelName = BaseApplication.getInstance().getResources().getString(R.string.app_name);
    private final int alarmId = 1;
    private final int incidentId = 2;
    private final int newsId = 3;
    private final int alarmAnalysisId = 4;
    private final int planId = 5;
    private final int authId = 6;
    private int channelCount;

    private MyBinder myBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate");
        startForeground(1, new Notification());

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("Cache-Control", "no-cache");

        tokenService = HttpService.getInstance().buildRetrofit(headerMap).create(TokenService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //5.0以下保活
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {
        /**
         * 开启轮询任务
         */
        public void startInterval() {
            MyNotifyService.this.startInterval();
        }

        /**
         * 关闭轮询任务
         */
        public void stopInterval() {
            MyNotifyService.this.stopInterval();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopInterval();
        gcEnv();
    }

    /**
     * 5.0以下保活
     */
    private void gcEnv() {
        Intent serviceTo = new Intent(getApplicationContext(), MyNotifyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceTo);
        } else {
            startService(serviceTo);
        }
    }

    private void buildNotify(int id, String channelName, String title, String content) {
        buildNotify(id, channelName, title, content, null);
    }


        /**
         * 发送通知
         *
         * @param id
         * @param title
         * @param content
         */
    private void buildNotify(int id, String channelName, String title, String content, Object arg) {
        boolean isSound = SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SOUND_OPEN, true);
        boolean isVibrate = SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SHOCK_OPEN, true);

        buildNotificationChannel(channelName, isSound, isVibrate);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setContentTitle(title)
                .setContentText(content)
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        int defaults = Notification.DEFAULT_LIGHTS;
        //设置通知方式，声音，震动，呼吸灯等效果
        if (isSound) {
            defaults |= Notification.DEFAULT_SOUND;
        }

        if (isVibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        builder.setDefaults(defaults);

        Log.d(TAG, "isSound---" + isSound);
        Log.d(TAG, "isVibrate---" + isVibrate);
        Log.d(TAG, "defaults---" + defaults);

        Bundle bundle = new Bundle();
        bundle.putString("tag", Messagefragment.TAG);
        switch (id) {
            case alarmId:
                bundle.putString(BaseConstant.NOTIFY_TAG, AlarmListFragment.TAG);
                break;
            case incidentId:
                bundle.putString(BaseConstant.NOTIFY_TAG, IncidentListFragment.TAG);
                break;
            case newsId:
                bundle.putString(BaseConstant.NOTIFY_TAG, NewsListFragment.TAG);
                break;
            case alarmAnalysisId:
                bundle.putString(BaseConstant.NOTIFY_TAG, AlarmAnalysisListFragment.TAG);
                break;
            case planId:
                bundle.putString(BaseConstant.NOTIFY_TAG, PlanListFragment.TAG);
                break;
            case authId:
                bundle.putString(BaseConstant.NOTIFY_TAG, AuthListFragment.TAG);
                if (arg instanceof String) {
                    bundle.putString("arg2", (String) arg);
                }
                break;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, channelCount, intent, PendingIntent
                .FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(channelName, id, builder.build());
    }

    /**
     * 8.0适配
     *
     * @param isSound
     * @param isVibrate
     */
    private void buildNotificationChannel(String channelName, boolean isSound, boolean isVibrate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel old = notificationManager.getNotificationChannel(channelId);
//            if (old != null) {
//                notificationManager.deleteNotificationChannel(old.getId());
//            }
            deleteChannel(channelName);
            channelCount++;
            channelId += channelCount;
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_MAX);
            channel.enableVibration(isVibrate);
            channel.enableLights(true);
            if (isSound) {
                channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.europa),
                        Notification.AUDIO_ATTRIBUTES_DEFAULT);
            } else {
                channel.setSound(null, null);
            }
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deleteChannel(String channelName) {
        List<NotificationChannel> notificationChannels = notificationManager.getNotificationChannels();
        if (notificationChannels != null && notificationChannels.size() > 0) {
            for (NotificationChannel channel : notificationChannels) {
                if (channel.getName().equals(channelName)) {
                    notificationManager.deleteNotificationChannel(channel.getId());
                }
            }
        }
    }

    private void startInterval() {
        refreshTokenInterval();
        alarmInterval();
        incidentInterval();
        newsInterval();
        alarmAnalysisInterval();
        planInterval();
        authInterval();
    }

    private void stopInterval() {
        if (mDisposable.isDisposed()) {
            LogUtil.d(TAG, "dispose disposableSize = " + mDisposable.size());
            mDisposable.dispose();
            mDisposable.clear();
        }
    }

    private void refreshTokenInterval() {
        Disposable disposable = Observable.interval(0, BaseConstant.REFRESH_TOKEN_TIME, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> {
                    LogUtil.d(TAG, "刷新token定时任务---" + aLong);
                    String refreshToken = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_REFRESH_TOKEN, "");
                    if (TextUtils.isEmpty(refreshToken)) {
                        return;
                    }
                    tokenService.refreshTokenInfo(BaseConstant.SP_REFRESH_TOKEN, refreshToken)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(accessTokenInfo -> {
                                if (accessTokenInfo != null) {
                                    LogUtil.d(TAG, "刷新token定时任务成功---" + accessTokenInfo.toString());
                                    SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_REFRESH_TOKEN,
                                            accessTokenInfo.getRefreshToken());
                                    SharedPreferencesUtil.getInstance().setString(BaseConstant.SP_ACCESS_TOKEN,
                                            accessTokenInfo.getAccessToken());
                                }
                            }, throwable -> {
                                LogUtil.e(TAG, "刷新token定时任务失败---" + throwable.getMessage());
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mDisposable.add(disposable);
    }

    private void alarmInterval() {
        Disposable disposable = Observable.interval(0, BaseConstant.ALARM_TIME, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> {
                    LogUtil.d(TAG, "警报定时任务---" + aLong);
                    String token = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_TOKEN, "");
                    if (TextUtils.isEmpty(token)) {
                        return;
                    }
                    ReqAllAlarm reqAllAlarm = new ReqAllAlarm();
                    reqAllAlarm.setMachineCode(token);
                    reqAllAlarm.setIsAllAlarm("true");
                    reqAllAlarm.setStartIndex("0");
                    reqAllAlarm.setNumberOfRecords("1");
                    reqAllAlarm.setGetByTimeDiff("true");
                    long endTime = System.currentTimeMillis();
                    long startTime = endTime - BaseConstant.ALARM_TIME;
                    reqAllAlarm.setStartTime(TimeUtil.ms2Date(startTime));
                    reqAllAlarm.setEndTime(TimeUtil.ms2Date(endTime));

                    AlarmService alarmService = HttpService.getInstance().buildJsonRetrofit().create(AlarmService.class);
                    alarmService.getAllAlarm(reqAllAlarm)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ObserverCallBack<List<AlarmInfo>>() {
                                @Override
                                protected void onThrowable(Throwable e) {
                                    LogUtil.d(TAG, "警报请求失败---" + e.getMessage());
                                }

                                @Override
                                protected void onFailure(BaseResponse response) {
                                    LogUtil.d(TAG, "警报请求失败---" + response.Code + "---message---" + response.Message);
                                }

                                @Override
                                protected void onSuccess(List<AlarmInfo> response) {
                                    if (response != null && response.size() > 0) {
                                        buildNotify(alarmId, BaseConstant.ALARM_CHANNEL, "有新警报信息", String.valueOf
                                                (response.size() + "个"));
                                    }
                                }
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mDisposable.add(disposable);
    }

    private void incidentInterval() {
        Disposable disposable = Observable.interval(0, BaseConstant.INCIDENT_TIME, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> {

                    if (isNotDistub()) {
                        return;
                    }

                    LogUtil.d(TAG, "事件定时任务---" + aLong);
                    String token = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_TOKEN, "");
                    if (TextUtils.isEmpty(token)) {
                        return;
                    }

                    ReqIncidentSelectItem reqIncidentSelectItem = new ReqIncidentSelectItem();
                    long endTime = System.currentTimeMillis();
                    long startTime = endTime - BaseConstant.INCIDENT_TIME;
                    reqIncidentSelectItem.setMStartTime(TimeUtil.ms2Date(startTime));
//                    reqIncidentSelectItem.setMStartTime("1970-01-01 00:00:00"); 测试使用
                    reqIncidentSelectItem.setMEndTime(TimeUtil.ms2Date(endTime));
                    reqIncidentSelectItem.setTimeQueryChecked("true");
                    reqIncidentSelectItem.setSiteIdQueryChecked("false");
                    reqIncidentSelectItem.setSiteID("0");
                    reqIncidentSelectItem.setMIncidentType(String.valueOf(BaseConstant.TYPE_INCIDENT_ALL));

                    ReqAllIncident reqAllIncident = new ReqAllIncident();
                    reqAllIncident.setMachineCode(token);
                    reqAllIncident.setStartIndex("0");
                    reqAllIncident.setNumberOfRecord("20");
                    reqAllIncident.setIncidentSelectItem(reqIncidentSelectItem);

                    IncidentService incidentService = HttpService.getInstance().buildJsonRetrofit().create(IncidentService.class);
                    incidentService.getAllIncident(reqAllIncident)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ObserverCallBack<List<IncidentInfo>>() {
                                @Override
                                protected void onThrowable(Throwable e) {
                                    LogUtil.d(TAG, "事件请求失败---" + e.getMessage());
                                }

                                @Override
                                protected void onFailure(BaseResponse response) {
                                    LogUtil.d(TAG, "事件请求失败---" + response.Code + "---message---" + response.Message);
                                }

                                @Override
                                protected void onSuccess(List<IncidentInfo> response) {
                                    if (response != null && response.size() > 0) {
                                        //判断通知哪种类型的事件
                                        String typeSP = SharedPreferencesUtil.getInstance().getString(BaseConstant
                                                .SP_INCIDENT_TYPES, BaseConstant.TYPES_INCIDENT);
                                        int count = 0;
                                        for (IncidentInfo incidentInfo : response) {
                                            if (typeSP.contains(String.valueOf(incidentInfo.getType()))) {
                                                count++;
                                            }
                                        }
                                        if (count > 0) {
                                            buildNotify(incidentId, BaseConstant.INCIDENT_CHANNEL, "有新事件信息", String
                                                    .valueOf(count + "个"));
                                        } else {
                                            LogUtil.d(TAG, "没有需要通知的事件类型");
                                        }
                                    }
                                }
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mDisposable.add(disposable);
    }

    private void newsInterval() {
        //TODO 新闻接口有问题，没有时间筛选条件，暂时无法实现
    }

    private void alarmAnalysisInterval() {
        Disposable disposable = Observable.interval(0, BaseConstant.ALARM_ANALYSIS_TIME, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> {
                    LogUtil.d(TAG, "警报分析定时任务---" + aLong);
                    String token = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_TOKEN, "");
                    if (TextUtils.isEmpty(token)) {
                        return;
                    }

                    long endTime = System.currentTimeMillis();
                    long startTime = endTime - BaseConstant.ALARM_ANALYSIS_TIME;

                    ReqAllAlarmAnalysis reqAllAlarmAnalysis = new ReqAllAlarmAnalysis();
                    reqAllAlarmAnalysis.setMachineCode(token);
                    reqAllAlarmAnalysis.setIsGetAll("false");
                    reqAllAlarmAnalysis.setGetByid("false");
                    reqAllAlarmAnalysis.setAlarmId("-1");
                    reqAllAlarmAnalysis.setAlarmAnalysisId("-1");
                    reqAllAlarmAnalysis.setGetByMy("false");
                    reqAllAlarmAnalysis.setStartTime(TimeUtil.ms2Date(startTime));
                    reqAllAlarmAnalysis.setEndTime(TimeUtil.ms2Date(endTime));
                    reqAllAlarmAnalysis.setStartIndex("0");
                    reqAllAlarmAnalysis.setNumberOfRecords("1");
                    reqAllAlarmAnalysis.setType("0");
                    AlarmAnalysisService alarmAnalysisService = HttpService.getInstance().buildJsonRetrofit().create
                            (AlarmAnalysisService.class);
                    alarmAnalysisService.getAllAnalysis(reqAllAlarmAnalysis)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ObserverCallBack<List<AlarmAnalysis>>() {
                                @Override
                                protected void onThrowable(Throwable e) {
                                    LogUtil.d(TAG, "警报分析请求失败---" + e.getMessage());
                                }

                                @Override
                                protected void onFailure(BaseResponse response) {
                                    LogUtil.d(TAG, "警报分析请求失败---" + response.Code + "---message---" + response.Message);
                                }

                                @Override
                                protected void onSuccess(List<AlarmAnalysis> response) {
                                    if (response != null && response.size() > 0) {
                                        buildNotify(alarmAnalysisId, BaseConstant.ALARM_ANALYSIS_CHANNEL, "有新警报分析信息",
                                                String.valueOf(response.size() + "个"));
                                    }
                                }
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mDisposable.add(disposable);
    }

    private void planInterval() {
        Disposable disposable = Observable.interval(0, BaseConstant.PLAN_TIME, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> {
                    LogUtil.d(TAG, "计划定时任务---" + aLong);
                    String token = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_TOKEN, "");
                    if (TextUtils.isEmpty(token)) {
                        return;
                    }

                    long endTime = System.currentTimeMillis();
                    long startTime = endTime - BaseConstant.PLAN_TIME;

                    ReqAllPlan reqAllPlan = new ReqAllPlan();
                    reqAllPlan.MachineCode = token;
                    reqAllPlan.isGetAll = "true";
                    reqAllPlan.State = "0";
                    reqAllPlan.startIndex = "0";
                    reqAllPlan.numberOfRecords = "1";
                    reqAllPlan.TimeQueryChecked = "true";
                    reqAllPlan.mStartTime = TimeUtil.ms2Date(startTime);
                    reqAllPlan.mEndTime = TimeUtil.ms2Date(endTime);
                    reqAllPlan.PesonChecked = "true";
                    reqAllPlan.Publisher = "admin";
                    String role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
                    if (!TextUtils.isEmpty(role)) {
                        if (!role.equals(BaseConstant.ROLE_SUPER_ADMINS) && !role.equals(BaseConstant.ROLE_ADMINS)) {
                            reqAllPlan.PersonLiable = SharedPreferencesUtil.getInstance().getString(BaseConstant
                                    .SP_ACCOUNT, "");
                        }
                    }

                    PlanService planService = HttpService.getInstance().buildJsonRetrofit().create(PlanService.class);
                    planService.getAllPlan(reqAllPlan)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ObserverCallBack<List<PlanBean>>() {
                                @Override
                                protected void onThrowable(Throwable e) {
                                    LogUtil.d(TAG, "计划请求失败---" + e.getMessage());
                                }

                                @Override
                                protected void onFailure(BaseResponse response) {
                                    LogUtil.d(TAG, "计划请求失败---" + response.Code + "---message---" + response.Message);
                                }

                                @Override
                                protected void onSuccess(List<PlanBean> response) {
                                    if (response != null && response.size() > 0) {
                                        buildNotify(planId, BaseConstant.PLAN_CHANNEL, "有新计划信息", String.valueOf
                                                (response.size() + "个"));
                                    }
                                }
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mDisposable.add(disposable);
    }

    private void authInterval() {
        Disposable disposable = Observable.interval(0, BaseConstant.AUTH_TIME, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> {
                    if (!SystemUtil.isAdmin()) {
                        return;
                    }

                    LogUtil.d(TAG, "授权定时任务---" + aLong);
                    String token = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_TOKEN, "");
                    if (TextUtils.isEmpty(token)) {
                        return;
                    }

                    long endTime = System.currentTimeMillis();
                    long startTime = endTime - BaseConstant.PLAN_TIME;

                    ReqAllPipeTap reqAllPipeTap = new ReqAllPipeTap();
                    reqAllPipeTap.MachineCode = token;
                    reqAllPipeTap.isGetAll = "false";
                    reqAllPipeTap.mStartTime = TimeUtil.ms2Date(startTime);
                    reqAllPipeTap.mEndTime = TimeUtil.ms2Date(endTime);
                    reqAllPipeTap.startIndex = String.valueOf("0");
                    reqAllPipeTap.numberOfRecords = String.valueOf("1");
                    reqAllPipeTap.TimeQueryChecked = "true";
                    reqAllPipeTap.PesonChecked = "false";
                    reqAllPipeTap.State = "0";
                    reqAllPipeTap.Applicant = "";
                    reqAllPipeTap.Approver = "";

//                    pipeTapService.getAll(reqAllPipeTap)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribeWith(new ObserverCallBack<List<PipeTapInfo>>() {
//                                @Override
//                                protected void onThrowable(Throwable e) {
//                                    LogUtil.d(TAG, "授权阀门请求失败---" + e.getMessage());
//                                }
//
//                                @Override
//                                protected void onFailure(BaseResponse response) {
//                                    LogUtil.d(TAG, "授权阀门请求失败---" + response.Code + "---message---" + response.Message);
//                                }
//
//                                @Override
//                                protected void onSuccess(List<PipeTapInfo> response) {
//                                    if (response != null && response.size() > 0) {
//                                        buildNotify(authId, BaseConstant.AUTH_CHANNEL, "有新授权信息", String.valueOf
//                                                (response.size() + "个"));
//                                    }
//                                }
//                            });

                    ReqAllAuth reqAllAuth = new ReqAllAuth();
                    reqAllAuth.MachineCode = token;
                    reqAllAuth.Id = "-1";
                    reqAllAuth.isGetAll = "false";
                    reqAllAuth.mStartTime = TimeUtil.ms2Date(startTime);
                    reqAllAuth.mEndTime = TimeUtil.ms2Date(endTime);
                    String ipPort = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_IP_PORT, "");
                    if (!TextUtils.isEmpty(ipPort)) {
                        String[] split = ipPort.split("_");
                        if (split.length > 1) {
                            reqAllAuth.serverIP = split[0];
                            reqAllAuth.serverPort = split[1];
                            reqAllAuth.serverName = ipPort;
                        }
                    }
                    reqAllAuth.startIndex = String.valueOf("0");
                    reqAllAuth.numberOfRecords = String.valueOf("1");
                    reqAllAuth.TimeQueryChecked = "true";
                    reqAllAuth.PesonChecked = "false";
                    reqAllAuth.State = "0";
                    reqAllAuth.Applicant = "";
                    reqAllAuth.Approver = "";

//                    authService.getAll(reqAllAuth)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribeWith(new ObserverCallBack<List<AuthInfo>>() {
//                                @Override
//                                protected void onThrowable(Throwable e) {
//                                    LogUtil.d(TAG, "用户授权请求失败---" + e.getMessage());
//                                }
//
//                                @Override
//                                protected void onFailure(BaseResponse response) {
//                                    LogUtil.d(TAG, "用户授权请求失败---" + response.Code + "---message---" + response.Message);
//                                }
//
//                                @Override
//                                protected void onSuccess(List<AuthInfo> response) {
//                                    if (response != null && response.size() > 0) {
//                                        buildNotify(authId, BaseConstant.AUTH_CHANNEL, "有新授权信息", String.valueOf(response.size() + "个"));
//                                    }
//                                }
//                            });

                    AuthService authService = HttpService.getInstance().buildJsonRetrofit().create(AuthService.class);
                    PipeTapService pipeTapService = HttpService.getInstance().buildJsonRetrofit().create
                            (PipeTapService.class);
                    Observable.zip(pipeTapService.getAll(reqAllPipeTap), authService.getAll(reqAllAuth),
                            (BiFunction<BaseResponse<List<PipeTapInfo>>, BaseResponse<List<AuthInfo>>, String>)
                                    (listBaseResponse, listBaseResponse2) -> {
                                        StringBuilder sb = new StringBuilder();
                                        if (listBaseResponse != null && listBaseResponse.Code == 200) {
                                            List<PipeTapInfo> pipeTapInfos = listBaseResponse.Response;
                                            if (pipeTapInfos != null && pipeTapInfos.size() > 0) {
                                                for (PipeTapInfo pipeTapInfo : pipeTapInfos) {
                                                    sb.append(pipeTapInfo.Applicant).append(",");
                                                }
                                            }
                                        }

                                        if (listBaseResponse2 != null && listBaseResponse2.Code == 200) {
                                            List<AuthInfo> authInfos = listBaseResponse2.Response;
                                            if (authInfos != null && authInfos.size() > 0) {
                                                for (AuthInfo authInfo : authInfos) {
                                                    sb.append(authInfo.ApplicatorName).append(",");
                                                }
                                            }
                                        }

                                        if (sb.length() > 0) {
                                            sb.deleteCharAt(sb.length() - 1);
                                        }

                                        return sb.toString();
                                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                                if (!TextUtils.isEmpty(s)) {
                                    LogUtil.d(TAG, "授权通知结果---" + s);
                                    String[] count = s.split(",");
                                    buildNotify(authId, BaseConstant.AUTH_CHANNEL, "有新授权信息", count.length + "个", s);
                                }
                            }, throwable -> {
                                LogUtil.e(TAG, "授权通知结果---" + throwable.getMessage());
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mDisposable.add(disposable);
    }

    /**
     * 是否在免打扰时间段
     *
     * @return
     */
    private boolean isNotDistub() {
        if (SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.NOT_DISTURB_OPEN, false)) {
            String start = SharedPreferencesUtil.getInstance().getString(BaseConstant.NOT_DISTURB_START, "");
            String end = SharedPreferencesUtil.getInstance().getString(BaseConstant.NOT_DISTURB_END, "");
            if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end)) {
                String[] starts = start.split(":");
                String[] ends = end.split(":");
                if (starts.length > 0 && ends.length > 0) {
                    int sHour = Integer.valueOf(starts[0]);
                    int sMinute = Integer.valueOf(starts[1]);
                    int eHour = Integer.valueOf(ends[0]);
                    int eMinute = Integer.valueOf(ends[1]);

                    long spe1 = TimeUtil.getSpeDate(sHour, sMinute, 0, 0);
                    long spe2 = TimeUtil.getSpeDate(eHour, eMinute, 0, 0);
                    boolean b = System.currentTimeMillis() > spe1 && System.currentTimeMillis() < spe2;
                    LogUtil.d(TAG, "isNotDistub = " + b + " start = " + spe1 + " end = " + spe2);
                    return b;
                }
            }
        }
        return false;
    }
}
