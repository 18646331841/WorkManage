package com.barisetech.www.workmanage.base;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.view.dialog.CommonDialogFragment;
import com.barisetech.www.workmanage.view.dialog.DialogFragmentHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import io.reactivex.annotations.NonNull;

/**
 * Created by LJH on 2018/7/9.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    protected Context mContext;
    protected CommonDialogFragment commonDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomDensity(this, getApplication());
        super.onCreate(savedInstanceState);
        mContext = this;
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initView(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (null != commonDialogFragment) {
                closeProgress();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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
     * 小屏幕跳转到指定activity，大屏幕显示指定的fragment
     *
     * @param eventBusMessage
     */
    protected abstract void showActivityOrFragment(EventBusMessage eventBusMessage);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(EventBusMessage eventBusMessage) {
        switch (eventBusMessage.message) {
            case BaseConstant.PROGRESS_SHOW:
                showProgress();
                break;
            case BaseConstant.PROGRESS_CLOSE:
                closeProgress();
                break;
            default:
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    showActivityOrFragment(eventBusMessage);
                }
                break;
        }
    }

    /**
     * 页面跳转
     *
     * @param targetActivity
     */
    protected void intent2Activity(Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(mContext, targetActivity);
        startActivity(intent);
    }

    /**
     * 页面跳转
     * @param bundle
     * @param targetActivity
     */
    protected void intent2Activity(Bundle bundle, Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(mContext, targetActivity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showProgress() {
        commonDialogFragment = DialogFragmentHelper.showProgress(getSupportFragmentManager(), getString(R.string
                .dialog_progress_text), true);
    }

    private void closeProgress() {
        if (null != commonDialogFragment) {
            commonDialogFragment.dismiss();
            commonDialogFragment = null;
        }
    }

    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    /**
     * 屏幕设配方法
     * @param activity
     * @param application
     */
    private static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration configuration) {
                    if (configuration == null && configuration.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        LogUtil.d("BaseActivity", "widthPixel = " + appDisplayMetrics.widthPixels + ", heightPixel = " +
                appDisplayMetrics.heightPixels);
        final float targetDensity = appDisplayMetrics.widthPixels / 360;//根据实际设计修改
        final float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
    }
}
