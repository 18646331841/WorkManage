package com.barisetech.www.workmanage.view.fragment.my;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentSoundBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;

public class SoundFragment extends BaseFragment {

    public static final String TAG ="SoundFragment";
    FragmentSoundBinding mBinding;
    private boolean openShock ;
    private boolean openSound ;


    public static SoundFragment newInstance() {
        SoundFragment fragment = new SoundFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sound, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.sound_shock));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {

        if (SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SOUND_OPEN, false)){
            mBinding.soundSwitch.setChecked(true);
        }else {
            mBinding.soundSwitch.setChecked(false);
        }

        if (SharedPreferencesUtil.getInstance().getBoolean(BaseConstant.SHOCK_OPEN, false)){
            mBinding.shockSwitch.setChecked(true);
        }else {
            mBinding.shockSwitch.setChecked(false);
        }


        mBinding.shockSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                openShock = true;
            }else {
                openShock = false;
            }
        });
        mBinding.soundSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                openSound = true;
            }else {
                openSound = false;
            }
        });

        mBinding.soundSave.setOnClickListener(view -> {
            SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SOUND_OPEN, openSound);
            SharedPreferencesUtil.getInstance().setBoolean(BaseConstant.SHOCK_OPEN, openShock);
            ToastUtil.showToast("设置成功");
        });

    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
