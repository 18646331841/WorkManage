package com.barisetech.www.workmanage.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollDisableView extends ScrollView {
    private boolean mScrollDisable = false;

    public ScrollDisableView(Context context) {
        super(context);
    }

    public ScrollDisableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = super.onInterceptTouchEvent(ev);
        if (mScrollDisable) {
            return false;
        }
        return result;
    }

    public void setScrollDisable(boolean disable) {
        mScrollDisable = disable;
    }
}
