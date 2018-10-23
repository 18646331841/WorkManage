package com.barisetech.www.workmanage.view.fragment.my;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ContactsAdapter;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.contacts.ReqAllContacts;
import com.barisetech.www.workmanage.bean.contacts.ReqContactsNum;
import com.barisetech.www.workmanage.databinding.FragmentContactsBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.viewmodel.ContactsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ContactsFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "ContactsFragment";
    FragmentContactsBinding mBinding;

    private List<ContactsBean> contactsBeanList;
    private ContactsAdapter contactsAdapter;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private ContactsViewModel contactsViewModel;
    private Disposable disposable;

    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private boolean flag = false;
    private String selectItem = "0";
    private String searchS = "";
    private final String ITEM_PC = "1";
    private final String ITEM_SITE = "2";
    private final String ITEM_USER = "3";

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
        Log.e("onP", "" + selectItem);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.tv_contacts));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        initRecyclerView();
        if (flag) {
            mBinding.searchLayout.tvFilter.setTextColor(getResources().getColor(R.color.blue_text));
            mBinding.lSelect.setVisibility(View.VISIBLE);
            buttonStyle(selectItem);
        }
        mBinding.pipeCollection.setOnClickListener(this);
        mBinding.site.setOnClickListener(this);
        mBinding.user.setOnClickListener(this);

        mBinding.searchLayout.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchS = v.getText().toString();
                getContactsNum();
            }
            return false;
        });


        mBinding.searchLayout.tvFilter.setOnClickListener(v -> {
            if (flag) {
                mBinding.searchLayout.tvFilter.setTextColor(getResources().getColor(R.color.text_black));
                mBinding.lSelect.setVisibility(View.GONE);
                contactsBeanList.clear();
                loadMoreWrapper.notifyDataSetChanged();
                selectItem = "0";
                mBinding.pipeCollection.setTextColor(getResources().getColor(R.color.message_item_gray));
                mBinding.site.setTextColor(getResources().getColor(R.color.message_item_gray));
                mBinding.user.setTextColor(getResources().getColor(R.color.message_item_gray));
                mBinding.site.setBackgroundResource(R.drawable.shape_button_corners_line_gray);
                mBinding.user.setBackgroundResource(R.drawable.shape_button_corners_line_gray);
                mBinding.pipeCollection.setBackgroundResource(R.drawable.shape_button_corners_line_gray);
                flag = false;
            } else {
                mBinding.searchLayout.tvFilter.setTextColor(getResources().getColor(R.color.blue_text));
                mBinding.lSelect.setVisibility(View.VISIBLE);
                flag = true;
            }
        });
    }

    private void initRecyclerView() {
        contactsAdapter = new ContactsAdapter(getContext(), contactsBeanList);
        loadMoreWrapper = new BaseLoadMoreWrapper(contactsAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.contactsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.contactsList.setAdapter(loadMoreWrapper);
        mBinding.contactsList.setItemAnimator(new DefaultItemAnimator());

        mBinding.contactsList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (contactsBeanList.size() == maxNum) {
                    if (maxNum != 0) {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                } else {
                    int count = maxNum - contactsBeanList.size();
                    if (count < PAGE_COUNT) {
                        updateRecyclerView(contactsBeanList.size(), count);
                    } else {
                        updateRecyclerView(contactsBeanList.size(), PAGE_COUNT);
                    }
                }

            }
        });
        contactsAdapter.OnClick(item -> {
            if (item instanceof ContactsBean) {
                ContactsBean contactsBean = (ContactsBean) item;
                EventBusMessage eventBusMessage = new EventBusMessage(ContactDetailFragment.TAG);
                eventBusMessage.setArg1(contactsBean);
                EventBus.getDefault().post(eventBusMessage);
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);
    }

    private void getDatas(int fromIndex, int toIndex) {
        if (toIndex <= 0) {
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
        ReqAllContacts reqAllContacts = new ReqAllContacts();
        reqAllContacts.setSelectItem(selectItem);
        reqAllContacts.setSearchString(searchS);
        reqAllContacts.setStartIndex(String.valueOf(fromIndex));
        reqAllContacts.setNumberOfRecords(String.valueOf(toIndex));

        disposable = contactsViewModel.reqAll(reqAllContacts);
    }


    private void getContactsNum() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
        contactsBeanList.clear();
        maxNum = 0;
        ReqContactsNum reqContactsNum = new ReqContactsNum();
        reqContactsNum.setSelectItem(selectItem);
        reqContactsNum.setSearchString(searchS);
        contactsViewModel.reqNum(reqContactsNum);
    }

    @Override
    public void bindViewModel() {
        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);
    }

    @Override
    public void subscribeToModel() {

        if (!contactsViewModel.getObservableAll().hasObservers()) {
            contactsViewModel.getObservableAll().observe(this, contactsBeans -> {
                if (ContactsFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != contactsBeans) {
                        if (contactsBeans.size() > 0) {
                            contactsBeanList.addAll(contactsBeans);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
//                        if (null != contactsBeanList && contactsBeans.size() > 0) {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                        }
                    }
                }
            });
        }

        if (!contactsViewModel.getObservableNum().hasObservers()) {
            contactsViewModel.getObservableNum().observe(this, integer -> {
                if (ContactsFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer && integer > 0) {
                        maxNum = integer;
                        if (maxNum >= PAGE_COUNT) {
                            getDatas(0, PAGE_COUNT);
                        } else {
                            getDatas(0, maxNum);
                        }
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }
        if (null == contactsBeanList || contactsBeanList.size() <= 0) {
            getContactsNum();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        contactsViewModel.getObservableAll().setValue(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pipe_collection:
                selectItem = "1";
                buttonStyle(selectItem);
                getContactsNum();
                break;
            case R.id.site:
                selectItem = "2";
                buttonStyle(selectItem);
                getContactsNum();
                break;
            case R.id.user:
                selectItem = "3";
                buttonStyle(selectItem);
                getContactsNum();
                break;
            default:
                break;

        }

    }


    private void buttonStyle(String str) {
        switch (str) {
            case "1":
                mBinding.pipeCollection.setTextColor(getResources().getColor(R.color.filter_blue));
                mBinding.site.setTextColor(getResources().getColor(R.color.message_item_gray));
                mBinding.user.setTextColor(getResources().getColor(R.color.message_item_gray));
                mBinding.site.setBackgroundResource(R.drawable.shape_button_corners_line_gray);
                mBinding.user.setBackgroundResource(R.drawable.shape_button_corners_line_gray);
                mBinding.pipeCollection.setBackgroundResource(R.drawable.shape_button_corners_line_blue);
                break;

            case "2":
                mBinding.pipeCollection.setTextColor(getResources().getColor(R.color.message_item_gray));
                mBinding.site.setTextColor(getResources().getColor(R.color.filter_blue));
                mBinding.user.setTextColor(getResources().getColor(R.color.message_item_gray));
                mBinding.site.setBackgroundResource(R.drawable.shape_button_corners_line_blue);
                mBinding.user.setBackgroundResource(R.drawable.shape_button_corners_line_gray);
                mBinding.pipeCollection.setBackgroundResource(R.drawable.shape_button_corners_line_gray);
                break;

            case "3":
                mBinding.pipeCollection.setTextColor(getResources().getColor(R.color.message_item_gray));
                mBinding.site.setTextColor(getResources().getColor(R.color.message_item_gray));
                mBinding.user.setTextColor(getResources().getColor(R.color.filter_blue));
                mBinding.site.setBackgroundResource(R.drawable.shape_button_corners_line_gray);
                mBinding.user.setBackgroundResource(R.drawable.shape_button_corners_line_blue);
                mBinding.pipeCollection.setBackgroundResource(R.drawable.shape_button_corners_line_gray);
                break;
            case "0":
                break;
            default:
                break;
        }
    }


}
