package com.barisetech.www.workmanage.view.fragment;

import android.annotation.TargetApi;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import com.barisetech.www.workmanage.bean.news.NewsImageInfo;
import com.barisetech.www.workmanage.bean.news.ReqAddNews;
import com.barisetech.www.workmanage.databinding.FragmentNewsAddBinding;
import com.barisetech.www.workmanage.utils.BitmapUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.NewsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LJH on 2018/8/21.
 */
public class NewsAddFragment extends BaseFragment {
    public static final String TAG = "NewsAddFragment";

    private FragmentNewsAddBinding mBinding;
    private NewsViewModel newsViewModel;

    //相册选择图片请求码
    private final int CODE_GALLERY_REQUEST = 100;
    //裁剪图片请求码
    private final int CODE_RESULT_REQUEST = 101;

    private String curImgPath;//当前添加图片的地址

    public NewsAddFragment() {
        // Required empty public constructor
    }

    public static NewsAddFragment newInstance() {
        NewsAddFragment fragment = new NewsAddFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BaseApplication.getInstance().requestPermissions(getActivity());

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_add, container, false);

        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_news_add));
        observableToolbar.set(toolbarInfo);

        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.company.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                mBinding.industry.setChecked(!b);
            }
        });

        mBinding.industry.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (b) {
                mBinding.company.setChecked(!b);
            }
        }));

        mBinding.imgUpload.setOnClickListener(view -> {
            choosePicture();
        });

        mBinding.confirmAdd.setOnClickListener(view -> {
            String title = mBinding.titleEt.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                ToastUtil.showToast("新闻标题不能为空");
                return;
            }
            String describe = mBinding.describeEt.getText().toString().trim();
            if (TextUtils.isEmpty(describe)) {
                ToastUtil.showToast("新闻描述不能为空");
                return;
            }
            String link = mBinding.linkEt.getText().toString().trim();
            if (TextUtils.isEmpty(link)) {
                ToastUtil.showToast("新闻链接不能为空");
                return;
            }

            int type = 1;
            if (mBinding.company.isChecked()) {
                type = 2;
            } else if (mBinding.industry.isChecked()) {
                type = 3;
            }

            if (TextUtils.isEmpty(curImgPath)) {
                ToastUtil.showToast("图片不能为空");
                return;
            }
            Bitmap imgB = BitmapUtil.compressImage(BitmapFactory.decodeFile(curImgPath), 100);
            String imgS = BitmapUtil.bitmapToBase64(imgB);

            if (!TextUtils.isEmpty(imgS)) {
                ReqAddNews reqAddNews = new ReqAddNews();
                reqAddNews.setTittle(title);
                reqAddNews.setDescription(describe);
                reqAddNews.setWebUrl(link);
                reqAddNews.setIsAdd("true");
                reqAddNews.setType(String.valueOf(type));

                NewsImageInfo newsImageInfo = new NewsImageInfo();
//            newsImageInfo.setCreatDate(TimeUtil.ms2Date(System.currentTimeMillis()));日期不需要也可以
                String user = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
                newsImageInfo.setCreatUser(user);
                newsImageInfo.setData(imgS);
                List<NewsImageInfo> imgList = new ArrayList<>();
                imgList.add(newsImageInfo);

                reqAddNews.setImageList(imgList);

                if (null == newsViewModel) {
                    bindViewModel();
                }

                EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_SHOW));
                newsViewModel.reqAddOrUpdateNews(reqAddNews);
            }
        });
    }

    @Override
    public void bindViewModel() {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        newsViewModel.getmObservableAddResult().observe(this, integer -> {
            if (null != integer) {
                if (integer > 0) {
                    ToastUtil.showToast("新闻添加成功");
                    getActivity().onBackPressed();
                } else {
                    ToastUtil.showToast("新闻添加失败");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case CODE_GALLERY_REQUEST:
                    LogUtil.d(TAG, "CODE_GALLERY_REQUEST uri = " + data.getData());
                    //暂时不用
//                    cropRawPhoto(data.getData());

                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19){
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else {
                        //4.4以下系统使用这个放出处理图片
                        handleImageBeforeKitKat(data);
                    }
                    break;
            }
        }
    }

    /**
     * 相册选择图片方法
     */
    private void choosePicture() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, CODE_GALLERY_REQUEST);
    }

    /**
     * 裁剪图片方法
     *
     * @param uri
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(),uri)){
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        diaplayImage(imagePath);//根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        diaplayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void diaplayImage(String imagePath){
        if (imagePath != null){
            curImgPath = imagePath;
            int width = mBinding.imgUpload.getWidth();
            int height = mBinding.imgUpload.getHeight();
            mBinding.imgUpload.setImageBitmap(BitmapUtil.getSmallBitmap(imagePath, width, height));
        }else {
            ToastUtil.showToast("获取图片失败");
        }
    }
}
