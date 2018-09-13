package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.map.pipe.PipeTrackInfo;
import com.barisetech.www.workmanage.bean.map.pipe.ReqPipeTrack;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by LJH on 2018/9/12.
 */
public interface MapService {
    /**
     * 获取管线路径
     *
     * @param reqPipeTrack
     * @return
     */
    @POST("/api/MapPath")
    Observable<BaseResponse<List<PipeTrackInfo>>> getPipeTrack(@Body ReqPipeTrack reqPipeTrack);
}
