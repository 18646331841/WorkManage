package com.barisetech.www.workmanage.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentNewsDetailsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetailsFragment extends BaseFragment {
    public static final String TAG = "NewsListFragment";
    private FragmentNewsDetailsBinding mBinding;

    public NewsDetailsFragment() {
        // Required empty public constructor
    }

    public static NewsDetailsFragment newInstance() {
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_details, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_news_details));
        observableToolbar.set(toolbarInfo);

        initView();

        return mBinding.getRoot();
    }

    private void initView() {

    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
