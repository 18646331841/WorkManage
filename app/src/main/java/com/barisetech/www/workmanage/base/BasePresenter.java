package com.barisetech.www.workmanage.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LJH on 2018/7/9.
 */
public abstract class BasePresenter<V> implements Presenter<V> {
    public Reference<V> mvpView;
    private CompositeSubscription mCompositeSubscription;

    public BasePresenter(V mvpView) {
        attachView(mvpView);
    }

    @Override
    public void attachView(V view) {
        this.mvpView = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        onUnsubscribe();
        this.mvpView = null;
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (null != mCompositeSubscription && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (null == mCompositeSubscription) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public void addSubscription(Subscription subscription) {
        if (null == mCompositeSubscription) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }
}
