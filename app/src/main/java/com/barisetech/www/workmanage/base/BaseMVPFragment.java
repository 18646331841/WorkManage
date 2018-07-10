package com.barisetech.www.workmanage.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by LJH on 2018/7/10.
 */
public abstract class BaseMVPFragment<P extends BasePresenter> extends BaseFragment {
    protected P mvpPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mvpPresenter) {
            mvpPresenter.detachView();
        }
    }

    protected abstract P createPresenter();
}
