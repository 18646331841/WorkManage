package com.barisetech.www.workmanage.view.fragment;

import android.annotation.TargetApi;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ImgSelectAdapter;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.base.BaseApplication;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ImageInfo;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.signin.ReqSignIn;
import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;
import com.barisetech.www.workmanage.databinding.FragmentSignInBinding;
import com.barisetech.www.workmanage.utils.BitmapUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.SignInViewModel;
import com.barisetech.www.workmanage.widget.CustomDialog;
import com.barisetech.www.workmanage.widget.CustomPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class SignInFragment extends BaseFragment {

    public static final String TAG = "SignInFragment";

    FragmentSignInBinding mBinding;
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption;
    private SignInViewModel signInViewModel;

    private double longitude;
    private double latitude;
    private double confirmLongitude;
    private double confirmLatitude;

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

    private static final String SITE = "site";
    private TaskSiteBean curSiteBean;
    private static final int NORMAL = 1;
    private static final int EXCEPTION = 2;
    private int curSiteState = NORMAL;
    private Disposable curDisposable;
    private int curState = 3;

    private CustomDialog.Builder builder;
    private CustomDialog mDialog;

    public static SignInFragment newInstance(TaskSiteBean taskSiteBean) {
        SignInFragment fragment = new SignInFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SITE, taskSiteBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curSiteBean = (TaskSiteBean) getArguments().getSerializable(SITE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_plan_sign_in));
        observableToolbar.set(toolbarInfo);
        initView();
        return mBinding.getRoot();
    }

    public AMapLocationListener mLocationListener = aMapLocation -> {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                latitude = aMapLocation.getLatitude();
                longitude = aMapLocation.getLongitude();

                //TODO
                curSiteBean.Latitude = 39.889871;
                curSiteBean.Longitude = 116.355843;
                curSiteBean.range = 50;

                boolean inArea = isInArea(latitude, longitude, curSiteBean.Latitude, curSiteBean.Longitude);
                if (inArea) {
                    mBinding.planSignInArea.setText(getString(R.string.plan_sign_in_in_area));
                    mBinding.planSignInArea.setTextColor(getResources().getColor(R.color.text_black));
                    mBinding.planSignInSite.setText(curSiteBean.Name);
                    mBinding.planSignInSite.setVisibility(View.VISIBLE);
                    mBinding.planSignInCheckIn.setVisibility(View.VISIBLE);
                    mBinding.planSignInWriteLocation.setVisibility(View.GONE);
                    curState = 1;
                } else {
                    mBinding.planSignInCheckIn.setVisibility(View.GONE);
                    mBinding.planSignInWriteLocation.setVisibility(View.VISIBLE);
                    mBinding.planSignInArea.setText(getString(R.string.plan_sign_in_out_area));
                    mBinding.planSignInArea.setTextColor(getResources().getColor(R.color.plan_sign_in_red_color));
                    mBinding.planSignInSite.setVisibility(View.GONE);
                    curState = 2;
                }

                Log.d(TAG, "longtude:" + longitude + ",latitude:" + latitude);
            } else {
                curState = 2;
//                ToastUtil.showToast("定位失败");
                Log.d(TAG, "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

            }
        }

    };

    private boolean isInArea(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        if (curSiteBean.range <= 0) {
            return false;
        }
        DPoint start = new DPoint(startLatitude, startLongitude);
        DPoint end = new DPoint(endLatitude, endLongitude);
        float dis = CoordinateConverter.calculateLineDistance(start, end);
        if (dis < curSiteBean.range) {
            return true;
        }
        return false;
    }

    private void initView() {
        builder = new CustomDialog.Builder(getContext());

        mLocationClient = new AMapLocationClient(getContext());
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(3000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();

        mBinding.planSignInCheckIn.setOnClickListener(view -> {
            confirmLatitude = latitude;
            confirmLongitude = longitude;
            ToastUtil.showToast("记录位置成功");
        });

        mBinding.planSignInWriteLocation.setOnClickListener(view -> {
            confirmLatitude = latitude;
            confirmLongitude = longitude;
            if (confirmLongitude > 0 && confirmLatitude > 0) {
                ToastUtil.showToast("记录位置成功");
            } else {
                ToastUtil.showToast("记录位置失败，经纬度为0");
            }
        });

        String date = TimeUtil.ms2YMD(System.currentTimeMillis());
        mBinding.signDate.setText(date);

        mBinding.radioNo.setChecked(true);//默认否
        mBinding.radioNo.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                curSiteState = NORMAL;
            }
        });
        mBinding.radioYes.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (b) {
                curSiteState = EXCEPTION;
            }
        }));

        mBinding.planSignInSubmit.setOnClickListener(view -> {
            signIn();
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
        mBinding.signInGv.setAdapter(alarmImgAdapter);
    }

    private void signIn() {
        String date = TimeUtil.ms2Date(System.currentTimeMillis());
        String remark = mBinding.planSignInRemark.getText().toString();

        String account = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "default");
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
        } else {
            ToastUtil.showToast("没有照片，请添加照片");
            return;
        }

        curSiteBean.DateTime = date;
        curSiteBean.Remark = remark;
        curSiteBean.State = curState;
        curSiteBean.SiteState = curSiteState;
        curSiteBean.UserLatitude = confirmLatitude;
        curSiteBean.UserLongitude = confirmLongitude;
        curSiteBean.WorkImageList = imageInfos;

        ReqSignIn reqSignIn = new ReqSignIn();
        reqSignIn.toTaskSite(curSiteBean);

        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
        curDisposable = signInViewModel.reqSignIn(reqSignIn);
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

    private void showSingleButtonDialog(String alertText, int ImgId, String title, String btnText, View
            .OnClickListener onClickListener) {
        mDialog = builder.setMessage(alertText)
                .setTitle(title)
                .setImagView(ImgId)
                .setSingleButton(btnText, onClickListener)
                .createSingleButtonDialog();
        mDialog.show();
    }

    @Override
    public void bindViewModel() {
        signInViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!signInViewModel.getmObservableSignIn().hasObservers()) {
            signInViewModel.getmObservableSignIn().observe(this, siteBean -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != siteBean) {
                        ToastUtil.showToast("打卡成功");
                        if (curSiteBean.isEnd) {
                            showSingleButtonDialog("恭喜您已完成1次巡线任务", R.mipmap.ic_launcher_round, null, "确定", view -> {
                                mDialog.dismiss();
                                getActivity().onBackPressed();
                            });
                        }
                    } else {
                        curSiteBean.State = 4;
                        ToastUtil.showToast("打卡失败");
                    }
                }
            });
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
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }
}
