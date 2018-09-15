package com.barisetech.www.workmanage.view.fragment;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ImgSelectAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.databinding.FragmentSignInBinding;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.widget.CustomPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class SignInFragment extends BaseFragment {

    public static final String TAG="SignInFragment";

    FragmentSignInBinding mBinding;

    public AMapLocationClient mLocationClient = null;

    public AMapLocationClientOption mLocationOption = null;

    private double longitude;
    private double latitude;


    //相册选择图片请求码
    private final int CODE_GALLERY_REQUEST = 100;
    //裁剪图片请求码
    private final int CODE_RESULT_REQUEST = 101;
    //请求相机
    private final int CODE_CAMERA_REQUEST = 102;

    private int curReason;

    private int curReadLv;

    private CustomPopupWindow customPopupWindow;
    private Uri CameraImageUri;
    public static final int MAX_IMG_NUM = 6;//最多选择图片数量
    /**
     * 被选择的所有图片路径集合
     */
    private List<String> curImgPaths = new ArrayList<>();
    private ImgSelectAdapter alarmImgAdapter;


    public static SignInFragment newInstance() {
        SignInFragment fragment = new  SignInFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.tv_sign_in));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    public AMapLocationListener mLocationListener = aMapLocation -> {
        if (aMapLocation!=null){
            if (aMapLocation.getErrorCode()==0){
                latitude = aMapLocation.getLatitude();
                longitude = aMapLocation.getLongitude();
                Log.e("qqq","longtude:"+longitude+",latitude:"+latitude);
            }else {
                ToastUtil.showToast("定位失败");
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

            }
        }

    };

    private void initView() {
        mLocationClient = new AMapLocationClient(getContext());
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
        mLocationOption.setInterval(3000);

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
        mBinding.signInGv.setAdapter(alarmImgAdapter);

    }

    private ItemCallBack itemCallBack = item -> {
        customPopupWindow.showAtLocation(R.layout.fragment_sign_in, Gravity.BOTTOM, 0, 0);
    };

    private ImgSelectAdapter.OnDeleteClick onDeleteClick = position -> {
        curImgPaths.remove(position);
        alarmImgAdapter.notifyDataSetChanged();
    };

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

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

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
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }
}
