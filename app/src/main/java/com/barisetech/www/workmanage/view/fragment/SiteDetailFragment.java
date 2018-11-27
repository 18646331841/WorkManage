package com.barisetech.www.workmanage.view.fragment;

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
import com.barisetech.www.workmanage.bean.site.ReqSiteBean;
import com.barisetech.www.workmanage.bean.site.ReqSiteInfos;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentSiteDetailBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.SiteViewModel;

import org.greenrobot.eventbus.EventBus;

public class SiteDetailFragment extends BaseFragment {

    public static final String TAG = "SiteDetailFragment";
    FragmentSiteDetailBinding mBinding;

    private static final String SITE_ID = "siteBean";
    private SiteBean siteBean;
    private SiteViewModel siteViewModel;

    public static SiteDetailFragment newInstance(SiteBean siteBean) {
        SiteDetailFragment fragment = new SiteDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SITE_ID, siteBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            siteBean = (SiteBean) getArguments().getSerializable(SITE_ID);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_site_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.site_detail));
        toolbarInfo.setOneText(getString(R.string.modify_site));

        observableToolbar.set(toolbarInfo);
        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.setSiteBean(siteBean);

        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(ModifySiteFragment.TAG);
            eventBusMessage.setArg1(siteBean);
            EventBus.getDefault().post(eventBusMessage);
        });

        mBinding.addSite.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(PipeAddFragment.TAG);
            EventBus.getDefault().post(eventBusMessage);
        });
    }

    @Override
    public void bindViewModel() {
        siteViewModel = ViewModelProviders.of(this).get(SiteViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!siteViewModel.getmObservableSiteInfos().hasObservers()) {
            siteViewModel.getmObservableSiteInfos().observe(this, siteBeans -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (siteBeans != null && siteBeans.size() > 0) {
                        siteBean = siteBeans.get(0);
                        mBinding.setSiteBean(siteBean);
                    } else {
                        ToastUtil.showToast("未查到站点数据");
                    }
                }
            });
        }

        if (siteBean.Name.equals(BaseConstant.DATA_REQUEST_NAME)) {
            getData();
        }
    }

    private void getData() {
        EventBusMessage eventBusMessage = new EventBusMessage(BaseConstant.PROGRESS_SHOW);
        EventBus.getDefault().post(eventBusMessage);

        ReqSiteInfos reqSiteInfos = new ReqSiteInfos();
        reqSiteInfos.setSiteId(String.valueOf(siteBean.SiteId));
        reqSiteInfos.setStartIndex("0");
        reqSiteInfos.setNumberOfRecords("0");

        siteViewModel.reqAllSite(reqSiteInfos);
    }
}
