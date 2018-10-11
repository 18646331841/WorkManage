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
import android.view.inputmethod.EditorInfo;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.OnlineUserAdapter;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.databinding.FragmentAuthListBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.viewmodel.OnlineUserViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class AuthListFragment extends BaseFragment {

    public static final String TAG = "AuthListFragment";
    FragmentAuthListBinding mBinding;

    private List<String> userList;
    private OnlineUserAdapter onlineUserAdapter;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private OnlineUserViewModel onlineUserViewModel;
    private Disposable disposable;

    public static AuthListFragment newInstance() {
        AuthListFragment fragment = new AuthListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth_list, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_auth_manager));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        initRecyclerView();
        mBinding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                userList.clear();
//                loadMoreWrapper.notifyDataSetChanged();
                //TODO 接口没有查询方法
            }
            return false;
        });
    }

    private void initRecyclerView() {
        onlineUserAdapter = new OnlineUserAdapter(getContext(), userList);
        loadMoreWrapper = new BaseLoadMoreWrapper(onlineUserAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.userList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.userList.setAdapter(loadMoreWrapper);
        mBinding.userList.setItemAnimator(new DefaultItemAnimator());

        onlineUserAdapter.OnClick(item -> {
            String user = (String) item;
            EventBusMessage eventBusMessage = new EventBusMessage(AuthDetailFragment.TAG);
            eventBusMessage.setArg1(user);
            EventBus.getDefault().post(eventBusMessage);
        });
    }

    private void getDatas() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        disposable = onlineUserViewModel.getAll();
    }

    @Override
    public void bindViewModel() {
        onlineUserViewModel = ViewModelProviders.of(this).get(OnlineUserViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!onlineUserViewModel.getmObservableAll().hasObservers()) {
            onlineUserViewModel.getmObservableAll().observe(this, users -> {
                if (AuthListFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != users) {
                        if (users.size() > 0) {
                            userList.addAll(users);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
                        if (null != userList && userList.size() > 0) {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    }
                }
            });
        }

        if (null == userList || userList.size() <= 0) {
            getDatas();
        }
    }
}
