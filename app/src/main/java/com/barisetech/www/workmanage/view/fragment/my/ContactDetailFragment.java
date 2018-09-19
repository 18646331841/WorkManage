package com.barisetech.www.workmanage.view.fragment.my;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentContactDetailBinding;

public class ContactDetailFragment extends BaseFragment {


    FragmentContactDetailBinding mBinding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.tv_contacts));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.copyEmail.setOnClickListener(view -> {
           onClickCopy();
        });

        mBinding.toCallPhone.setOnClickListener(view -> {
            Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mBinding.phone.getText()));
            startActivity(dialIntent);
        });

        mBinding.toSendSms.setOnClickListener(view -> {
            Uri smsToUri = Uri.parse("smsto:"+mBinding.phone.getText());
            Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
            startActivity(intent);
        });


    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }

    public void onClickCopy() {
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(mBinding.email.getText());
        Toast.makeText(getContext(), "复制成功", Toast.LENGTH_LONG).show();
    }
}
