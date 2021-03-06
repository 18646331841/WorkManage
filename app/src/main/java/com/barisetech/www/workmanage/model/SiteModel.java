package com.barisetech.www.workmanage.model;

import android.arch.lifecycle.LiveData;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.site.ReqAddSite;
import com.barisetech.www.workmanage.bean.site.ReqDelSiteInfo;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.SiteService;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/20.
 */
public class SiteModel extends BaseModel{

    public static final String TAG = "SiteModel";

    private ModelCallBack modelCallBack;
    private SiteService siteService;

    public static final int TYPE_NUM = 1;
    public static final int TYPE_DELETE = 2;
    public static final int TYPE_ADD = 3;
    public static final int TYPE_ALL = 4;

    public SiteModel(ModelCallBack modelCallBack) {
        super(modelCallBack);
        this.modelCallBack = modelCallBack;
        siteService = HttpService.getInstance().buildJsonRetrofit().create(SiteService.class);
    }


    public Disposable reqSiteNum(){
        Disposable disposable = siteService.getSiteNum(getBearer(), mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Integer>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_NUM, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_NUM, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_NUM, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(Integer response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_NUM, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }


    public Disposable reqDeletePipe(ReqDelSiteInfo reqDelSiteInfo) {
        if (null == reqDelSiteInfo) {
            return null;
        }

        Disposable disposable =siteService.deleteSite(getBearer(), mToken, reqDelSiteInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Boolean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_DELETE, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);

                    }
                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_DELETE, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_DELETE, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(Boolean response) {
                        LogUtil.d(TAG, "DeletePipe result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_DELETE, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }


    public Disposable reqAddOrModifySite(ReqAddSite reqAddSite) {
        if (reqAddSite == null) {
            return null;
        }
        Disposable disposable = siteService.addOrModifySite(getBearer(), mToken,reqAddSite)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<String>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_ADD, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_ADD, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_ADD, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(String response) {
                        LogUtil.d(TAG, "AddOrModifyPipe result = " + response);
                        TypeResponse typeResponse = new TypeResponse(TYPE_ADD, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }

    public Disposable reqAllSite(ReqSiteInfos reqSiteInfos){
        if (null==reqSiteInfos){
            return null;
        }
        reqSiteInfos.setMachineCode(mToken);
        Disposable disposable = siteService.getAllSite(getBearer(), reqSiteInfos)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<SiteBean>>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_ALL, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_ALL, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_ALL, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(List<SiteBean> response) {
                        TypeResponse typeResponse = new TypeResponse(TYPE_ALL, response);
                        modelCallBack.netResult(typeResponse);
                    }
                });
        return disposable;
    }
}
