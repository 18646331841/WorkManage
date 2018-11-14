package com.barisetech.www.workmanage.view.fragment.my;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.OnlineUserAdapter;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.auth.AuthInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAllAuth;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.databinding.FragmentAuthListBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.viewmodel.AuthViewModel;
import com.barisetech.www.workmanage.viewmodel.OnlineUserViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;

public class AuthListFragment extends BaseFragment {

    public static final String TAG = "AuthListFragment";
    FragmentAuthListBinding mBinding;

    private List<String> userList;
    private OnlineUserAdapter onlineUserAdapter;
    private BaseLoadMoreWrapper loadMoreWrapper;
//    private OnlineUserViewModel onlineUserViewModel;
    private AuthViewModel authViewModel;
    private Disposable disposable;
    private static final String USERS = "users";
    private String user;
    private Map<String, Integer> userCount;

    public static AuthListFragment newInstance(String users) {
        AuthListFragment fragment = new AuthListFragment();
        if (!TextUtils.isEmpty(users)) {
            Bundle bundle = new Bundle();
            bundle.putString(USERS, users);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList = new ArrayList<>();
        userCount = new HashMap<>();
        Bundle bundle = getArguments();
        if (null != bundle) {
            user = bundle.getString(USERS);
        }
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
        if (!TextUtils.isEmpty(user) && userCount.size() <= 0) {
            String[] count = user.split(",");
            if (count.length > 0) {
                for (String name : count) {
                    if (userCount.containsKey(name)) {
                        Integer num = userCount.get(name) + 1;
                        userCount.put(name, num);
                    } else {
                        userCount.put(name, 1);
                    }
                }
            }
        }

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
        onlineUserAdapter.setCountMap(userCount);
        loadMoreWrapper = new BaseLoadMoreWrapper(onlineUserAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.userList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.userList.setAdapter(loadMoreWrapper);
        mBinding.userList.setItemAnimator(new DefaultItemAnimator());

        onlineUserAdapter.OnClick(item -> {
            String user = (String) item;
            if (userCount.size() > 0) {
                if (userCount.containsKey(user)) {
                    userCount.remove(user);
                    if (userCount.size() <= 0) {
                        this.user = "";
                    }
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                }
            }
            EventBusMessage eventBusMessage = new EventBusMessage(AuthDetailFragment.TAG);
            eventBusMessage.setArg1(user);
            EventBus.getDefault().post(eventBusMessage);
        });
    }

//    private void getDatas() {
//        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
//
//        disposable = onlineUserViewModel.getAll();
//    }

    private void getUserDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqAllAuth reqAllAuth = new ReqAllAuth();
        reqAllAuth.Id = "-1";
        reqAllAuth.isGetAll = "true";
        reqAllAuth.mStartTime = "1970-01-01 00:00:00";
        reqAllAuth.mEndTime = TimeUtil.ms2Date(System.currentTimeMillis());
        String ipPort = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_IP_PORT, "");
        if (!TextUtils.isEmpty(ipPort)) {
            String[] split = ipPort.split("_");
            if (split.length > 1) {
                reqAllAuth.serverIP = split[0];
                reqAllAuth.serverPort = split[1];
                reqAllAuth.serverName = ipPort;
            }
        }
        reqAllAuth.startIndex = String.valueOf(formIndex);
        reqAllAuth.numberOfRecords = String.valueOf(toIndex);
        reqAllAuth.TimeQueryChecked = "false";
        reqAllAuth.PesonChecked = "false";
        reqAllAuth.State = "0";
        reqAllAuth.Applicant = user;
        reqAllAuth.Approver = "";

        authViewModel.getAll(reqAllAuth);
    }

    @Override
    public void bindViewModel() {
//        onlineUserViewModel = ViewModelProviders.of(this).get(OnlineUserViewModel.class);
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
    }

    @Override
    public void subscribeToModel() {
//        if (!onlineUserViewModel.getmObservableAll().hasObservers()) {
//            onlineUserViewModel.getmObservableAll().observe(this, users -> {
//                if (AuthListFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
//                    if (null != users) {
//                        if (users.size() > 0) {
//                            userList.addAll(users);
//                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
//                        } else {
//                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                        }
//                    } else {
//                        if (null != userList && userList.size() > 0) {
//                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                        }
//                    }
//                }
//            });
//        }

        if (!authViewModel.getmObservableAll().hasObservers()) {
            authViewModel.getmObservableAll().observe(this, authInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != authInfos) {
                        if (authInfos.size() > 0) {
                            //使用set排除重复项
                            Set<String> set = new HashSet<>();
                            for (AuthInfo authInfo : authInfos) {
                                set.add(authInfo.ApplicatorName);
                            }

                            userList.addAll(set);
                            LogUtil.d(TAG, "load authList complete = " + authInfos);

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

        if (null == userList || userList.size() <= 0) {
            getUserDatas(0, 10);
        }
    }
}
