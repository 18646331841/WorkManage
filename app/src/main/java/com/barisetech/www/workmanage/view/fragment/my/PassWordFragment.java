package com.barisetech.www.workmanage.view.fragment.my;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ReqModifyPwd;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentPassWordBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.LoginActivity;
import com.barisetech.www.workmanage.viewmodel.UserInfoViewModel;

public class PassWordFragment extends BaseFragment {


    public static final String TAG ="PassWordFragment";
    FragmentPassWordBinding mBinding;


    private UserInfoViewModel userInfoViewModel;

    public static PassWordFragment newInstance() {
        PassWordFragment fragment = new PassWordFragment();
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pass_word, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.change_pwd));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.pwdSave.setOnClickListener(view->{
            if (TextUtils.isEmpty(mBinding.oldPwd.getText())){
                ToastUtil.showToast("原密码不能为空");
            }else if (TextUtils.isEmpty(mBinding.newPwd.getText())||TextUtils.isEmpty(mBinding.submitPwd.getText())){
                ToastUtil.showToast("新密码不能为空");
            }else{
                if (!mBinding.newPwd.getText().equals(mBinding.submitPwd.getText())){
                    ToastUtil.showToast("新密码和确认密码不一致");
                }else {
                    ReqModifyPwd reqModifyPwd = new ReqModifyPwd();
                    reqModifyPwd.setMyNewPsd(mBinding.newPwd.getText());
                    reqModifyPwd.setMyPsd(mBinding.oldPwd.getText());
                    userInfoViewModel.reqPwd(reqModifyPwd);
                }
            }
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
                    ToastUtil.showToast("修改成功，请重新登录");
                    Intent itent = new Intent(getContext(), LoginActivity.class);
                    startActivity(itent);
                    if (getActivity()!=null){
                        getActivity().finish();
                    }
                }else {
                    ToastUtil.showToast("修改失败");
                }
            }
        });

    }
}
