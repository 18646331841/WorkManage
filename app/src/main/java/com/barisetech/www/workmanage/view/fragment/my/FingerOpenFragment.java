package com.barisetech.www.workmanage.view.fragment.my;

import android.app.KeyguardManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentFingerOpenBinding;
import com.barisetech.www.workmanage.utils.FingerprintUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.widget.CustomDialog;
import com.barisetech.www.workmanage.widget.SwitchView;

import static com.barisetech.www.workmanage.utils.ToastUtil.showToast;

public class FingerOpenFragment extends BaseFragment {


    public static final String TAG = "FingerOpenFragment";
    FragmentFingerOpenBinding mBinding;

    private CustomDialog.Builder builder;
    private CustomDialog mDialog;
    private boolean flag = true;



    public static FingerOpenFragment newInstance() {
        FingerOpenFragment fragment = new FingerOpenFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
        checkFingerPrint();
        return mBinding.getRoot();
    }

    private void initView() {

        builder = new CustomDialog.Builder(getContext());

        mBinding.fingerSwitch.setOnClickListener(v -> {
            if (flag){
                FingerprintUtil.getInstance().callFingerprint(onCallBackListenr);
                showSingleButtonDialog("请验证你的指纹", R.mipmap.ic_launcher_round, null, "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                        FingerprintUtil.getInstance().cancel();
                    }
                });

            }else {

                showTwoButtonDialog("确认关闭指纹登录", 0, "管线管理助手", "确定", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                        mBinding.fingerSwitch.setBackgroundResource(R.drawable.item_shape);
                        SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SP_LOGIN_FP, false);
                        flag = true;
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });



            }
        });

//        mBinding.fingerSwitch.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
////            @Override
////            public void toggleToOn(SwitchView view) {
//                showSingleButtonDialog("请验证你的指纹", R.mipmap.ic_launcher_round, null, "取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mDialog.dismiss();
//                    }
//                });
//            }
//
//            @Override
//            public void toggleToOff(SwitchView view) {
//
//                showTwoButtonDialog("确认关闭指纹登录", 0, "管线管理助手", "确定", "取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mDialog.dismiss();
//                        mBinding.fingerSwitch.setOpened(false);
//                    }
//                }, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mDialog.dismiss();
//                    }
//                });
//
//
//            }
//        });
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


    private void showSingleButtonDialog(String alertText, int ImgId,String title, String btnText, View.OnClickListener onClickListener) {
        mDialog = builder.setMessage(alertText)
                .setTitle(title)
                .setImagView(ImgId)
                .setSingleButton(btnText, onClickListener)
                .createSingleButtonDialog();
        mDialog.show();
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void checkFingerPrint(){
        FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(BaseApplication.getInstance()
                .getApplicationContext());

        if (!managerCompat.isHardwareDetected()){
            LogUtil.d(TAG, "设备不支持指纹");
            ToastUtil.showToast("设备不支持指纹");
            mBinding.fingerSwitch.setEnabled(false);
            return;
        }
        KeyguardManager keyguardManager = (KeyguardManager) BaseApplication.getInstance().getApplicationContext()
                .getSystemService(BaseApplication.KEYGUARD_SERVICE);
        if (null == keyguardManager || !keyguardManager.isKeyguardSecure()) {
            LogUtil.d(TAG, "设备没处于安全保护中");
            ToastUtil.showToast("设备没处于安全保护中");
            mBinding.fingerSwitch.setEnabled(false);
            return;
        }
        if (!managerCompat.hasEnrolledFingerprints()){
            LogUtil.d(TAG, "设备未设置指纹");
            ToastUtil.showToast("设备未设置指纹");
            mBinding.fingerSwitch.setEnabled(false);
            return;
        }
    }


    private FingerprintUtil.OnCallBackListenr onCallBackListenr = new FingerprintUtil.OnCallBackListenr() {
        @Override
        public void onSupportFailed(String msg) {
            showToast(msg);
        }

        @Override
        public void onAuthenticationStart() {
            showToast("开始指纹识别");
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            showToast(errString);
        }

        @Override
        public void onAuthenticationFailed() {
            showToast("指纹识别失败");

        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            showToast(helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            showToast("指纹识别成功");
            SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SP_LOGIN_FP, true);
            if (null != mDialog) {
                mDialog.dismiss();
                FingerprintUtil.getInstance().cancel();
                mBinding.fingerSwitch.setBackgroundResource(R.drawable.shape_button_corners);
                flag = false;
            }
        }
    };
}
