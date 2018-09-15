package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarm.ReqAllAlarm;
import com.barisetech.www.workmanage.bean.plugin.PluginInfo;
import com.barisetech.www.workmanage.bean.plugin.ReqAllPlugin;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PlugService {

    @POST("/api/Plug")
    Observable<BaseResponse<List<PluginInfo>>> getAllPlugin(@Body ReqAllPlugin reqAllPlugin);

}
