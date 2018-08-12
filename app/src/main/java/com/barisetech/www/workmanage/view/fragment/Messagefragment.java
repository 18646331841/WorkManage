package com.barisetech.www.workmanage.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.adapter.MessageAdapter;
import com.barisetech.www.workmanage.adapter.MessageCallBack;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.databinding.FragmentMessageBinding;
import com.barisetech.www.workmanage.R;

import org.greenrobot.eventbus.EventBus;

public class Messagefragment extends BaseFragment implements View.OnClickListener {

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
        setToolBarHeight(mBinding.toolbar);
        mBinding.messageRecyclerView.setAdapter(messageAdapter);
        mBinding.imgWarn.setOnClickListener(this);
        mBinding.imgAnalysisWarn.setOnClickListener(this);
        mBinding.imgEvent.setOnClickListener(this);
        mBinding.imgNews.setOnClickListener(this);
        return mBinding.getRoot();
    }

    private MessageCallBack messageCallBack = new MessageCallBack() {
        @Override
        public void onClick(MessageInfo messageInfo) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_warn:
                EventBus.getDefault().post(new EventBusMessage(AlarmListFragment.TAG));
                break;
            case R.id.img_event:
                EventBus.getDefault().post(new EventBusMessage(EventFragment.TAG));
                break;
            case R.id.img_analysis_warn:
                EventBus.getDefault().post(new EventBusMessage(AnalysisWarnFragment.TAG));
                break;
            case R.id.img_news:
                EventBus.getDefault().post(new EventBusMessage(NewsFragment.TAG));
                break;
        }

    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
