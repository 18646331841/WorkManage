package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.wave.ReqWave;
import com.barisetech.www.workmanage.bean.wave.WaveBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WaveService {

    /**
     * 获取波形
     *
     * @param
     * @return
     */
    @POST("/api/Waveform")
    Observable<BaseResponse<WaveBean>> getAll(@Header(BaseConstant.AUTH_HEADER) String Bearer, @Body ReqWave reqWave);
}
