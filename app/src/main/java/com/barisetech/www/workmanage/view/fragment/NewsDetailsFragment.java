package com.barisetech.www.workmanage.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentNewsDetailsBinding;
import com.barisetech.www.workmanage.utils.LogUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetailsFragment extends BaseFragment {
    public static final String TAG = "NewsDetailsFragment";
    private FragmentNewsDetailsBinding mBinding;

    private static final String ARG_URL = "url";
    private String curUrl;

    public NewsDetailsFragment() {
        // Required empty public constructor
    }

    public static NewsDetailsFragment newInstance(String url) {
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_URL, url);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            curUrl = getArguments().getString(ARG_URL, "");
        }
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

    @Override
    public void onResume() {
        super.onResume();
        mBinding.newsDetailsWv.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.newsDetailsWv.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.newsDetailsWv.destroy();
    }

    private void initView() {
        LogUtil.d(TAG, "url = " + curUrl);

        WebSettings webSettings = mBinding.newsDetailsWv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mBinding.newsDetailsWv.loadUrl(curUrl);
        mBinding.newsDetailsWv.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
