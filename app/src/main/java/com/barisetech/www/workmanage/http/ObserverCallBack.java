package com.barisetech.www.workmanage.http;

import com.barisetech.www.workmanage.base.BaseObserverCallBack;
import com.barisetech.www.workmanage.base.BaseResponse;

/**
 * Created by LJH on 2018/8/4.
 */
public abstract class ObserverCallBack<T> extends BaseObserverCallBack<BaseResponse<T>> {
    @Override
    public void onNext(BaseResponse<T> tBaseResponse) {
        boolean isSuccess = tBaseResponse.code == 200;
        if (isSuccess) {
            onSuccess(tBaseResponse.response);
        } else {
//            ToastUtils.showToast(response.message);
            onFailure(tBaseResponse);
        }
    }

    protected abstract void onSuccess(T response);
}
