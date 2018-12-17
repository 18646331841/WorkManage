package com.barisetech.www.workmanage.view.fragment.my;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.AuthAdapter;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.AuthBean;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.auth.AuthInfo;
import com.barisetech.www.workmanage.bean.auth.ReqAllAuth;
import com.barisetech.www.workmanage.bean.auth.ReqModifyAuth;
import com.barisetech.www.workmanage.bean.onlineuser.ReqOffline;
import com.barisetech.www.workmanage.bean.pipetap.PipeTapInfo;
import com.barisetech.www.workmanage.bean.pipetap.ReqAllPipeTap;
import com.barisetech.www.workmanage.bean.pipetap.ReqModifyPipeTap;
import com.barisetech.www.workmanage.bean.workplan.ReqAllPlan;
import com.barisetech.www.workmanage.databinding.FragmentAuthDetailBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.OsUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.SystemUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.AuthViewModel;
import com.barisetech.www.workmanage.viewmodel.OnlineUserViewModel;
import com.barisetech.www.workmanage.viewmodel.PipeTapViewModel;
import com.barisetech.www.workmanage.widget.CustomDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AuthDetailFragment extends BaseFragment {
    public static final String TAG = "AuthDetailFragment";
    private String user;

    private List<AuthBean> userList;
    private List<AuthBean> pipeTapList;

    private BaseLoadMoreWrapper userLoadMore;
    private BaseLoadMoreWrapper pipeTapLoadMore;

    private AuthViewModel authViewModel;
    private PipeTapViewModel pipeTapViewModel;
    private OnlineUserViewModel onlineUserViewModel;

    private CustomDialog.Builder builder;
    private CustomDialog mDialog;

    FragmentAuthDetailBinding mBinding;
    private static final String NAME = "name";
    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private int curType = AuthBean.TYPE_USER;

    private PipeTapInfo curPipeTap;

    public static AuthDetailFragment newInstance(String user) {
        AuthDetailFragment fragment = new AuthDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NAME, user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            user = getArguments().getString(NAME, "");
        }
        userList = new ArrayList<>();
        pipeTapList = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(user);
        toolbarInfo.setOneText(getString(R.string.auth_offline_user));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        //普通管理员不显示登录授权
        if (!SystemUtil.isSuperAdmin()) {
            mBinding.radioGroup.setVisibility(View.GONE);
            mBinding.pipetapList.setVisibility(View.VISIBLE);
            mBinding.userList.setVisibility(View.GONE);
        }

        builder = new CustomDialog.Builder(getContext());

        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            showTwoButtonDialog("确认下线该用户？", 0, "管线管理助手", "确定", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    offlineUser();
                }
            }, view1 -> mDialog.dismiss());
        });

        mBinding.user.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                curType = AuthBean.TYPE_USER;
                mBinding.userList.setVisibility(View.VISIBLE);
            } else {
                mBinding.userList.setVisibility(View.GONE);
            }
        });
        mBinding.pipetap.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                curType = AuthBean.TYPE_PIPETAP;
                mBinding.pipetapList.setVisibility(View.VISIBLE);
            } else {
                mBinding.pipetapList.setVisibility(View.GONE);
            }
        });

        initRecyclerView();
    }

    private void showTwoButtonDialog(String alertText, int ImgId,String title,String confirmText, String cancelText, View.OnClickListener conFirmListener, View.OnClickListener cancelListener) {
        mDialog = builder.setMessage(alertText)
                .setImagView(ImgId)
                .setTitle(title)
                .setPositiveButton(confirmText, conFirmListener)
                .setNegativeButton(cancelText, cancelListener)
                .createTwoButtonDialog();
        mDialog.show();
    }

    private void initRecyclerView() {

        AuthAdapter userAdapter = new AuthAdapter(userList, onClickButtonListener);
        userLoadMore = new BaseLoadMoreWrapper(userAdapter);
        userLoadMore.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.userList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.userList.setAdapter(userLoadMore);
        mBinding.userList.setItemAnimator(new DefaultItemAnimator());

        mBinding.userList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                updateUserRecyclerView(userList.size(), PAGE_COUNT);
            }
        });

        AuthAdapter pipeTapAdapter = new AuthAdapter(pipeTapList, onClickButtonListener);
        pipeTapLoadMore = new BaseLoadMoreWrapper(pipeTapAdapter);
        pipeTapLoadMore.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.pipetapList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.pipetapList.setAdapter(pipeTapLoadMore);
        mBinding.pipetapList.setItemAnimator(new DefaultItemAnimator());

        mBinding.pipetapList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                updatePipeTapRecyclerView(pipeTapList.size(), PAGE_COUNT);
            }
        });
    }

    private AuthAdapter.OnClickButtonListener onClickButtonListener = new AuthAdapter.OnClickButtonListener() {
        @Override
        public void accept(AuthBean authBean) {
            if (authBean instanceof AuthInfo) {
                AuthInfo authInfo = (AuthInfo) authBean;
                modifyUser(authInfo, true);
            } else {
                PipeTapInfo pipeTapInfo = (PipeTapInfo) authBean;
                modifyPipeTap(pipeTapInfo, true);
            }
        }

        @Override
        public void refuse(AuthBean authBean) {
            if (authBean instanceof AuthInfo) {
                AuthInfo authInfo = (AuthInfo) authBean;
                modifyUser(authInfo, false);
            } else {
                PipeTapInfo pipeTapInfo = (PipeTapInfo) authBean;
                modifyPipeTap(pipeTapInfo, false);
            }
        }
    };

    private void updateUserRecyclerView(int fromIndex, int toIndex) {
        getUserDatas(fromIndex, toIndex);
    }

    private void getUserDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            userLoadMore.setLoadState(userLoadMore.LOADING_END);
            return;
        }
        userLoadMore.setLoadState(userLoadMore.LOADING);

        ReqAllAuth reqAllAuth = new ReqAllAuth();
        reqAllAuth.Id = "-1";
        reqAllAuth.isGetAll = "false";
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
        reqAllAuth.PesonChecked = "true";
        reqAllAuth.State = "0";
        reqAllAuth.Applicant = user;
        reqAllAuth.Approver = "";

        authViewModel.getAll(reqAllAuth);
    }

    private void updatePipeTapRecyclerView(int fromIndex, int toIndex) {
        getPipeTapDatas(fromIndex, toIndex);
    }

    private void getPipeTapDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            pipeTapLoadMore.setLoadState(pipeTapLoadMore.LOADING_END);
            return;
        }
        pipeTapLoadMore.setLoadState(pipeTapLoadMore.LOADING);

        ReqAllPipeTap reqAllPipeTap = new ReqAllPipeTap();
        reqAllPipeTap.isGetAll = "false";
        reqAllPipeTap.mStartTime = "1970-01-01 00:00:00";
        reqAllPipeTap.mEndTime = TimeUtil.ms2Date(System.currentTimeMillis());
        reqAllPipeTap.startIndex = String.valueOf(formIndex);
        reqAllPipeTap.numberOfRecords = String.valueOf(toIndex);
        reqAllPipeTap.TimeQueryChecked = "false";
        reqAllPipeTap.PesonChecked = "true";
        reqAllPipeTap.State = "0";
        reqAllPipeTap.Applicant = user;
        reqAllPipeTap.Approver = "";

        pipeTapViewModel.getAll(reqAllPipeTap);
    }

    /**
     * 用户审批结果请求
     * @param authInfo
     * @param accept
     */
    private void modifyUser(AuthInfo authInfo, boolean accept) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));

        authInfo.ApproverUserName = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
        authInfo.ApproverIPAdress = OsUtil.getIp();
        authInfo.ApproverTime = TimeUtil.ms2Date(System.currentTimeMillis());
        authInfo.ApproverState = accept;

        ReqModifyAuth reqModifyAuth = new ReqModifyAuth();
        reqModifyAuth.toBean(authInfo);
        reqModifyAuth.isAdd = "false";

        authViewModel.modify(reqModifyAuth);
    }

    /**
     * 阀门审批结果请求
     * @param pipeTapInfo
     * @param accept
     */
    private void modifyPipeTap(PipeTapInfo pipeTapInfo, boolean accept) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));

        pipeTapInfo.Approver = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
        pipeTapInfo.ApproverTime = TimeUtil.ms2Date(System.currentTimeMillis());
        pipeTapInfo.ApproverState = accept;
        curPipeTap = pipeTapInfo;

        ReqModifyPipeTap reqModifyPipeTap = new ReqModifyPipeTap();
        reqModifyPipeTap.toBean(pipeTapInfo);
        reqModifyPipeTap.isApprove = "true";

        pipeTapViewModel.modify(reqModifyPipeTap);
    }

    /**
     * 下线用户
     */
    private void offlineUser() {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));

        ReqOffline reqOffline = new ReqOffline();
        reqOffline.userName = user;

        onlineUserViewModel.offlineUser(reqOffline);
    }

    @Override
    public void bindViewModel() {
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        pipeTapViewModel = ViewModelProviders.of(this).get(PipeTapViewModel.class);
        onlineUserViewModel = ViewModelProviders.of(this).get(OnlineUserViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!authViewModel.getmObservableAll().hasObservers()) {
            authViewModel.getmObservableAll().observe(this, authInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != authInfos) {
                        if (authInfos.size() > 0) {
                            for (AuthInfo authInfo : authInfos) {
                                authInfo.toSuper();
                            }
                            userList.addAll(authInfos);
                            LogUtil.d(TAG, "load user complete = " + authInfos);

                            userLoadMore.setLoadState(userLoadMore.LOADING_COMPLETE);
                        } else {
                            userLoadMore.setLoadState(userLoadMore.LOADING_END);
                        }
                    } else {
                        userLoadMore.setLoadState(userLoadMore.LOADING_END);
                    }
                }
            });
        }

        if (!pipeTapViewModel.getmObservableAll().hasObservers()) {
            pipeTapViewModel.getmObservableAll().observe(this, pipeTapInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != pipeTapInfos) {
                        if (pipeTapInfos.size() > 0) {
                            for (PipeTapInfo pipeTapInfo : pipeTapInfos) {
                                pipeTapInfo.toSuper();
                            }
                            pipeTapList.addAll(pipeTapInfos);
                            LogUtil.d(TAG, "load pipeTap complete = " + pipeTapInfos);

                            pipeTapLoadMore.setLoadState(pipeTapLoadMore.LOADING_COMPLETE);
                        } else {
                            pipeTapLoadMore.setLoadState(pipeTapLoadMore.LOADING_END);
                        }
                    } else {
                        pipeTapLoadMore.setLoadState(pipeTapLoadMore.LOADING_END);
                    }
                }
            });
        }

        if (SystemUtil.isSuperAdmin()) {
            if (userList == null || userList.size() <= 0) {
                getUserDatas(0, PAGE_COUNT);
            }
        }

        if (pipeTapList == null || pipeTapList.size() <= 0) {
            getPipeTapDatas(0, PAGE_COUNT);
        }

        if (!pipeTapViewModel.getmObservableModify().hasObservers()) {
            pipeTapViewModel.getmObservableModify().observe(this, s -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (!TextUtils.isEmpty(s)) {
                        ToastUtil.showToast(s);
                        for (int i = 0; i < pipeTapList.size(); i++) {
                            PipeTapInfo pipeTapInfo = (PipeTapInfo) pipeTapList.get(i);
                            if (pipeTapInfo.ID == curPipeTap.ID) {
                                pipeTapInfo.Approver = curPipeTap.Approver;
                                pipeTapInfo.ApproverState = curPipeTap.ApproverState;
                                pipeTapInfo.ApproverTime = curPipeTap.ApproverTime;
                                pipeTapInfo.toSuper();
                            }
                        }
                        pipeTapLoadMore.setLoadState(pipeTapLoadMore.LOADING_COMPLETE);
                    } else {
                        ToastUtil.showToast("阀门授权请求失败");
                    }
                }
            });
        }
        if (!authViewModel.getmObservableModify().hasObservers()) {
            authViewModel.getmObservableModify().observe(this, s -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (!TextUtils.isEmpty(s)) {
                        ToastUtil.showToast(s);
                    } else {
                        ToastUtil.showToast("用户授权请求失败");
                    }
                }
            });
        }

        if (!onlineUserViewModel.getmObservableOffline().hasObservers()) {
            onlineUserViewModel.getmObservableOffline().observe(this, aBoolean -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (aBoolean) {
                        ToastUtil.showToast("下线成功");
                        getActivity().onBackPressed();
                    } else {
                        ToastUtil.showToast("下线失败");
                    }
                }
            });
        }
    }
}
