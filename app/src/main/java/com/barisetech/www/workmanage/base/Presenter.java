package com.barisetech.www.workmanage.base;

/**
 * Created by LJH on 2018/7/9.
 */
public interface Presenter<V> {
    void attachView(V view);

    void detachView();
}
