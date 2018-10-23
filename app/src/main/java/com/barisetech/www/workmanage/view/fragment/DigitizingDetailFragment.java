package com.barisetech.www.workmanage.view.fragment;

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
import com.barisetech.www.workmanage.bean.digitalizer.DigitalizerBean;
import com.barisetech.www.workmanage.databinding.FragmentDigitizingDetailBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.viewmodel.PipeWorkViewModel;

import org.greenrobot.eventbus.EventBus;

public class DigitizingDetailFragment extends BaseFragment {

    public static final String TAG = "DigitizingDetailFragment";

    FragmentDigitizingDetailBinding mBinding;
    private static final String DIGITIZING = "digitizing";
    private DigitalizerBean digitalizerBean;

    public static DigitizingDetailFragment newInstance(DigitalizerBean DigitalizerBean) {
        DigitizingDetailFragment fragment = new DigitizingDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DIGITIZING, DigitalizerBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            digitalizerBean = (DigitalizerBean) getArguments().getSerializable(DIGITIZING);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_digitizing_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.digitizing_detail));
        String role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
        if (role.equals(BaseConstant.ROLE_SUPER_ADMINS) || role.equals(BaseConstant.ROLE_ADMINS)) {
            toolbarInfo.setOneText("修改");
        }
        observableToolbar.set(toolbarInfo);
        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.setBean(digitalizerBean);

        String role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
        if (role.equals(BaseConstant.ROLE_SUPER_ADMINS) || role.equals(BaseConstant.ROLE_ADMINS)) {
            mBinding.seniorCard.setVisibility(View.VISIBLE);
        } else {
            mBinding.seniorCard.setVisibility(View.GONE);
        }
        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(DigitizingModifyFragment.TAG);
            eventBusMessage.setArg1(digitalizerBean);
            EventBus.getDefault().post(eventBusMessage);
        });

        mBinding.addSite.setOnClickListener(view -> {
            EventBusMessage eventBusMessage = new EventBusMessage(AddSiteFragment.TAG);
            eventBusMessage.setArg1(String.valueOf(digitalizerBean.ID));
            EventBus.getDefault().post(eventBusMessage);
        });

        mBinding.openPipeTab.setOnClickListener(view -> {

        });
    }

    @Override
    public void bindViewModel() {
    }

    @Override
    public void subscribeToModel() {
    }
}
