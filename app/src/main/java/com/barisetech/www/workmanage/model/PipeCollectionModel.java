package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAddPC;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.bean.pipecollections.ReqDeletePc;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.PipeCollectionsService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/28.
 */
public class PipeCollectionModel extends BaseModel {
    public static final String TAG = "PipeCollectionModel";

    private ModelCallBack modelCallBack;
    private PipeCollectionsService pipeCollectionsService;

    public PipeCollectionModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        pipeCollectionsService = HttpService.getInstance().buildJsonRetrofit().create
                (PipeCollectionsService.class);
    }

    /**
     * 获取管线集合数量
     * @return
     */
    public Disposable reqPcNUM() {
        Disposable disposable = pipeCollectionsService.getPcNum(mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Integer>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        modelCallBack.fail(Config.ERROR_NETWORK);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        if (response.Code == 401) {
                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
                        }
                        modelCallBack.fail(Config.ERROR_FAIL);
                    }

                    @Override
                    protected void onSuccess(Integer response) {
                        LogUtil.d(TAG, "Pc = " + response);
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }

    /**
     * 添加或修改管线集合
     * @param reqAddPC
     * @return
     */
    public Disposable reqAddOrModifyPc(ReqAddPC reqAddPC) {
        if (reqAddPC == null) {
            return null;
        }
        Disposable disposable = pipeCollectionsService.addOrModifyPc(mToken, reqAddPC)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<String>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        modelCallBack.fail(Config.ERROR_NETWORK);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        if (response.Code == 401) {
                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
                        }
                        modelCallBack.fail(Config.ERROR_FAIL);
                    }

                    @Override
                    protected void onSuccess(String response) {
                        LogUtil.d(TAG, "AddOrModifyPc result = " + response);
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }

    /**
     * 删除管线集合
     * @param reqDeletePc
     * @return
     */
    public Disposable reqDeletePc(ReqDeletePc reqDeletePc) {
        if (null == reqDeletePc) {
            return null;
        }

        Disposable disposable = pipeCollectionsService.deletePc(mToken, reqDeletePc)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Boolean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        modelCallBack.fail(Config.ERROR_NETWORK);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        if (response.Code == 401) {
                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
                        }
                        modelCallBack.fail(Config.ERROR_FAIL);
                    }

                    @Override
                    protected void onSuccess(Boolean response) {
                        LogUtil.d(TAG, "DeletePc result = " + response);
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }

    /**
     * 获取所有管线集合
     * @param reqAllPc
     * @return
     */
    public Disposable reqAllPc(ReqAllPc reqAllPc) {
        if (null == reqAllPc) {
            return null;
        }
        Disposable disposable = pipeCollectionsService.getAllPc(reqAllPc)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<PipeCollections>>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        modelCallBack.fail(Config.ERROR_NETWORK);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        if (response.Code == 401) {
                            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
                        }
                        modelCallBack.fail(Config.ERROR_FAIL);
                    }

                    @Override
                    protected void onSuccess(List<PipeCollections> response) {
                        LogUtil.d(TAG, "AllPc result = " + response);
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }
}
