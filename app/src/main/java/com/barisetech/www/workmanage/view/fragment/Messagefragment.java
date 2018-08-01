package com.barisetech.www.workmanage.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.adapter.MessageAdapter;
import com.barisetech.www.workmanage.adapter.MessageCallBack;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.databinding.FragmentMessageBinding;
import com.barisetech.www.workmanage.R;

public class Messagefragment extends Fragment {

    public static final String TAG = "Messagefragment";

    private FragmentMessageBinding mBinding;

    public Messagefragment() {
    }

    public static Messagefragment newInstance() {
        Messagefragment fragment = new Messagefragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);

        MessageAdapter messageAdapter = new MessageAdapter(messageCallBack);
        mBinding.messageRecyclerView.setAdapter(messageAdapter);

        return mBinding.getRoot();
    }

    private MessageCallBack messageCallBack = new MessageCallBack() {
        @Override
        public void onClick(MessageInfo messageInfo) {

        }
    };
}
