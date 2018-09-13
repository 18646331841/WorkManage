package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.map.pipe.PipeTrackInfo;
import com.barisetech.www.workmanage.bean.map.pipe.ReqPipeTrack;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.MapService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/9/12.
 */
public class MapModel extends BaseModel {
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

    String key = "default";

    /**
     * 通过管线集合获取所有管线路径
     * @param pipeInfos
     * @return
     */
    public Disposable allPipeTrack(List<PipeInfo> pipeInfos) {
        Map<String, List<PipeTrackInfo>> trackMap = new HashMap<>();

        Disposable disposable = Observable.fromArray(pipeInfos)
                .flatMap((Function<List<PipeInfo>, ObservableSource<PipeInfo>>) pipeInfos1 -> {
                    key = "default";
                    return Observable.fromIterable(pipeInfos1);
                })
                .map(pipeInfo -> String.valueOf(pipeInfo.PipeId))
                .flatMap((Function<String, ObservableSource<BaseResponse<List<PipeTrackInfo>>>>) pipeId -> {
                    ReqPipeTrack reqPipeTrack = new ReqPipeTrack();
                    reqPipeTrack.setMachineCode(mToken);
                    reqPipeTrack.setPipeId(pipeId);
                    key = pipeId;
                    return mapService.getPipeTrack(reqPipeTrack).onErrorReturnItem(new
                            BaseResponse<>(-1, "", null));
                }).map((Function<BaseResponse<List<PipeTrackInfo>>, Integer>) listBaseResponse -> {
                    if (listBaseResponse != null) {
                        int code = listBaseResponse.Code;
                        if (code == 200) {
                            List<PipeTrackInfo> response = listBaseResponse.Response;
                            if (response != null && response.size() > 0) {
                                trackMap.put(key, response);
                                return code;
                            } else {
                                LogUtil.d(TAG, "Key : " + key + " Response = null");
                            }
                        } else {
                            LogUtil.d(TAG, "Key : " + key + " Response Code = " + code);
                            if (code == 401) {
                                return Config.ERROR_UNAUTHORIZED;
                            }
                        }
                    } else {
                        LogUtil.d(TAG, "Key : " + key + " BaseResponse = null");
                    }
                    return Config.ERROR_FAIL;
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(lists -> {
                    if (trackMap.size() <= 0) {
                        FailResponse failResponse = new FailResponse(TYPE_TRACK, Config.ERROR_FAIL);
                        for (int i = 0; i < lists.size(); i++) {
                            if (lists.get(i) == Config.ERROR_UNAUTHORIZED) {
                                failResponse = new FailResponse(TYPE_TRACK, Config.ERROR_UNAUTHORIZED);
                                break;
                            }
                        }
                        modelCallBack.fail(failResponse);
                    } else {
                        LogUtil.d(TAG, "track all load");
                        TypeResponse typeResponse = new TypeResponse(TYPE_TRACK, trackMap);
                        modelCallBack.netResult(typeResponse);
                    }
                }, throwable -> {
                    LogUtil.d(TAG, throwable.getMessage());
                });
        return disposable;
    }
}
