package com.barisetech.www.workmanage.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;

public class ManageFragment extends BaseFragment {

    public static final String TAG="ManageFragment";

    private View toolbar;



    public static ManageFragment newInstance() {
        ManageFragment fragment = new  ManageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        setToolBarHeight(toolbar);
        return view;
    }
}
