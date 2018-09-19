package com.barisetech.www.workmanage.view.fragment.workplan;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.PlanSiteListAdapter;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.bean.workplan.PlanSiteBean;
import com.barisetech.www.workmanage.bean.workplan.ReqAddPlan;
import com.barisetech.www.workmanage.databinding.FragmentPlanThirdListBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PlanViewModel;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class ThirdPublishFragment extends BaseFragment {
    public static final String TAG = "ThirdPublishFragment";

    FragmentPlanThirdListBinding mBinding;
    private Disposable curDisposable;
    private Disposable numDisposable;
    private List<SiteBean> siteBeanList;
    private PlanSiteListAdapter planContactsListAdapter;
    private SiteViewModel siteViewModel;
    private PlanViewModel planViewModel;

    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private BaseLoadMoreWrapper loadMoreWrapper;

    private static final String PLAN_ADD = "plan";
    private ReqAddPlan curPlanAdd;
    private String curSiteId = "0";
    private Map<String, SiteBean> siteBeanMap;

    public ThirdPublishFragment() {
        // Required empty public constructor
    }

    public static ThirdPublishFragment newInstance(ReqAddPlan reqAddPlan) {
        ThirdPublishFragment fragment = new ThirdPublishFragment();
        if (reqAddPlan != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(PLAN_ADD, reqAddPlan);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        siteBeanList = new ArrayList<>();
        siteBeanMap = new HashMap<>();
        if (getArguments() != null) {
            curPlanAdd = (ReqAddPlan) getArguments().getSerializable(PLAN_ADD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_third_list, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_plan_publish_plan));
        toolbarInfo.setOneText(getString(R.string.title_plan_publish));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.planPublicSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                curSiteId = textView.getText().toString();
                closeDisposable();
                siteBeanList.clear();
                siteBeanMap.clear();
                getDatas(0, maxNum);
            }
            return false;
        });

        initRecyclerView();

        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            if (siteBeanMap.size() <= 0) {
                ToastUtil.showToast("请选择站点");
                return;
            }

            List<PlanSiteBean> siteBeans = new ArrayList<>();
            for (Map.Entry<String, SiteBean> entry : siteBeanMap.entrySet()) {
                PlanSiteBean planSiteBean = new PlanSiteBean();
                planSiteBean.toSite(entry.getValue());
                siteBeans.add(planSiteBean);
            }

            curPlanAdd.Id = "0";
            curPlanAdd.BeginTime = TimeUtil.ms2Date(System.currentTimeMillis());
            curPlanAdd.isAdd = "true";
            curPlanAdd.Publisher = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
            curPlanAdd.State = "2";//未完成状态
            curPlanAdd.PlanSiteList = siteBeans;

            planViewModel.reqAdd(curPlanAdd);
        });
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

        planContactsListAdapter = new PlanSiteListAdapter(siteBeanList, getContext(), itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(planContactsListAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.planPublishSiteList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mBinding.planPublishSiteList.setAdapter(loadMoreWrapper);
        mBinding.planPublishSiteList.setItemAnimator(new DefaultItemAnimator());

        mBinding.planPublishSiteList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (siteBeanList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - siteBeanList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(siteBeanList.size(), count);
                    } else {
                        updateRecyclerView(siteBeanList.size(), PAGE_COUNT);
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

        ReqSiteInfos reqSiteInfos = new ReqSiteInfos();
        reqSiteInfos.setSiteId(curSiteId);
        reqSiteInfos.setStartIndex(String.valueOf(formIndex));
        reqSiteInfos.setNumberOfRecords(String.valueOf(toIndex));

        curDisposable = siteViewModel.reqAllSite(reqSiteInfos);
    }

    private void getListNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        numDisposable = siteViewModel.reqSiteNum();
    }

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof SiteBean) {
            SiteBean siteBean = (SiteBean) item;
            siteBeanMap.put(String.valueOf(siteBean.SiteId), siteBean);
        }
    };

    @Override
    public void bindViewModel() {
        siteViewModel = ViewModelProviders.of(this).get(SiteViewModel.class);
        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!planViewModel.getObservableAdd().hasObservers()) {
            planViewModel.getObservableAdd().observe(this, s -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != s){
                        if (s.equals("成功添加")){
                            ToastUtil.showToast("成功添加");
                            getFragmentManager().popBackStackImmediate(FirstPublishFragment.TAG, FragmentManager
                                    .POP_BACK_STACK_INCLUSIVE);
                        }else if (s.equals("失败添加")){
                            ToastUtil.showToast("失败添加");
                        }
                    }
                }
            });
        }

        if (!siteViewModel.getmObservableSiteInfos().hasObservers()) {
            siteViewModel.getmObservableSiteInfos().observe(this, siteBeans -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != siteBeans) {
                        if (siteBeans.size() > 0) {
                            siteBeanList.addAll(siteBeans);
                            LogUtil.d(TAG, "load complete = " + siteBeans);
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

        if (!siteViewModel.getmObservableSiteNum().hasObservers()) {
            siteViewModel.getmObservableSiteNum().observe(this, integer -> {
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

        if (null == siteBeanList || siteBeanList.size() <= 0) {
            getListNums();
        }
    }
}
