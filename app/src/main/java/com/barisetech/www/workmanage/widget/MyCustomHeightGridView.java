package com.barisetech.www.workmanage.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by LJH on 2018/8/27.
 */
public class MyCustomHeightGridView extends GridView {
    public MyCustomHeightGridView(Context context) {
        super(context);
    }

    public MyCustomHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
