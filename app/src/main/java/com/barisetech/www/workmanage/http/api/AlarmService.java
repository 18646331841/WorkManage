package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfoNewest;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.alarm.ReqLiftAlarm;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/8/10.
 */
public interface AlarmService {

    /**
     * 获取所有警报
     *
     * @param reqAllAlarm
     * @return
     */
    @POST("/api/ALarms")
    Observable<BaseResponse<List<AlarmInfo>>> getAllAlarm(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Body
            ReqAllAlarm reqAllAlarm);

    /**
     * 获取警报数量
     *
     * @param token
     * @return
     */
    @GET("/api/ALarms/{token}")
    Observable<BaseResponse<Integer>> getAlarmNum(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token);

    /**
     * 获取最新警报信息
     *
     * @param token
     * @return
     */
    @PUT("/api/ALarms/{token}")
    Observable<BaseResponse<AlarmInfoNewest>> getAlarmInfoNewest(@Header(BaseConstant.AUTH_HEADER) String Bearer,
                                                                 @Path("token") String token);

    /**
     * 解除警报
     *
     * @param token
     * @param reqLiftAlarm
     * @return
     */
    @HTTP(method = "DELETE", path = "/api/ALarms/{token}", hasBody = true)
//    @DELETE("/api/ALarms/{token}", hasBody = true)
    Observable<BaseResponse<Boolean>> reqLiftAlarm(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token, @Body ReqLiftAlarm reqLiftAlarm);
}
