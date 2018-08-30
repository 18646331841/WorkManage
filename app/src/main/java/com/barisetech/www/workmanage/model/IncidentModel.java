package com.barisetech.www.workmanage.model;

import android.arch.lifecycle.LiveData;

import com.barisetech.www.workmanage.base.BaseModel;
import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.incident.ReqAllIncident;
import com.barisetech.www.workmanage.bean.incident.ReqIncidentSelectItem;
import com.barisetech.www.workmanage.bean.incident.ReqLiftIncident;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.db.AppDatabase;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.http.HttpService;
import com.barisetech.www.workmanage.http.ObserverCallBack;
import com.barisetech.www.workmanage.http.api.IncidentService;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LJH on 2018/8/15.
 */
public class IncidentModel extends BaseModel{
    public static final String TAG = "IncidentModel";

    private AppDatabase appDatabase;
    private ModelCallBack modelCallBack;

    public static final int TYPE_NUM = 1;
    public static final int TYPE_LIFT = 2;
    public static final int TYPE_ADD = 3;
    public static final int TYPE_ALL = 4;

    public IncidentModel(AppDatabase appDatabase, ModelCallBack callBack) {
        super(callBack);
        this.appDatabase = appDatabase;
        modelCallBack = callBack;
    }

    /**
     * 获取事件列表
     * @return
     */
    public Disposable reqAllIncident() {
        ReqIncidentSelectItem reqIncidentSelectItem = new ReqIncidentSelectItem();
        reqIncidentSelectItem.setMStartTime("2010-01-01 12:12:12");
        reqIncidentSelectItem.setMEndTime("2019-01-01 12:12:12");
        reqIncidentSelectItem.setTimeQueryChecked("true");
        reqIncidentSelectItem.setSiteIdQueryChecked("false");
        reqIncidentSelectItem.setSiteID("0");
        reqIncidentSelectItem.setMIncidentType("1");

        ReqAllIncident reqAllIncident = new ReqAllIncident();
        reqAllIncident.setMachineCode(mToken);
        reqAllIncident.setStartIndex("1");
        reqAllIncident.setNumberOfRecord("10");
        reqAllIncident.setIncidentSelectItem(reqIncidentSelectItem);

        IncidentService incidentService = HttpService.getInstance().buildJsonRetrofit().create(IncidentService.class);
        Disposable disposable = incidentService.getAllIncident(reqAllIncident)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<List<IncidentInfo>>() {
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
                    protected void onSuccess(List<IncidentInfo> response) {
                        modelCallBack.netResult(response);
                        appDatabase.incidentDao().insertAll(response);
                    }
                });

        return disposable;
    }

    /**
     * 获取事件数量
     * @return
     */
    public Disposable reqIncidentNum() {
        ReqIncidentSelectItem reqIncidentSelectItem = new ReqIncidentSelectItem();
        reqIncidentSelectItem.setMStartTime("2010-01-01 12:12:12");
        reqIncidentSelectItem.setMEndTime("2019-01-01 12:12:12");
        reqIncidentSelectItem.setTimeQueryChecked("true");
        reqIncidentSelectItem.setSiteIdQueryChecked("false");
        reqIncidentSelectItem.setSiteID("0");
        reqIncidentSelectItem.setMIncidentType("1");

        IncidentService incidentService = HttpService.getInstance().buildJsonRetrofit().create(IncidentService.class);
        Disposable disposable = incidentService.getIncidentNum(mToken, reqIncidentSelectItem)
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
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }

    /**
     * 解除事件
     * @param incidentId
     * @return
     */
    public Disposable reqLiftIncident(String incidentId) {
        ReqLiftIncident reqLiftIncident = new ReqLiftIncident();
        reqLiftIncident.setIncidentId(incidentId);

        IncidentService incidentService = HttpService.getInstance().buildJsonRetrofit().create(IncidentService.class);
        Disposable disposable = incidentService.liftIncident(mToken, reqLiftIncident)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ObserverCallBack<Boolean>() {
                    @Override
                    protected void onThrowable(Throwable e) {
                        FailResponse failResponse = new FailResponse(TYPE_LIFT, Config.ERROR_NETWORK);
                        modelCallBack.fail(failResponse);

                    }

                    @Override
                    protected void onFailure(BaseResponse response) {
                        FailResponse failResponse;
                        if (response.Code == 401) {
                            failResponse = new FailResponse(TYPE_LIFT, Config.ERROR_UNAUTHORIZED);
                        } else {
                            failResponse = new FailResponse(TYPE_LIFT, Config.ERROR_FAIL);
                        }
                        modelCallBack.fail(failResponse);
                    }

                    @Override
                    protected void onSuccess(Boolean response) {
                        modelCallBack.netResult(response);
                    }
                });
        return disposable;
    }

    public LiveData<List<IncidentInfo>> getAllIncidentInfo() {
        return appDatabase.incidentDao().getAllIncidentInfo();
    }

    public LiveData<List<IncidentInfo>> getIncidentInfosByRead(boolean isRead) {
        return appDatabase.incidentDao().getIncidentInfosByRead(isRead);
    }
}
