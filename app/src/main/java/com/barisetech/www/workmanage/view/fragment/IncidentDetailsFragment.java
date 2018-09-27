package com.barisetech.www.workmanage.view.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentIncidentDetailsBinding;
import com.barisetech.www.workmanage.viewmodel.IncidentViewModel;

import org.greenrobot.eventbus.EventBus;

public class IncidentDetailsFragment extends BaseFragment implements View.OnClickListener{
    public static final String TAG = "IncidentDetailsFragment";

    private static final String INCIDENT_ID = "incident";

    private IncidentViewModel incidentViewModel;
    private IncidentInfo curIncidentInfo;
    public ObservableField<IncidentInfo> incidentInfo;
    FragmentIncidentDetailsBinding mBinding;

    public IncidentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curIncidentInfo = (IncidentInfo) getArguments().getSerializable(INCIDENT_ID);
        }
    }

    public static IncidentDetailsFragment newInstance(IncidentInfo incidentInfo) {
        IncidentDetailsFragment fragment = new IncidentDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(INCIDENT_ID, incidentInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_incident_details, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_incident_detail));
        observableToolbar.set(toolbarInfo);

        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        incidentInfo = new ObservableField<>();
        incidentInfo.set(curIncidentInfo);
        mBinding.setMyFragment(this);

        mBinding.toSiteBt.setOnClickListener(this);
        mBinding.toPipeBt.setOnClickListener(this);
    }

    @Override
    public void bindViewModel() {
        incidentViewModel = ViewModelProviders.of(this).get(IncidentViewModel.class);
        incidentViewModel.setReadIncident(curIncidentInfo.getKey());
    }

    @Override
    public void subscribeToModel() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_site_bt:
                EventBusMessage siteMessage = new EventBusMessage(SiteDetailFragment.TAG);
                SiteBean siteBean = new SiteBean();
                siteBean.SiteId = curIncidentInfo.getSiteId();
                siteBean.Name = BaseConstant.DATA_REQUEST_NAME;
                siteMessage.setArg1(siteBean);
                EventBus.getDefault().post(siteMessage);
                break;
            case R.id.to_pipe_bt:
                EventBusMessage pipeMessage = new EventBusMessage(PipeDetailFragment.TAG);
                PipeInfo pipeInfo = new PipeInfo();
                pipeInfo.PipeId = curIncidentInfo.getPipeId();
                pipeInfo.Name = BaseConstant.DATA_REQUEST_NAME;
                pipeMessage.setArg1(pipeInfo);
                EventBus.getDefault().post(pipeMessage);
                break;
        }
    }
}
