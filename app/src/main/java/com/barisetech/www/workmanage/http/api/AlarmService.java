package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfoNewest;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/8/10.
 */
public interface AlarmService {

    /**
     * 获取所有警报
     * @param reqAllAlarm
     * @return
     */
    @POST("/api/ALarms")
    Observable<AlarmInfo> getAllAlarm(@Body ReqAllAlarm reqAllAlarm);

    /**
     * 获取警报数量
     * @param token
     * @return
     */
    @GET("/api/ALarms/{token}")
    Observable<Integer> getAlarmNum(@Path("token") String token);

    /**
     * 获取最新警报信息
     * @param token
     * @return
     */
    @PUT("/api/ALarms/{token}")
    Observable<AlarmInfoNewest> getAlarmInfoNewest(@Path("token") String token);
}
