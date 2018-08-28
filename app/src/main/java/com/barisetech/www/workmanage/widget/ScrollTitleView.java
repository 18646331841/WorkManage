package com.barisetech.www.workmanage.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.barisetech.www.workmanage.utils.LogUtil;

/**
 * @author LJH
 */
public class ScrollTitleView extends ScrollView {

    private static final String TAG = "ScrollTitleView";

    private int mTitleHeight;
    private RecyclerView mRecyclerView;
    private ScrollView mScrollView;
    private int mType;

    private int mLastMotionY;

    private int mLastMotionX;

    private boolean mScrollDisable = false;

    public ScrollTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            if (child instanceof ViewGroup) {
                ViewGroup childLayout = (ViewGroup) child;
                // 判断是否下层控件是否有两个
                if (childLayout.getChildCount() > 1) {
                    View subChildTitle = childLayout.getChildAt(0);
                    mTitleHeight = subChildTitle.getHeight();
                }
            }
            if (mTitleHeight > 0) {
                int height = getMeasuredHeight();
                int childHeight = height + mTitleHeight;
                final FrameLayout.LayoutParams lp = (LayoutParams) child
                        .getLayoutParams();
                int childWidthMeasureSpec = getChildMeasureSpec(
                        widthMeasureSpec, getPaddingLeft() + getPaddingRight(),
                        lp.width);
                int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        childHeight, MeasureSpec.EXACTLY);

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int scrollY = getScrollY();
        int scrollX = getScrollX();
        // 在ScrollView的onLayout方法中，如果子控件获取到焦点，将滚动到子控件去。
        super.onLayout(changed, l, t, r, b);
        if (scrollY != getScrollY() || scrollX != getScrollX()) {
            scrollTo(scrollX, scrollY);
        }
    }

    public void setType(int type) {
        mType = type;
    }

    public void setScrollView(ScrollView scrollview) {
        mScrollView = scrollview;
    }

    private boolean isScroll() {
        boolean hasScroll = false;

        if (mRecyclerView != null) {
            View view = mRecyclerView.getChildAt(0);
            if (view != null) {
                int y = (int) view.getY();
                hasScroll = (y != 0);
//                LogUtil.d(TAG, "isScroll = " + hasScroll);
            }
        }
        return hasScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = super.onInterceptTouchEvent(ev);
        if (mScrollDisable) {
            return false;
        }
        int dir = 0; // 1:向上滚动 -1:向下滚动
        int distanseX = 0;
        int distanseY = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                mLastMotionX = (int) ev.getX();
                dir = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getY();
                dir = (y > mLastMotionY ? 1 : -1);
                distanseX = (int) (mLastMotionX - ev.getX());
                distanseY = mLastMotionY - y;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        int scrollY = getScrollY();
//        LogUtil.d(TAG,  "ScrollY = " + scrollY);

        boolean subItemHasScroll = false;
        subItemHasScroll = isScroll();

        if (scrollY == mTitleHeight) {
            if (dir > 0) {
                result = !subItemHasScroll;
            } else if (dir < 0) {
                result = false;
            }
        } else if (scrollY > mTitleHeight) {
            //锤子滑动适配
            if (dir <= 0) {
                result = false;
            }
        } else {
            if (!subItemHasScroll) {
                if (dir > 0) {
                    result = false;
                } else if (dir < 0) {
                    result = true;
                }
            }
        }
        if (result && (Math.abs(distanseY) < Math.abs(distanseX))) {
            result = false;
        }
//        LogUtil.d(TAG, "mTitleHeight" + mTitleHeight + "   ScrollY" + scrollY);
//        LogUtil.d(TAG, "on Scroll dir: " + dir);
//        LogUtil.d(TAG, "on Scroll onInterceptTouchEvent: " + result);
        return result;
    }

    public void setScrollDisable(boolean disable) {
        mScrollDisable = disable;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }
}
