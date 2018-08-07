package com.barisetech.www.workmanage.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.widget.gesturePassword.GestureLockLayout;

public class GestureFragment extends Fragment {

    public static final String TAG = "GestureFragment";
    private Button mSettingBtn;
    private GestureLockLayout mGestureLockLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_gesture,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSettingBtn = (Button) view.findViewById(R.id.btn_setting);
        mGestureLockLayout= (GestureLockLayout) view.findViewById(R.id.l_gesture_lock_view);
        mGestureLockLayout.setDotCount(3);
        mGestureLockLayout.setMode(GestureLockLayout.VERIFY_MODE);
        mGestureLockLayout.setTryTimes(100);
        mGestureLockLayout.setAnswer(0,1,2);

        mGestureLockLayout.setOnLockVerifyListener(new GestureLockLayout.OnLockVerifyListener() {
            @Override
            public void onGestureSelected(int id) {

            }

            @Override
            public void onGestureFinished(boolean isMatched) {
                if (isMatched) {
                    Toast.makeText(getActivity(),"密码正确",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),"密码不正确",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onGestureTryTimesBoundary() {

            }
        });

    }
}
