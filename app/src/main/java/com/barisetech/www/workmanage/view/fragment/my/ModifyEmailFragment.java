package com.barisetech.www.workmanage.view.fragment.my;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ReqModifyUser;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.UserInfo;
import com.barisetech.www.workmanage.databinding.FragmentModifyEmailBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.UserInfoViewModel;

public class ModifyEmailFragment extends BaseFragment {


    public static final String TAG = "ModifyEmailFragment";
    private static String USER_INFO = "userInfo";
    FragmentModifyEmailBinding mBinding;
    private UserInfo userInfo;

    private UserInfoViewModel userInfoViewModel;



    public static ModifyEmailFragment newInstance(UserInfo userInfo) {
        ModifyEmailFragment fragment = new ModifyEmailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_INFO, userInfo);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            userInfo = (UserInfo) getArguments().getSerializable(USER_INFO);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_email, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.modify_email));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.emailSave.setOnClickListener(view -> {
            ReqModifyUser user = new ReqModifyUser();
            user.setTel(userInfo.getTel());
            user.setEmail(mBinding.modifyEmail.getText());
            userInfoViewModel.reqUser(user);
        });
    }

    @Override
    public void bindViewModel() {

        userInfoViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);

    }

    @Override
    public void subscribeToModel() {
        userInfoViewModel.getmObservableModifyPwd().observe(this,aBoolean -> {
            if (null != aBoolean){
                if (aBoolean){
                    ToastUtil.showToast("修改成功");
                    getActivity().onBackPressed();

                }else {
                    ToastUtil.showToast("修改失败");
                }
            }
        });

    }
}
