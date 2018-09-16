package com.barisetech.www.workmanage.widget.twomenu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.barisetech.www.workmanage.R;

/**
 * 菜单控件头部，封装了下拉动画，动态生成头部按钮个数
 *
 * @author
 */

@SuppressLint("ResourceAsColor")
public class ExpandTabView extends LinearLayout implements OnDismissListener {

    //
//    private ToggleButton selectedButton;
    //按钮的Text
//    private ArrayList<String> mTextList = new ArrayList<String>();

    private ArrayList<RelativeLayout> mViewList = new ArrayList<RelativeLayout>();

    //状态切换按钮
//    private ArrayList<ToggleButton> mToggleList = new ArrayList<ToggleButton>();

    private Context mContext;
    private final int SMALL = 0;
    private int displayWidth;
    private int displayHeight;

    private PopupWindow popupWindow;
    private int selectPosition;

    public ExpandTabView(Context context) {
        super(context);
        init(context);
    }

    public ExpandTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 根据选择的位置设置tabitem显示的值
     */
//    public void setTitle(String valueText, int position) {
//        if (position < mToggleList.size()) {
//            mToggleList.get(position).setText(valueText);
//        }
//    }
    public void setTitle(String title) {

    }

    /**
     * 根据选择的位置获取tabitem显示的值
     */
//    public String getTitle(int position) {
//        if (position < mToggleList.size() && mToggleList.get(position).getText() != null) {
//            return mToggleList.get(position).getText().toString();
//        }
//        return "";
//    }


    /**
     * @param textArray: ListView中item对应的text值的集合..
     * @param viewArray: 当前Layout中需要加入的View..
     */
    @SuppressLint("ResourceAsColor")
    public void setValue(ArrayList<String> textArray, ArrayList<View> viewArray) {
        if (mContext == null) {
            return;
        }
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        mTextList = textArray;
        for (int i = 0; i < viewArray.size(); i++) {

            //这里就添加了一个View..
            final RelativeLayout r = new RelativeLayout(mContext);
            int maxHeight = (int) (displayHeight * 0.5);   //定义布局的高度..


            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
                    .MATCH_PARENT, maxHeight);
            r.addView(viewArray.get(i), rl);  //在布局中添加View并指定参数

            mViewList.add(r);
            r.setTag(SMALL);

            //定义最上方的按钮,并在布局中添加这个按钮。并设置按钮的text
//			ToggleButton tButton = (ToggleButton) inflater.inflate(R.layout.toggle_button, this, false);
//			addView(tButton);
//			mToggleList.add(tButton);
//			tButton.setTag(i);
//			tButton.setText(mTextList.get(i));

            //用于实现当PopWindow显示时.再次点击收回PopWindow
            r.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPressBack();
                }
            });

            r.setBackgroundColor(mContext.getResources().getColor(R.color.popup_main_background));

            //当按钮被点击后需要触发的监听
//			tButton.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View view) {
//
//					ToggleButton tButton = (ToggleButton) view;
//					/** 如果当前点击的按钮与上次的点击不同.则设置当前的按钮处于点击状态 */
//					if (selectedButton != null && selectedButton != tButton) {
//						selectedButton.setChecked(false);
//					}
//					selectedButton = tButton;
//					selectPosition = (Integer) selectedButton.getTag();
//					/** 按钮被点击后,需要触发对应的监听事件.*/
//					initPopupWindow();
//					if (mOnButtonClickListener != null && tButton.isChecked()) {
//						mOnButtonClickListener.onClick(selectPosition);
//					}
//				}
//			});
        }
    }

    public void show() {
        if (popupWindow != null) {
            if (!popupWindow.isShowing()) {
                showPopup(selectPosition);
            } else {
                popupWindow.dismiss();
            }
        }
    }

    public void close() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    //开启动画效果
    public void initPopupWindow() {

        if (popupWindow == null) {
            popupWindow = new PopupWindow(mViewList.get(selectPosition), displayWidth, (int) (displayHeight * 0.5));
            popupWindow.setAnimationStyle(R.style.Animation_top_left);
            popupWindow.setFocusable(false);
//            popupWindow.setOutsideTouchable(true);
            popupWindow.setOnDismissListener(this);
        }

        //当popWindow 没有显示..并且按钮被点击时需要显示popup窗口
//        if (selectedButton.isChecked()) {
//            if (!popupWindow.isShowing()) {
//                showPopup(selectPosition);
//            } else {
//                popupWindow.setOnDismissListener(this);
//                popupWindow.dismiss();
//            }
//        } else {
//            if (popupWindow.isShowing()) {
//                popupWindow.dismiss();
//            }
//        }
    }

    //显示弹出窗函数
    private void showPopup(int position) {
        //获取当前布局中的第一个子节点元素
        if (popupWindow.getContentView() != mViewList.get(position)) {
            popupWindow.setContentView(mViewList.get(position));
        }
        popupWindow.showAsDropDown(this, 0, 0);
    }

    /**
     * 如果菜单成展开状态，则让菜单收回去
     */
    public boolean onPressBack() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
//            if (selectedButton != null) {
//                selectedButton.setChecked(false);
//            }
            return true;
        } else {
            return false;
        }

    }


    //初始化操作
    @SuppressWarnings("deprecation")
    private void init(Context context) {
        mContext = context;
        //获取默认显示的高度和宽度
        displayWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        displayHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        //设置对齐方式
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    public void onDismiss() {
//        showPopup(selectPosition);
//        popupWindow.setOnDismissListener(null);
    }

//    private OnButtonClickListener mOnButtonClickListener;

//    /**
//     * 设置tabitem的点击监听事件
//     */
//    public void setOnButtonClickListener(OnButtonClickListener l) {
//        mOnButtonClickListener = l;
//    }
//
//    /**
//     * 自定义tabitem点击回调接口
//     */
//    public interface OnButtonClickListener {
//        public void onClick(int selectPosition);
//    }
}
