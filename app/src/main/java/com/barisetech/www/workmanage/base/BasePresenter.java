package com.barisetech.www.workmanage.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
//import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LJH on 2018/7/9.
 */
public abstract class BasePresenter<V> implements Presenter<V> {
    public Reference<V> mvpView;

    public BasePresenter(V mvpView) {
        attachView(mvpView);
    }

    @Override
    public void attachView(V view) {
        this.mvpView = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        this.mvpView = null;
    }
}
