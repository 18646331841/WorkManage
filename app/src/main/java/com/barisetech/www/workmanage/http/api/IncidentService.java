package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.incident.ReqAllIncident;
import com.barisetech.www.workmanage.bean.incident.ReqIncidentSelectItem;
import com.barisetech.www.workmanage.bean.incident.ReqLiftIncident;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/8/15.
 */
public interface IncidentService {

    /**
     * 获取警报列表
     *
     * @param reqAllIncident
     * @return
     */
    @POST("/api/Incidents")
    Observable<BaseResponse<List<IncidentInfo>>> getAllIncident(@Body ReqAllIncident reqAllIncident);

    /**
     * 获取事件数量
     *
     * @param token
     * @param reqIncidentSelectItem
     * @return
     */
    @PUT("/api/Incidents/{token}")
    Observable<BaseResponse<Integer>> getIncidentNum(@Path("token") String token, @Body ReqIncidentSelectItem
            reqIncidentSelectItem);

    @DELETE("/api/Incidents/{token}")
    Observable<BaseResponse<Boolean>> liftIncident(@Path("token") String token, @Body ReqLiftIncident reqLiftIncident);

}
