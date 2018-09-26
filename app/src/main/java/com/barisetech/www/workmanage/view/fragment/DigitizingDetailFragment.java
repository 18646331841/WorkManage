package com.barisetech.www.workmanage.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.digitalizer.DigitalizerBean;
import com.barisetech.www.workmanage.databinding.FragmentDigitizingDetailBinding;

public class DigitizingDetailFragment extends BaseFragment{

    public static final String TAG ="DigitizingDetailFragment";
    FragmentDigitizingDetailBinding mBinding;

    private static final String DIGITALIZER = "digitalizer";
    private DigitalizerBean digitalizerBean;

    public static DigitizingDetailFragment newInstance(DigitalizerBean digitalizerBean) {
        DigitizingDetailFragment fragment = new  DigitizingDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DIGITALIZER, digitalizerBean);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            digitalizerBean = new DigitalizerBean();
            digitalizerBean = (DigitalizerBean) getArguments().getSerializable(DIGITALIZER);
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
        observableToolbar.set(toolbarInfo);
        return mBinding.getRoot();
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
