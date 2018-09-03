package com.barisetech.www.workmanage.view.fragment;

import android.annotation.TargetApi;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ImgSelectAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ImageInfo;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.alarmanalysis.ReqAddAlarmAnalysis;
import com.barisetech.www.workmanage.databinding.FragmentAlarmAnalysisBinding;
import com.barisetech.www.workmanage.utils.BitmapUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.AlarmAnalysisViewModel;
import com.barisetech.www.workmanage.widget.CustomPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class AlarmAnalysisFragment extends BaseFragment {
    public static final String TAG = "AlarmAnalysisFragment";

    private static final String ALARM_ID = "alarmId";

    private FragmentAlarmAnalysisBinding mBinding;
    private AlarmInfo curAlarmInfo;
    public ObservableField<AlarmInfo> alarmInfo;

    private AlarmAnalysisViewModel alarmAnalysisViewModel;

    //相册选择图片请求码
    private final int CODE_GALLERY_REQUEST = 100;
    //裁剪图片请求码
    private final int CODE_RESULT_REQUEST = 101;
    //请求相机
    private final int CODE_CAMERA_REQUEST = 102;

    private final int REASON_WAIT = 1;
    private final int REASON_TEST = 2;
    private final int REASON_NORMAL = 3;
    private final int REASON_MISINFO = 4;
    private final int REASON_DEVICE_FAULT = 5;
    private final int REASON_NET_FAULT = 6;
    private int curReason;

    private int curReadLv;

    private CustomPopupWindow customPopupWindow;
    private Uri CameraImageUri;
    public static final int MAX_IMG_NUM = 6;//最多选择图片数量
    /**
     * 被选择的所有图片路径集合
     */
    private List<String> curImgPaths;
    private ImgSelectAdapter alarmImgAdapter;

    public AlarmAnalysisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curAlarmInfo = (AlarmInfo) getArguments().getSerializable(ALARM_ID);
        }
        curImgPaths = new ArrayList<>();
    }

    public static AlarmAnalysisFragment newInstance(AlarmInfo alarmInfo) {
        AlarmAnalysisFragment fragment = new AlarmAnalysisFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALARM_ID, alarmInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_analysis, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_alarm_analysis));
        observableToolbar.set(toolbarInfo);

        initView();

        return mBinding.getRoot();
    }

    public void initView() {
        alarmInfo = new ObservableField<>();
        alarmInfo.set(curAlarmInfo);
        mBinding.setMyFragment(this);

        mBinding.alarmAnalysisWaitConfirm.setOnClickListener(view -> {
            cleanDownRadioGroup();
            RadioButton radioButton = (RadioButton) view;
            radioButton.setChecked(true);
            curReason = REASON_WAIT;
        });
        mBinding.alarmAnalysisTest.setOnClickListener(view -> {
            cleanDownRadioGroup();
            RadioButton radioButton = (RadioButton) view;
            radioButton.setChecked(true);
            curReason = REASON_TEST;
        });
        mBinding.alarmAnalysisNormal.setOnClickListener(view -> {
            cleanDownRadioGroup();
            RadioButton radioButton = (RadioButton) view;
            radioButton.setChecked(true);
            curReason = REASON_NORMAL;
        });

        mBinding.alarmAnalysisMisinformation.setOnClickListener(view -> {
            cleanUpRadioGroup();
            RadioButton radioButton = (RadioButton) view;
            radioButton.setChecked(true);
            curReason = REASON_MISINFO;
        });
        mBinding.alarmAnalysisDeviceFault.setOnClickListener(view -> {
            cleanUpRadioGroup();
            RadioButton radioButton = (RadioButton) view;
            radioButton.setChecked(true);
            curReason = REASON_DEVICE_FAULT;
        });
        mBinding.alarmAnalysisNetFault.setOnClickListener(view -> {
            cleanUpRadioGroup();
            RadioButton radioButton = (RadioButton) view;
            radioButton.setChecked(true);
            curReason = REASON_NET_FAULT;
        });

        mBinding.alarmAnalysisSuperAdmin.setOnClickListener(view -> {
            curReadLv = BaseConstant.LEVEL_SUPER_ADMINS;
        });
        mBinding.alarmAnalysisAdmin.setOnClickListener(view -> {
            curReadLv = BaseConstant.LEVEL_ADMINS;
        });
        mBinding.alarmAnalysisUser.setOnClickListener(view -> {
            curReadLv = BaseConstant.LEVEL_USERS;
        });

        customPopupWindow = new CustomPopupWindow.Builder()
                .setContext(getContext())
                .setContentView(R.layout.popup_photo_selecter)
                .setwidth(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setheight(LinearLayout.LayoutParams.WRAP_CONTENT)
                .setFouse(true)
                .setOutSideCancel(true)
                .setBackGroudAlpha(getActivity(), 0.7f)
                .setAnimationStyle(R.style.popup_anim_style)
                .builder();

        customPopupWindow.getItemView(R.id.pop_pic).setOnClickListener(view -> {
            choosePicture();
            customPopupWindow.dismiss();
        });

        customPopupWindow.getItemView(R.id.pop_camera).setOnClickListener(view -> {
            openCamera();
            customPopupWindow.dismiss();
        });

        customPopupWindow.getItemView(R.id.pop_cancel).setOnClickListener(view -> {
            customPopupWindow.dismiss();
        });

        alarmImgAdapter = new ImgSelectAdapter(curImgPaths, getContext(), MAX_IMG_NUM);
        alarmImgAdapter.setItemCallBack(itemCallBack);
        alarmImgAdapter.setOnDeleteClick(onDeleteClick);
        mBinding.alarmAnalysisGv.setAdapter(alarmImgAdapter);

        mBinding.alarmAnalysisUpload.setOnClickListener(view -> {
            upload();
        });
    }

    /**
     * 清空上行RadioGroup为未选中状态
     */
    private void cleanUpRadioGroup() {
        mBinding.alarmAnalysisNormal.setChecked(false);
        mBinding.alarmAnalysisTest.setChecked(false);
        mBinding.alarmAnalysisWaitConfirm.setChecked(false);
    }

    /**
     * 清空下行RadioGroup为未选中状态
     */
    private void cleanDownRadioGroup() {
        mBinding.alarmAnalysisMisinformation.setChecked(false);
        mBinding.alarmAnalysisDeviceFault.setChecked(false);
        mBinding.alarmAnalysisNetFault.setChecked(false);
    }

    private ItemCallBack itemCallBack = item -> {
        customPopupWindow.showAtLocation(R.layout.fragment_alarm_analysis, Gravity.BOTTOM, 0, 0);
    };

    private ImgSelectAdapter.OnDeleteClick onDeleteClick = position -> {
        curImgPaths.remove(position);
        alarmImgAdapter.notifyDataSetChanged();
    };

    private void upload() {

        String titleS = mBinding.alarmAnalysisTitleEt.getText().toString().trim();
        if (TextUtils.isEmpty(titleS)) {
            ToastUtil.showToast("标题不能为空");
            return;
        }
        String abstractS = mBinding.alarmAnalysisAbstractEt.getText().toString().trim();
        if (TextUtils.isEmpty(abstractS)) {
            ToastUtil.showToast("摘要不能为空");
            return;
        }

        if (curReason == 0) {
            ToastUtil.showToast("请选择警报原因");
            return;
        }
        if (curReadLv == 0) {
            ToastUtil.showToast("请选择阅读等级");
            return;
        }

        ReqAddAlarmAnalysis reqAddAlarmAnalysis = new ReqAddAlarmAnalysis();
        reqAddAlarmAnalysis.setId("0");
        reqAddAlarmAnalysis.setIsNewReader("false");
        reqAddAlarmAnalysis.setReadLV(String.valueOf(curReadLv));
        reqAddAlarmAnalysis.setTittle(titleS);
        reqAddAlarmAnalysis.setReleaseTime(TimeUtil.ms2Date(System.currentTimeMillis()));
        reqAddAlarmAnalysis.setAlarmID(String.valueOf(curAlarmInfo.getKey()));
        reqAddAlarmAnalysis.setAlarmCause(String.valueOf(curReason));
        reqAddAlarmAnalysis.setAlarmDetail(curAlarmInfo.getDetails());
        String account = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "default");
        reqAddAlarmAnalysis.setAnalyst(account);
        reqAddAlarmAnalysis.setIsAdd("true");
        List<ImageInfo> imageInfos = new ArrayList<>();
        if (null != curImgPaths && curImgPaths.size() > 0) {
            for (int i = 0; i < curImgPaths.size(); i++) {
                String path = curImgPaths.get(i);

                Bitmap imgB = BitmapUtil.compressImage(BitmapFactory.decodeFile(path), 300);
                String imgS = BitmapUtil.bitmapToBase64(imgB);

                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setCreatUser(account);
                imageInfo.setData(imgS);
                imageInfos.add(imageInfo);
            }
        }
        reqAddAlarmAnalysis.setImageList(imageInfos);

        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
        alarmAnalysisViewModel.reqAddOrModifyAnalysis(reqAddAlarmAnalysis);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case CODE_GALLERY_REQUEST:
                    if (null != data) {
                        // 获取返回的图片列表(存放的是图片路径)
                        List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        if (null != path && path.size() > 0) {
                            LogUtil.d(TAG, "CODE_GALLERY_REQUEST uri = " + path);
                            addImg(path);
                        }
                    }
                    break;
                case CODE_CAMERA_REQUEST:
                    LogUtil.d(TAG, "CODE_GALLERY_REQUEST uri = " + CameraImageUri);
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(CameraImageUri);
                    } else {
                        //4.4以下系统使用这个放出处理图片
                        handleImageBeforeKitKat(CameraImageUri);
                    }
                    break;
            }
        }
    }

    /**
     * 相册选择图片方法
     */
    private void choosePicture() {
        int count = MAX_IMG_NUM - curImgPaths.size();
        if (count > 0) {
            MultiImageSelector.create()
                    .showCamera(false) // 是否显示相机. 默认为显示
                    .count(MAX_IMG_NUM - curImgPaths.size()) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                    .multi() // 多选模式, 默认模式;
                    .start(this, CODE_GALLERY_REQUEST);
        }
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        File path = new File(Environment.getExternalStorageDirectory() + BaseApplication.appDir);
        if (!path.exists()) {
            boolean result = path.mkdirs();
            if (!result) {
                return;
            }
        }
        File file = new File(path.getAbsoluteFile(), System
                .currentTimeMillis() + ".jpg");
        LogUtil.d(TAG, file.getAbsolutePath());
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());

            CameraImageUri = getContext().getContentResolver().insert(MediaStore.Images.Media
                    .EXTERNAL_CONTENT_URI, contentValues);
            openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, CameraImageUri);
        } else {
            CameraImageUri = Uri.fromFile(file);
            //指定照片保存路径（SD卡）
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, CameraImageUri);
        }
        startActivityForResult(openCameraIntent, CODE_CAMERA_REQUEST);

    }

    @TargetApi(19)
    private void handleImageOnKitKat(Uri uri) {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long
                        .valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        addImg(imagePath);
    }

    private void handleImageBeforeKitKat(Uri uri) {
        String imagePath = getImagePath(uri, null);
        addImg(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void addImg(String imgPath) {
        curImgPaths.add(imgPath);
        alarmImgAdapter.notifyDataSetChanged();
    }

    private void addImg(List<String> imgPaths) {
        curImgPaths.addAll(imgPaths);
        alarmImgAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != customPopupWindow) {
            customPopupWindow.dismiss();
        }
    }

    @Override
    public void bindViewModel() {
        alarmAnalysisViewModel = ViewModelProviders.of(this).get(AlarmAnalysisViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!alarmAnalysisViewModel.getmObservableAdd().hasObservers()) {
            alarmAnalysisViewModel.getmObservableAdd().observe(this, integer -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer) {
                        if (integer > 0) {
                            ToastUtil.showToast(getString(R.string.alarm_analysis_upload_success));
                        } else {
                            ToastUtil.showToast(getString(R.string.alarm_analysis_upload_fail));

                        }
                    }
                }
            });
        }
    }
}
