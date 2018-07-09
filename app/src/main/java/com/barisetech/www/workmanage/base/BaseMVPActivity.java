package com.barisetech.www.workmanage.base;

import android.os.Bundle;

/**
 * Created by LJH on 2018/7/9.
 */
public abstract class BaseMVPActivity<P extends BasePresenter> extends BaseActivity {
    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mvpPresenter) {
            mvpPresenter.detachView();
        }
    }

    protected abstract P createPresenter();
}
