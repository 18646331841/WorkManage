package com.barisetech.www.workmanage.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.widget.gesturePassword.GestureLockDisplayView;
import com.barisetech.www.workmanage.widget.gesturePassword.GestureLockLayout;
import com.barisetech.www.workmanage.widget.gesturePassword.ILockView;
import com.barisetech.www.workmanage.widget.gesturePassword.JDLockView;
import com.barisetech.www.workmanage.widget.gesturePassword.LockViewFactory;

import java.util.List;

public class GestureSettingFragment extends Fragment {

    private GestureLockLayout mGestureLockLayout;
    private GestureLockDisplayView mLockDisplayView;
    private TextView mSettingHintText;
    private Handler mHandler = new Handler();

    public static final String TAG = "GestureSettingFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gesture_setting,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mGestureLockLayout = (GestureLockLayout) view.findViewById(R.id.l_gesture_view);
        mLockDisplayView = (GestureLockDisplayView) view.findViewById(R.id.l_display_view);
        mSettingHintText = (TextView) view.findViewById(R.id.tv_setting_hint);
        //设置提示view 每行每列点的个数
        mLockDisplayView.setDotCount(3);
        //设置提示view 选中状态的颜色
        mLockDisplayView.setDotSelectedColor(Color.parseColor("#01A0E5"));
        //设置提示view 非选中状态的颜色
        mLockDisplayView.setDotUnSelectedColor(Color.TRANSPARENT);
        //设置手势解锁view 每行每列点的个数
        mGestureLockLayout.setDotCount(3);
        //设置手势解锁view 最少连接数
        mGestureLockLayout.setMinCount(3);

        mGestureLockLayout.setLockView(new LockViewFactory() {
            @Override
            public ILockView newLockView() {
                return new JDLockView(getActivity());
            }
        });
        //设置手势解锁view 模式为重置密码模式
        mGestureLockLayout.setMode(GestureLockLayout.RESET_MODE);

        mGestureLockLayout.setOnLockResetListener(new GestureLockLayout.OnLockResetListener() {
            @Override
            public void onConnectCountUnmatched(int connectCount, int minCount) {
                mSettingHintText.setText("最少连接" + minCount + "个点");
                resetGesture();
            }

            @Override
            public void onFirstPasswordFinished(List<Integer> answerList) {

                //第一次绘制手势成功时调用

                mSettingHintText.setText("确认解锁图案");
                //将答案设置给提示view
                mLockDisplayView.setAnswer(answerList);
                //重置
                resetGesture();
            }

            @Override
            public void onSetPasswordFinished(boolean isMatched, List<Integer> answerList) {

                //第二次密码绘制成功时调用

                if (isMatched) {
                    //两次答案一致，保存
                } else {
                    resetGesture();
                }
            }
        });
    }

    /**
     * 重置
     */
    private void resetGesture() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGestureLockLayout.resetGesture();
            }
        }, 200);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

    }
}
