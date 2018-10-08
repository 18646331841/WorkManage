package com.barisetech.www.workmanage.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAllAlarmAnalysis;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.incident.ReqAllIncident;
import com.barisetech.www.workmanage.bean.incident.ReqIncidentSelectItem;
import com.barisetech.www.workmanage.bean.workplan.PlanBean;
import com.barisetech.www.workmanage.bean.workplan.ReqAllPlan;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.AlarmAnalysisService;
import com.barisetech.www.workmanage.http.api.AlarmService;
import com.barisetech.www.workmanage.http.api.IncidentService;
import com.barisetech.www.workmanage.http.api.NewsService;
import com.barisetech.www.workmanage.http.api.PlanService;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/9/25.
 */
public class MyNotifyService extends Service {
    private static final String TAG = "MyNotifyService";
    private AlarmService alarmService;
    private IncidentService incidentService;
    private NewsService newsService;
    private AlarmAnalysisService alarmAnalysisService;
    private PlanService planService;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    private NotificationManager notificationManager;
    private final String channelId = "1";
    private final String channelName = BaseApplication.getInstance().getResources().getString(R.string.app_name);
    private final int alarmId = 1;
    private final int incidentId = 2;
    private final int newsId = 3;
    private final int alarmAnalysisId = 4;
    private final int planId = 5;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate");
        startForeground(1, new Notification());

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager
                    .IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            notificationManager.createNotificationChannel(channel);
        }
        alarmService = HttpService.getInstance().buildJsonRetrofit().create(AlarmService.class);
        incidentService = HttpService.getInstance().buildJsonRetrofit().create(IncidentService.class);
        newsService = HttpService.getInstance().buildJsonRetrofit().create(NewsService.class);
        alarmAnalysisService = HttpService.getInstance().buildJsonRetrofit().create
                (AlarmAnalysisService.class);
        planService = HttpService.getInstance().buildJsonRetrofit().create(PlanService.class);

        startInterval();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //5.0以下保活
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
        mDisposable.clear();
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

    /**
     * 发送通知
     *
     * @param id
     * @param title
     * @param content
     */
    private void buildNotify(int id, String title, String content) {
        boolean isSound = SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SOUND_OPEN, true);
        boolean isVibrate = SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SHOCK_OPEN, true);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setContentTitle(title)
                .setContentText(content)
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //设置通知时间
                .setWhen(System.currentTimeMillis());

        int defaults = Notification.DEFAULT_LIGHTS;
        //设置通知方式，声音，震动，呼吸灯等效果
        if (isSound) {
            defaults |= Notification.DEFAULT_SOUND;
        }

        if (isVibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        builder.setDefaults(defaults);

        switch (id) {
            case alarmId:
//                Intent intent = new Intent(this, .class);
//                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//                mBuilder.setContentIntent(pendingIntent);
        }

        notificationManager.notify(id, builder.build());
    }

    private void startInterval() {
        alarmInterval();
        incidentInterval();
        newsInterval();
        alarmAnalysisInterval();
        planInterval();
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
                    reqAllAlarm.setGetByTimeDiff("false");
                    long endTime = System.currentTimeMillis();
                    long startTime = endTime - BaseConstant.ALARM_TIME;
                    reqAllAlarm.setStartTime(TimeUtil.ms2Date(startTime));
                    reqAllAlarm.setEndTime(TimeUtil.ms2Date(endTime));

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
                                    buildNotify(alarmId, "有新警报信息", String.valueOf(response.size() + "个"));
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
                    LogUtil.d(TAG, "事件定时任务---" + aLong);
                    String token = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_TOKEN, "");
                    if (TextUtils.isEmpty(token)) {
                        return;
                    }

                    ReqIncidentSelectItem reqIncidentSelectItem = new ReqIncidentSelectItem();
                    long endTime = System.currentTimeMillis();
                    long startTime = endTime - BaseConstant.INCIDENT_TIME;
                    reqIncidentSelectItem.setMStartTime(TimeUtil.ms2Date(startTime));
                    reqIncidentSelectItem.setMEndTime(TimeUtil.ms2Date(endTime));
                    reqIncidentSelectItem.setTimeQueryChecked("true");
                    reqIncidentSelectItem.setSiteIdQueryChecked("false");
                    reqIncidentSelectItem.setSiteID("0");
                    reqIncidentSelectItem.setMIncidentType("1");

                    ReqAllIncident reqAllIncident = new ReqAllIncident();
                    reqAllIncident.setMachineCode(token);
                    reqAllIncident.setStartIndex("0");
                    reqAllIncident.setNumberOfRecord("1");
                    reqAllIncident.setIncidentSelectItem(reqIncidentSelectItem);

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
                                    //TODO
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
                                    //TODO
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
                                    //TODO
                                }
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mDisposable.add(disposable);
    }
}
