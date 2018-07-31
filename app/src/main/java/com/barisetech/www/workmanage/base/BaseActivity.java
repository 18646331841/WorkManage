package com.barisetech.www.workmanage.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LJH on 2018/7/9.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * 初始化界面
     */
    protected void initView(Bundle savedInstanceState) {
        loadViewLayout();
        bindViews();
        processLogic(savedInstanceState);
        setListener();
    }

    /**
     * 获取控件
     *
     * @param id  控件的id
     * @param <E>
     * @return
     */
    protected <E extends View> E get(int id) {
        return (E) findViewById(id);
    }

    /**
     * 加载布局
     */
    protected abstract void loadViewLayout();

    /**
     * find控件
     */
    protected abstract void bindViews();

    /**
     * 处理数据
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 页面跳转
     *
     * @param targetActivity
     */
    protected void intent2Activity(Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(mContext, targetActivity);
        startActivity(intent);
    }
}
