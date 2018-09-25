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
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.UserInfo;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.contacts.ReqAllContacts;
import com.barisetech.www.workmanage.bean.contacts.ReqContactsNum;
import com.barisetech.www.workmanage.databinding.FragmentMyInfoBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.viewmodel.ContactsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class MyInfoFragment extends BaseFragment {


    public static final String TAG = "MyInfoFragment";
    FragmentMyInfoBinding mBinding;

    private UserInfo userInfo;
    private ContactsViewModel contactsViewModel;
    private List<ContactsBean> contactsBeanList;
    private Disposable disposable;
    private String user;

    public static MyInfoFragment newInstance() {
        MyInfoFragment fragment = new MyInfoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_info, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        userInfo = new UserInfo();
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.personal_info));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        user = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
        mBinding.tvAccount.setText(user);

        mBinding.itemPhone.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(ModifyPhoneFragment.TAG);
            eventBusMessage.setArg1(userInfo);
            EventBus.getDefault().post(eventBusMessage);
        });


        mBinding.itemEmail.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(ModifyEmailFragment.TAG);
            eventBusMessage.setArg1(userInfo);
            EventBus.getDefault().post(eventBusMessage);
        });


        mBinding.itemPwd.setOnClickListener(v -> {
            EventBus.getDefault().post(new EventBusMessage(PassWordFragment.TAG));
        });
    }

    @Override
    public void bindViewModel() {
        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);


    }

    @Override
    public void subscribeToModel() {
        if (!contactsViewModel.getObservableAll().hasObservers()) {
            contactsViewModel.getObservableAll().observe(this, contactsBeans -> {
                if (MyInfoFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != contactsBeans) {
                        if (contactsBeans.size() > 0) {
                            contactsBeanList.addAll(contactsBeans);
                            for (ContactsBean contactsBean : contactsBeanList) {
                                if (contactsBean.getName().equals(user)) {
                                    mBinding.tvEmail.setText(contactsBean.getEmail());
                                    mBinding.tvPhone.setText(contactsBean.getTelephone());
                                    mBinding.tvAccount.setText(contactsBean.getName());
                                }
                            }

                        }
                    }
                }
            });
        }


        if (!contactsViewModel.getObservableNum().hasObservers()) {
            contactsViewModel.getObservableNum().observe(this, integer -> {
                if (MyInfoFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer) {
                        getDatas(0, integer);
                    }
                }
            });
        }

        if (null == contactsBeanList || contactsBeanList.size() <= 0) {
            getContactsNum();
        }

    }

    private void getContactsNum() {
        ReqContactsNum reqContactsNum = new ReqContactsNum();
        reqContactsNum.setSelectItem("0");
        reqContactsNum.setSearchString("");
        contactsViewModel.reqNum(reqContactsNum);
    }

    private void getDatas(int fromIndex, int toIndex) {
        if (toIndex <= 0) {
            return;
        }
        ReqAllContacts reqAllContacts = new ReqAllContacts();
        reqAllContacts.setSelectItem("0");
        reqAllContacts.setSearchString("");
        reqAllContacts.setStartIndex(String.valueOf(fromIndex));
        reqAllContacts.setNumberOfRecords(String.valueOf(toIndex));

        disposable = contactsViewModel.reqAll(reqAllContacts);
    }
}
