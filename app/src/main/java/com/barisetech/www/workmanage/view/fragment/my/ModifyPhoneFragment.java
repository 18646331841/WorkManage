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
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.databinding.FragmentModifyPhoneBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.UserInfoViewModel;

import org.greenrobot.eventbus.EventBus;

public class ModifyPhoneFragment extends BaseFragment {


    public static final String TAG = "ModifyPhoneFragment";
    private static String USER_INFO = "userInfo";
    private UserInfo userInfo;
    FragmentModifyPhoneBinding mBinding;

    private UserInfoViewModel userInfoViewModel;


    public static ModifyPhoneFragment newInstance(UserInfo userInfo) {
        ModifyPhoneFragment fragment = new ModifyPhoneFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_phone, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.modify_phone));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {

        mBinding.phoneSave.setOnClickListener(view -> {
            EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));

            ReqModifyUser user = new ReqModifyUser();
            user.setTel(mBinding.modifyPhone.getText());
            user.setEmail(userInfo.getEmail());
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
