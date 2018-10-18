package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAddAlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAllAlarmAnalysis;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqDeleteAlarmAnalysis;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAddPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqDeletePipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/8/10.
 */
public interface AlarmAnalysisService {

    /**
     * 获取全部警报分析
     *
     * @param reqAllAlarmAnalysis
     * @return
     */
    @POST("/api/AlarmAnalysis")
//TODO 返回对象需要修改
    Observable<BaseResponse<List<AlarmAnalysis>>> getAllAnalysis(@Header(BaseConstant.AUTH_HEADER) String Bearer,
                                                                 @Body ReqAllAlarmAnalysis reqAllAlarmAnalysis);

    /**
     * 获取警报分析数量
     *
     * @param token
     * @return
     */
    @GET("/api/AlarmAnalysis/{token}")
    Observable<BaseResponse<Integer>> getAnalysisNum(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token);

    /**
     * 添加或修改警报分析
     *
     * @param token
     * @param reqAddAlarmAnalysis
     * @return >0表示成功
     */
    @PUT("/api/AlarmAnalysis/{token}")
    Observable<BaseResponse<Integer>> addOrModifyAnalysis(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path
            ("token") String token, @Body ReqAddAlarmAnalysis reqAddAlarmAnalysis);

    /**
     * 删除警报分析
     *
     * @param token
     * @param reqDeleteAlarmAnalysis
     * @return
     */
    @DELETE("/api/AlarmAnalysis/{token}")
    Observable<BaseResponse<Boolean>> deleteAnalysis(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Path("token")
            String token, @Body ReqDeleteAlarmAnalysis reqDeleteAlarmAnalysis);
}
