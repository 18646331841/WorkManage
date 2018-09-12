package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.map.PipeTrackInfo;
import com.barisetech.www.workmanage.bean.map.ReqPipeTrack;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.MapService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/9/12.
 */
public class MapModel extends BaseModel{
    public static final String TAG = "MapModel";

    private ModelCallBack modelCallBack;
    private MapService mapService;

    public static final int TYPE_TRACK = 1;

    public MapModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        mapService = HttpService.getInstance().buildJsonRetrofit().create(MapService.class);
    }

    public Disposable pipeTrack(ReqPipeTrack reqPipeTrack) {
        reqPipeTrack.setMachineCode(mToken);
        ObserverCallBack<List<PipeTrackInfo>> disposable = mapService.getPipeTrack(reqPipeTrack)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<PipeTrackInfo>>() {

                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_TRACK, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_TRACK, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_TRACK, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(List<PipeTrackInfo> response) {
                        LogUtil.d(TAG, "pipeTrack = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_TRACK, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

//    public Disposable AllPipeTrack(List<PipeInfo> pipeInfos) {
//        Observable.fromArray(pipeInfos)
//                .flatMap((Function<List<PipeInfo>, ObservableSource<PipeInfo>>) pipeInfos1 -> Observable.fromIterable
//                        (pipeInfos1))
//                .flatMap();
//    }
}
