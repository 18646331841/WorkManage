package com.barisetech.www.workmanage.view.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.viewmodel.NewsViewModel;

public class NewsFragment extends BaseFragment {
    public static final String TAG = "NewsFragment";

    private NewsViewModel newsViewModel;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void bindViewModel() {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        //TODO
        newsViewModel.reqNewsById(1);
    }

    @Override
    public void subscribeToModel() {

    }
}
