package com.barisetech.www.workmanage.view.fragment.my;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentMyBinding;
import com.barisetech.www.workmanage.utils.DataCleanManagerUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.widget.CustomDialog;

import org.greenrobot.eventbus.EventBus;

public class MyFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "MyFragment";
    FragmentMyBinding mBinding;

    private CustomDialog.Builder builder;
    private CustomDialog mDialog;
    private static final String COUNT = "count";
    private String count;

    public static MyFragment newInstance(String count) {
        MyFragment fragment = new MyFragment();
        if (!TextUtils.isEmpty(count)) {
            Bundle bundle = new Bundle();
            bundle.putString(COUNT, count);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            count = bundle.getString(COUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_myself));
        observableToolbar.set(toolbarInfo);
        builder = new CustomDialog.Builder(getContext());
        mBinding.itemAbout.setOnClickListener(this);
        mBinding.itemAuthorizationManage.setOnClickListener(this);
        mBinding.itemClearCache.setOnClickListener(this);
        mBinding.itemContacts.setOnClickListener(this);
        mBinding.itemEventType.setOnClickListener(this);
        mBinding.itemFinger.setOnClickListener(this);
        mBinding.itemNotDisturb.setOnClickListener(this);
        mBinding.itemSound.setOnClickListener(this);
        mBinding.itemInfo.setOnClickListener(this);

        if (!TextUtils.isEmpty(count)) {
            mBinding.authorizationManageNum.setText(count);
            mBinding.authorizationManageNum.setVisibility(View.VISIBLE);
        }

        return mBinding.getRoot();
    }

    private void clearCount() {
        count = "";
        mBinding.authorizationManageNum.setText(count);
        mBinding.authorizationManageNum.setVisibility(View.GONE);
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.item_info:
                EventBus.getDefault().post(new EventBusMessage(MyInfoFragment.TAG));
                break;
            case R.id.item_finger:
                EventBus.getDefault().post(new EventBusMessage(FingerOpenFragment.TAG));
                break;
            case R.id.item_sound:
                EventBus.getDefault().post(new EventBusMessage(SoundFragment.TAG));
                break;
            case R.id.item_event_type:
                EventBus.getDefault().post(new EventBusMessage(EventTypeFragment.TAG));
                break;
            case R.id.item_authorization_manage:
                String role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
                if (role.equals(BaseConstant.ROLE_ADMINS) || role.equals(BaseConstant.ROLE_SUPER_ADMINS)) {
                    EventBusMessage authList = new EventBusMessage(AuthListFragment.TAG);
                    authList.setArg1(count);
                    EventBus.getDefault().post(authList);
                    clearCount();
                }
                break;
            case R.id.item_not_disturb:
                EventBus.getDefault().post(new EventBusMessage(NotDisturbFragment.TAG));
                break;
            case R.id.item_clear_cache:
                showTwoButtonDialog("是否清除缓存", 0, null, "确定", "取消", v -> {
//                    DataCleanManagerUtil.cleanSharedPreference(getContext());
                    DataCleanManagerUtil.cleanDatabases(getContext());
                    DataCleanManagerUtil.cleanInternalCache(getContext());
                    mDialog.dismiss();
                    BaseApplication.getInstance().reBuildDB();
                    SharedPreferencesUtil.getInstance().clearAll();
                    }, v -> mDialog.dismiss());
                break;
            case R.id.item_contacts:
                EventBus.getDefault().post(new EventBusMessage(ContactsFragment.TAG));
                break;
            case R.id.item_about:
                EventBus.getDefault().post(new EventBusMessage(AboutFragment.TAG));
                break;

        }

    }

    private void showTwoButtonDialog(String alertText, int ImgId,String title,String confirmText, String cancelText, View.OnClickListener conFirmListener, View.OnClickListener cancelListener) {
        mDialog = builder.setMessage(alertText)
                .setImagView(ImgId)
                .setTitle(title)
                .setPositiveButton(confirmText, conFirmListener)
                .setNegativeButton(cancelText, cancelListener)
                .createTwoButtonDialog();
        mDialog.show();
    }
}
