package com.barisetech.www.workmanage.view.fragment.my;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ReqModifyUser;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.UserInfo;
import com.barisetech.www.workmanage.databinding.FragmentModifyEmailBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.UserInfoViewModel;

import org.greenrobot.eventbus.EventBus;

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
            EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
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
        if (!userInfoViewModel.getmObservableModifyUser().hasObservers()) {
            userInfoViewModel.getmObservableModifyUser().observe(this,aBoolean -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != aBoolean){
                        if (aBoolean){
                            ToastUtil.showToast("修改成功");
                            getActivity().onBackPressed();

                        }else {
                            ToastUtil.showToast("修改失败");
                        }
                    }
                }
            });
        }

    }
}
