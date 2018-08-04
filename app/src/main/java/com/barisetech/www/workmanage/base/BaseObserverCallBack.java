package com.barisetech.www.workmanage.base;

import android.os.Handler;
import android.os.Looper;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by LJH on 2018/8/4.
 */
public abstract class BaseObserverCallBack<T> extends DisposableObserver<T> {
    private Handler mDelivery;

    public BaseObserverCallBack() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onError(final Throwable e) {
        mDelivery.post(() -> {
            if (e instanceof SocketTimeoutException) {
                ToastUtil.showToast("网络连接超时");
            } else if (e instanceof SocketException) {
                if (e instanceof ConnectException) {
                    ToastUtil.showToast("网络未连接");
                } else {
                    ToastUtil.showToast("网络错误");
                }
            }
        });
        onThrowable(e);
    }

    protected void onThrowable(Throwable e) {
    }

    protected void onFailure(BaseResponse response) {
    }
}
