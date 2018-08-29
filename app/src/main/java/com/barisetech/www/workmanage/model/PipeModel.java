package com.barisetech.www.workmanage.model;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAddPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.pipe.ReqDeletePipe;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.PipeService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/28.
 */
public class PipeModel extends BaseModel {
    public static final String TAG = "PipeModel";

    private ModelCallBack modelCallBack;
    private PipeService pipeService;

    public PipeModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        pipeService = HttpService.getInstance().buildJsonRetrofit().create(PipeService.class);
    }

    /**
     * 获取管线数量
     * @return
     */
    public Disposable reqPipeNum() {
        Disposable disposable = pipeService.getPipeNum(mToken)
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
                        LogUtil.d(TAG, "Pipe = " + response);
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }

    /**
     * 添加或修改管线
     * @param reqAddPipe
     * @return
     */
    public Disposable reqAddOrModifyPipe(ReqAddPipe reqAddPipe) {
        if (reqAddPipe == null) {
            return null;
        }
        Disposable disposable = pipeService.addOrModifyPipe(mToken, reqAddPipe)
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
                        LogUtil.d(TAG, "AddOrModifyPipe result = " + response);
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }

    /**
     * 删除管线
     * @param reqDeletePipe
     * @return
     */
    public Disposable reqDeletePipe(ReqDeletePipe reqDeletePipe) {
        if (null == reqDeletePipe) {
            return null;
        }

        Disposable disposable = pipeService.deletePipe(mToken, reqDeletePipe)
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
                        LogUtil.d(TAG, "DeletePipe result = " + response);
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }

    /**
     * 获取所有管线
     * @param reqAllPipe
     * @return
     */
    public Disposable reqAllPipe(ReqAllPipe reqAllPipe) {
        if (null == reqAllPipe) {
            return null;
        }
        reqAllPipe.setMachineCode(mToken);
        Disposable disposable = pipeService.getAllPipe(reqAllPipe)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<PipeInfo>>() {
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
                    protected void onSuccess(List<PipeInfo> response) {
                        LogUtil.d(TAG, "AllPipe result = " + response);
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }
}
