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

    private boolean isShow = true;//activity是否在前台显示
    public static final int LANDSCAPE = 2;
    public static final int PORTRAIT = 1;
    public int screenOrientation;//当前屏幕方向

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtil.d(TAG, "landscape");
            screenOrientation = LANDSCAPE;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LogUtil.d(TAG, "portrait");
            screenOrientation = PORTRAIT;
        }

        LogUtil.d(TAG, "Base onCreate");
        setCustomDensity(this, getApplication());
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mContext = this;
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtil.d(TAG, "landscape");
            screenOrientation = LANDSCAPE;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LogUtil.d(TAG, "portrait");
            screenOrientation = PORTRAIT;
        }
        setCustomDensity(this, getApplication());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.d(TAG, "Base onSaveInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG, "Base onPause");

        isShow = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(TAG, "Base onResume");

        isShow = true;
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
//        if (isShow) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

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
//        }
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

    private void setCustomDensity(@NonNull Activity activity, @NonNull final Application application) {
        if (screenOrientation == PORTRAIT) {

            final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

            if (sNoncompatDensity == 0) {
                sNoncompatDensity = appDisplayMetrics.density;
                LogUtil.d(TAG, "sNoncompatDensity = " + sNoncompatDensity);
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
            final float targetDensity = ((float) appDisplayMetrics.widthPixels) / 375f;//根据实际设计修改
            LogUtil.d(TAG, "targetDensity = " + targetDensity);
            final float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
            final int targetDensityDpi = (int) (160 * targetDensity);

            appDisplayMetrics.density = targetDensity;
            appDisplayMetrics.scaledDensity = targetScaledDensity;
            appDisplayMetrics.densityDpi = targetDensityDpi;

            final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
            LogUtil.d(TAG, "original density = " + activityDisplayMetrics.density);

            activityDisplayMetrics.density = targetDensity;
            LogUtil.d(TAG, "new density = " + activityDisplayMetrics.density);

            activityDisplayMetrics.densityDpi = targetDensityDpi;
            activityDisplayMetrics.scaledDensity = targetScaledDensity;
        }
    }
}
