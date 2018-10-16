package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.SiteAdapter;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.bean.site.ReqDelSiteInfo;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentSiteBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;
import com.barisetech.www.workmanage.widget.QPopuWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.disposables.Disposable;

public class SiteFragment extends BaseFragment implements View.OnClickListener{


    public static final String TAG = "SiteFragment";
    FragmentSiteBinding mBinding;
    private SiteAdapter siteAdapter;
    private SiteViewModel siteViewModel;
    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private BaseLoadMoreWrapper loadMoreWrapper;

    private Disposable curDisposable;

    private List<SiteBean> siteList;

    private Point mPoint = new Point();
    private boolean flag = false;

    public SiteFragment() {

    }

    public static SiteFragment newInstance() {
        SiteFragment fragment = new SiteFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        siteList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_site, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.tv_site));
        toolbarInfo.setOneText(getString(R.string.add_site));
        observableToolbar.set(toolbarInfo);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            EventBus.getDefault().post(new EventBusMessage(AddSiteFragment.TAG));
        });
        mBinding.selectAll.setOnClickListener(this);
        mBinding.selectAllCancel.setOnClickListener(this);
        initRecyclerView();
    }

    private void initRecyclerView() {

        siteAdapter = new SiteAdapter(siteList, getContext());
        loadMoreWrapper = new BaseLoadMoreWrapper(siteAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.siteList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.siteList.setAdapter(loadMoreWrapper);
        mBinding.siteList.setItemAnimator(new DefaultItemAnimator());

        mBinding.siteList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (siteList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - siteList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(siteList.size(), count);
                    } else {
                        updateRecyclerView(siteList.size(), PAGE_COUNT);
                    }
                }
            }
        });
        siteAdapter.OnClick(item -> {
            if (item instanceof SiteBean) {
                SiteBean siteBean = (SiteBean) item;
                EventBusMessage eventBusMessage = new EventBusMessage(SiteDetailFragment.TAG);
                eventBusMessage.setArg1(siteBean);
                EventBus.getDefault().post(eventBusMessage);
            }

        });

        siteAdapter.setOnItemLongClickListener((view, position) -> {
            QPopuWindow.getInstance(getActivity()).builder
                    .bindView(view,0)
                    .setPopupItemList(new String[]{"添加管线", "编辑站点", "删除站点"})
                    .setPointers(mPoint.x,mPoint.y)
                    .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                        @Override
                        public void onPopuListItemClick(View anchorView, int anchorViewPosition, int p) {
                            switch (p){
                                case 0:
//                                    mBinding.longMenu.setVisibility(View.VISIBLE);
//                                    mBinding.selectAll.setText("全选");
//                                    flag = false;
//                                    siteAdapter.setFlag(siteAdapter.SHOW_ALL);
//                                    loadMoreWrapper.notifyDataSetChanged();
                                    EventBusMessage addPipe = new EventBusMessage(PipeAddFragment.TAG);
                                    EventBus.getDefault().post(addPipe);
                                    break;
                                case 1:
                                    EventBusMessage modify = new EventBusMessage(ModifySiteFragment.TAG);
                                    modify.setArg1(siteList.get(position));
                                    EventBus.getDefault().post(modify);
                                    break;
                                case 2:
                                    ReqDelSiteInfo reqDelSiteInfo = new ReqDelSiteInfo();
                                    SiteBean siteBean = siteList.get(position);
                                    reqDelSiteInfo.setSiteId(String.valueOf(siteBean.SiteId));
                                    siteViewModel.reqDelSite(reqDelSiteInfo);
                                    break;
                            }
                        }
                    }).show();
        });
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void PointEventBus(Point point) {
        mPoint = point;
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);

    }

    private void getDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqSiteInfos reqSiteInfos = new ReqSiteInfos();
        reqSiteInfos.setSiteId("0");
        reqSiteInfos.setStartIndex(String.valueOf(formIndex));
        reqSiteInfos.setNumberOfRecords(String.valueOf(toIndex));

        curDisposable = siteViewModel.reqAllSite(reqSiteInfos);
    }

    private void getSiteNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        siteViewModel.reqSiteNum();
    }

    @Override
    public void bindViewModel() {
        siteViewModel = ViewModelProviders.of(this).get(SiteViewModel.class);

    }

    @Override
    public void subscribeToModel() {

        if (!siteViewModel.getmObservableSiteInfos().hasObservers()) {
            siteViewModel.getmObservableSiteInfos().observe(this, siteBeans -> {
                if (SiteFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

                    if (null != siteBeans) {
                        if (siteBeans.size() > 0) {
                            siteList.addAll(siteBeans);
                            LogUtil.d(TAG, "load complete = " + siteBeans);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
//                        if (null !=siteList && siteList.size() > 0) {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                        }
                    }
                }
            });
        }

        if (!siteViewModel. getmObservableSiteNum().hasObservers()) {
            siteViewModel. getmObservableSiteNum().observe(this, integer -> {
                if (SiteFragment.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
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

        if (!siteViewModel.getmObservableSiteDel().hasObservers()) {
            siteViewModel.getmObservableSiteDel().observe(this, flag -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != flag) {
                        if (flag) {
                            ToastUtil.showToast("删除成功");
                            getActivity().onBackPressed();
                        } else {
                            ToastUtil.showToast("删除失败");
                        }
                    } else {
                        ToastUtil.showToast("删除失败");
                    }
                }
            });
        }

        if (null == siteList || siteList.size() <= 0) {
           getSiteNums();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_all:
                if (flag){
                    siteAdapter.neverall();
                    mBinding.selectAll.setText("全选");
                    loadMoreWrapper.notifyDataSetChanged();
                    flag = false;
                }else {
                    siteAdapter.All();
                    mBinding.selectAll.setText("取消全选");
                    loadMoreWrapper.notifyDataSetChanged();
                    flag = true;
                }
                break;
            case R.id.select_all_cancel:
                mBinding.longMenu.setVisibility(View.GONE);
                siteAdapter.setFlag(siteAdapter.HIDE_ALL);
                loadMoreWrapper.notifyDataSetChanged();
                break;
        }
    }
}
