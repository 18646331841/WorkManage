package com.barisetech.www.workmanage.view.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.news.NewsImageInfo;
import com.barisetech.www.workmanage.bean.news.ReqAddNews;
import com.barisetech.www.workmanage.databinding.FragmentNewsAddBinding;
import com.barisetech.www.workmanage.utils.BitmapUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;

/**
 * Created by LJH on 2018/8/21.
 */
public class NewsAddFragment extends BaseFragment{
    public static final String TAG = "NewsAddFragment";

    private FragmentNewsAddBinding mBinding;

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
        mBinding.confirmAdd.setOnClickListener(view -> {
            String title = mBinding.titleEt.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                ToastUtil.showToast("新闻标题不能为空");
            }
            String describe = mBinding.describeEt.getText().toString().trim();
            if (TextUtils.isEmpty(describe)) {
                ToastUtil.showToast("新闻描述不能为空");
            }
            String link = mBinding.linkEt.getText().toString().trim();
            if (TextUtils.isEmpty(link)) {
                ToastUtil.showToast("新闻链接不能为空");
            }

            int type = 1;
            if (mBinding.company.isChecked()) {
                type = 2;
            } else if (mBinding.industry.isChecked()) {
                type = 3;
            }

            BitmapDrawable bitmapDrawable = (BitmapDrawable) mBinding.imgUpload.getDrawable();
            if (null == bitmapDrawable) {
                ToastUtil.showToast("图片不能为空");
                return;
            }
            Bitmap bitmap = bitmapDrawable.getBitmap();
            byte[] imgB = BitmapUtil.bitmap2Bytes(bitmap);
            String imgS = Base64.encodeToString(imgB, Base64.DEFAULT);

            ReqAddNews reqAddNews = new ReqAddNews();
            reqAddNews.setTittle(title);
            reqAddNews.setDescription(describe);
            reqAddNews.setWebUrl(link);
            reqAddNews.setIsAdd("true");
            reqAddNews.setType(String.valueOf(type));
            NewsImageInfo newsImageInfo = new NewsImageInfo();
            newsImageInfo.setCreatDate(TimeUtil.ms2Date(System.currentTimeMillis()));
            SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
//            newsImageInfo.setCreatUser();
        });
    }

    @Override
    public void bindViewModel() {

    }

    @Override
    public void subscribeToModel() {

    }
}
