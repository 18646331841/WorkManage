package com.barisetech.www.workmanage.view.fragment.my;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ContactsAdapter;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.PipeLindAreaAdapter;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.contacts.ReqAllContacts;
import com.barisetech.www.workmanage.bean.contacts.ReqContactsNum;
import com.barisetech.www.workmanage.bean.pipelindarea.PipeLindAreaInfo;
import com.barisetech.www.workmanage.databinding.FragmentContactsBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.viewmodel.ContactsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ContactsFragment extends BaseFragment {

    public static final String TAG = "ContactsFragment";
    FragmentContactsBinding mBinding;

    private List<ContactsBean> contactsBeanList;
    private ContactsAdapter contactsAdapter;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private ContactsViewModel contactsViewModel;
    private Disposable disposable;

    private static final int PAGE_COUNT = 10;
    private int maxNum;


    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsBeanList = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.tv_contacts));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {

        contactsAdapter = new ContactsAdapter(getContext(),contactsBeanList);
        loadMoreWrapper = new BaseLoadMoreWrapper(contactsAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(),50));
        mBinding.contactsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.contactsList.setAdapter(loadMoreWrapper);
        mBinding.contactsList.setItemAnimator(new DefaultItemAnimator());

        mBinding.contactsList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (contactsBeanList.size()==maxNum){
                    if (maxNum!=0){
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }else {
                    int count = maxNum - contactsBeanList.size();
                    if (count < PAGE_COUNT){
                        updateRecyclerView(contactsBeanList.size(), count);
                    }else {
                        updateRecyclerView(contactsBeanList.size(), PAGE_COUNT);
                    }
                }

            }
        });
        contactsAdapter.OnClick(item -> {
            if (item instanceof ContactsBean){
                ContactsBean contactsBean = (ContactsBean) item;
                EventBusMessage eventBusMessage = new EventBusMessage(ContactDetailFragment.TAG);
                eventBusMessage.setArg1(contactsBean);
                EventBus.getDefault().post(eventBusMessage);
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex,toIndex);
    }

    private void getDatas(int fromIndex, int toIndex) {
        if (toIndex <= 0){
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
        ReqAllContacts reqAllContacts = new ReqAllContacts();
        reqAllContacts.setSelectItem("3");
        reqAllContacts.setSearchString("");
        reqAllContacts.setStartIndex(String.valueOf(fromIndex));
        reqAllContacts.setNumberOfRecords(String.valueOf(toIndex));

        disposable = contactsViewModel.reqAll(reqAllContacts);
    }


    private void getContactsNum(){
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
        ReqContactsNum reqContactsNum = new ReqContactsNum();
        reqContactsNum.setSelectItem("3");
        reqContactsNum.setSearchString("");
        contactsViewModel.reqNum(reqContactsNum);
    }

    @Override
    public void bindViewModel() {
        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);


    }

    @Override
    public void subscribeToModel() {

        if (!contactsViewModel.getObservableAll().hasObservers()){
            contactsViewModel.getObservableAll().observe(this,contactsBeans -> {
                if (ContactsFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)){
                    if (null!=contactsBeans){
                        if (contactsBeans.size() >0){
                            contactsBeanList.addAll(contactsBeans);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        }else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    }else {
                        if (null!= contactsBeanList&&contactsBeans.size()>0){
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    }
                }
            });
        }

        if (!contactsViewModel.getObservableNum().hasObservers()){
            contactsViewModel.getObservableNum().observe(this,integer -> {
                if (ContactsFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)){
                    if(null!=integer){
                        maxNum = integer;
                        if (maxNum>= PAGE_COUNT){
                            getDatas(0,PAGE_COUNT);
                        }else {
                            getDatas(0,maxNum);
                        }
                    }else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }
        if (null== contactsBeanList || contactsBeanList.size() <=0){
            getContactsNum();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        contactsViewModel.getObservableAll().setValue(null);
    }
}
