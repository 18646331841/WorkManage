package com.barisetech.www.workmanage.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.barisetech.www.workmanage.utils.LogUtil;

/**
 * Created by LJH on 2018/8/23.
 */
public  class CustomPopupWindow implements PopupWindow.OnDismissListener {
    private static final String TAG = "CustomPopupWindow";
    private PopupWindow mPopupWindow;
    private android.view.View contentview;
    private Context mContext;
    private Activity mActivity;
    private float alpha;

    public CustomPopupWindow(Builder builder) {
        mContext = builder.context;
        contentview = LayoutInflater.from(mContext).inflate(builder.contentviewid,null);
        if (builder.width ==0 || builder.height == 0) {
            builder.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            builder.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        mPopupWindow = new PopupWindow(contentview, builder.width, builder.height, builder.fouse);
        //需要跟 setBackGroundDrawable 结合
        mPopupWindow.setOutsideTouchable(builder.outsidecancel);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setAnimationStyle(builder.animstyle);

        alpha = builder.alpha;
        mActivity = builder.activity;

        mPopupWindow.setOnDismissListener(this);
    }
    /**
     * popup 消失
     */
    public void dismiss(){
        if (mPopupWindow != null){
            mPopupWindow.dismiss();
        }
    }

    /**
     * 显示背景透明度
     */
    private void showAlpha() {
        //设置背景色
        if (alpha > 0 && alpha < 1 & null != mActivity){
            WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
            params.alpha = alpha;
            mActivity.getWindow().setAttributes(params);
        }
    }

    /**
     * 根据id获取view
     * @param viewid
     * @return
     */
    public android.view.View getItemView(int viewid){
        if (mPopupWindow != null){
            return this.contentview.findViewById(viewid);
        }
        return null;
    }
    /**
     * 根据父布局，显示位置
     * @param rootviewid
     * @param gravity
     * @param x
     * @param y
     * @return
     */
    public CustomPopupWindow showAtLocation(int rootviewid,int gravity,int x,int y){
        if (mPopupWindow != null){
            View rootview = LayoutInflater.from(mContext).inflate(rootviewid,null);
            mPopupWindow.showAtLocation(rootview,gravity,x,y);
            showAlpha();
        }
        return this;
    }
    /**
     * 根据id获取view ，并显示在该view的位置
     * @param targetviewId
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CustomPopupWindow showAsLaction(int targetviewId, int gravity, int offx, int offy){
        if (mPopupWindow != null){
            android.view.View targetview = LayoutInflater.from(mContext).inflate(targetviewId,null);
            mPopupWindow.showAsDropDown(targetview,offx,offy,gravity);
            showAlpha();
        }
        return this;
    }
    /**
     * 显示在 targetview 的不同位置
     * @param targetview
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CustomPopupWindow showAsLaction(android.view.View targetview, int gravity, int offx, int offy){
        if (mPopupWindow != null){
            mPopupWindow.showAsDropDown(targetview,offx,offy,gravity);
            showAlpha();
        }
        return this;
    }
    /**
     * 根据id设置焦点监听
     * @param viewid
     * @param listener
     */
    public void setOnFocusListener(int viewid, android.view.View.OnFocusChangeListener listener){
        android.view.View view = getItemView(viewid);
        view.setOnFocusChangeListener(listener);
    }

    /**
     * 根据id设置点击事件监听
     * @param viewid
     * @param listener
     */
    public void setOnClickListener(int viewid, View.OnClickListener listener){
        getItemView(viewid).setOnClickListener(listener);
    }

    /**
     * 监听 dismiss，还原背景色
     */
    @Override
    public void onDismiss() {
        LogUtil.d(TAG, "onDismiss: ");
        if (mActivity != null){
            WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
            params.alpha = 1.0f;
            mActivity.getWindow().setAttributes(params);
        }
    }

    /**
     * builder 类
     */
    public static class Builder{
        private int contentviewid;
        private int width;
        private int height;
        private boolean fouse;
        private boolean outsidecancel;
        private int animstyle;
        private Context context;
        private Activity activity;
        private float alpha;
        public Builder setContext(Context context){
            this.context = context;
            return this;
        }
        public Builder setContentView(int contentviewid){
            this.contentviewid = contentviewid;
            return this;
        }
        public Builder setwidth(int width){
            this.width = width;
            return this;
        }
        public Builder setheight(int height){
            this.height = height;
            return this;
        }
        public Builder setFouse(boolean fouse){
            this.fouse = fouse;
            return this;
        }
        public Builder setOutSideCancel(boolean outsidecancel){
            this.outsidecancel = outsidecancel;
            return this;
        }
        public Builder setAnimationStyle(int animstyle){
            this.animstyle = animstyle;
            return this;
        }
        public Builder setBackGroudAlpha(Activity activity,float alpha){
            this.activity = activity;
            this.alpha = alpha;
            return this;
        }
        public CustomPopupWindow builder(){
            return new CustomPopupWindow(this);
        }
    }
}