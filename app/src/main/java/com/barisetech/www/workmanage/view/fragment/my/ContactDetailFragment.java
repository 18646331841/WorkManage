package com.barisetech.www.workmanage.view.fragment.my;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.databinding.FragmentContactDetailBinding;
import com.barisetech.www.workmanage.utils.ToastUtil;

public class ContactDetailFragment extends BaseFragment {


    public static final String TAG = "ContactDetailFragment";
    private static String CONTACT = "ContactsBean";
    private ContactsBean contactsBean;


    FragmentContactDetailBinding mBinding;

    public static ContactDetailFragment newInstance(ContactsBean contactsBean) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CONTACT, contactsBean);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            contactsBean = (ContactsBean) getArguments().getSerializable(CONTACT);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_detail, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.tv_contact_detail));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {

        mBinding.tvName.setText(contactsBean.getName());
        mBinding.phone.setText(contactsBean.getTelephone());
        mBinding.email.setText(contactsBean.getEmail());
        mBinding.source.setText(contactsBean.getSource());

        mBinding.copyEmail.setOnClickListener(view -> {
            onClickCopy();
        });

        mBinding.toCallPhone.setOnClickListener(view -> {
            if (hasCall(getActivity())) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mBinding.phone.getText()));
                startActivity(dialIntent);
            } else {
                ToastUtil.showToast("该设备不具备电话功能");
            }
        });

        mBinding.toSendSms.setOnClickListener(view -> {
            Uri smsToUri = Uri.parse("smsto:" + mBinding.phone.getText());
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

    /**
     * 判断是否有电话功能
     *
     * @param activity
     * @return
     */
    public boolean hasCall(Activity activity) {
        TelephonyManager telephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return false;
        } else {
            return true;
        }
    }
}
