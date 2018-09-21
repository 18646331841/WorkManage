package com.barisetech.www.workmanage.view.fragment.workplan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioButton;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.PlanContactsListAdapter;
import com.barisetech.www.workmanage.adapter.PlanListAdapter;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.contacts.ReqAllContacts;
import com.barisetech.www.workmanage.bean.contacts.ReqContactsNum;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.workplan.ReqAddPlan;
import com.barisetech.www.workmanage.bean.workplan.ReqAllPlan;
import com.barisetech.www.workmanage.bean.workplan.ReqPlanNum;
import com.barisetech.www.workmanage.databinding.FragmentPlanContactsListBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.view.fragment.IncidentDetailsFragment;
import com.barisetech.www.workmanage.viewmodel.ContactsViewModel;
import com.barisetech.www.workmanage.viewmodel.PlanViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class FirstPublishFragment extends BaseFragment {
    public static final String TAG = "FirstPublishFragment";

    FragmentPlanContactsListBinding mBinding;
    private Disposable curDisposable;
    private Disposable numDisposable;
    private List<ContactsBean> contactsBeanList;
    private PlanContactsListAdapter planContactsListAdapter;
    private ContactsViewModel contactsViewModel;

    private String curSearch = "";

    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private BaseLoadMoreWrapper loadMoreWrapper;

    public FirstPublishFragment() {
        // Required empty public constructor
    }

    public static FirstPublishFragment newInstance() {
        FirstPublishFragment fragment = new FirstPublishFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsBeanList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_contacts_list, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_plan_publish_plan));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.planPublicSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                closeDisposable();
                contactsBeanList.clear();
                curSearch = textView.getText().toString();
                getListNums();
            }
            return false;
        });

        initRecyclerView();
    }

    private void closeDisposable() {
        if (curDisposable != null) {
            curDisposable.dispose();
        }
        if (numDisposable != null) {
            numDisposable.dispose();
        }
    }

    private void initRecyclerView() {

        planContactsListAdapter = new PlanContactsListAdapter(contactsBeanList, getContext(), itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(planContactsListAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.planPublishContactsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.planPublishContactsList.setAdapter(loadMoreWrapper);
        mBinding.planPublishContactsList.setItemAnimator(new DefaultItemAnimator());

        mBinding.planPublishContactsList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (contactsBeanList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - contactsBeanList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(contactsBeanList.size(), count);
                    } else {
                        updateRecyclerView(contactsBeanList.size(), PAGE_COUNT);
                    }
                }
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);
    }

    private void getDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqAllContacts reqAllContacts = new ReqAllContacts();
        reqAllContacts.setStartIndex(String.valueOf(formIndex));
        reqAllContacts.setNumberOfRecords(String.valueOf(toIndex));
        reqAllContacts.setSelectItem("3");
        reqAllContacts.setSearchString(curSearch);

        curDisposable = contactsViewModel.reqAll(reqAllContacts);
    }

    private void getListNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqContactsNum reqContactsNum = new ReqContactsNum();
        reqContactsNum.setSearchString(curSearch);
        reqContactsNum.setSelectItem("3");

        numDisposable = contactsViewModel.reqNum(reqContactsNum);
    }

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof ContactsBean) {
            ContactsBean contactsBean = (ContactsBean) item;
            ReqAddPlan reqAddPlan = new ReqAddPlan();
            reqAddPlan.PersonLiable = contactsBean.getName();

            EventBusMessage eventBusMessage = new EventBusMessage(SecondPublishFragment.TAG);
            eventBusMessage.setArg1(reqAddPlan);
            EventBus.getDefault().post(eventBusMessage);
        }
    };

    @Override
    public void bindViewModel() {
        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!contactsViewModel.getObservableAll().hasObservers()) {
            contactsViewModel.getObservableAll().observe(this, contactsBeans -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != contactsBeans) {
                        if (contactsBeans.size() > 0) {
                            contactsBeanList.addAll(contactsBeans);
                            LogUtil.d(TAG, "load complete = " + contactsBeans);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }

        if (!contactsViewModel.getObservableNum().hasObservers()) {
            contactsViewModel.getObservableNum().observe(this, integer -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer) {
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
            getListNums();
        }
    }
}
