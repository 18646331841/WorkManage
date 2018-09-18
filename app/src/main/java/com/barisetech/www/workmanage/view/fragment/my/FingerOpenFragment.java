package com.barisetech.www.workmanage.view.fragment.my;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentFingerOpenBinding;
import com.barisetech.www.workmanage.widget.CustomDialog;
import com.barisetech.www.workmanage.widget.SwitchView;

public class FingerOpenFragment extends BaseFragment {


    public static final String TAG = "FingerOpenFragment";
    FragmentFingerOpenBinding mBinding;

    private CustomDialog.Builder builder;
    private CustomDialog mDialog;


    public static FingerOpenFragment newInstance() {
        FingerOpenFragment fragment = new FingerOpenFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_finger_open, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.finger_open));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        builder = new CustomDialog.Builder(getContext());
        mBinding.fingerSwitch.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                showTwoButtonDialog("确认关闭指纹登录", 0, null, "确定", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });

            }

            @Override
            public void toggleToOff(SwitchView view) {

            }
        });
    }


    private void showTwoButtonDialog(String alertText, int ImgId,String title,String confirmText, String cancelText, View.OnClickListener conFirmListener, View.OnClickListener cancelListener) {
        mDialog = builder.setMessage(alertText)
                .setImagView(ImgId)
                .setTitle(title)
                .setPositiveButton(confirmText, conFirmListener)
                .setNegativeButton(cancelText, cancelListener)
                .createTwoButtonDialog();
        mDialog.show();
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
