package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.news.NewsImageInfo;
import com.barisetech.www.workmanage.bean.news.NewsInfo;
import com.barisetech.www.workmanage.utils.BitmapUtil;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LJH on 2018/8/22.
 */
public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "NewsListAdapter";

    private List<NewsInfo> datas; // 数据源
    private Context context;    // 上下文Context

    private Handler mHandler = new Handler(Looper.getMainLooper()); //获取主线程的Handler

    private ItemCallBack itemCallBack;
    private int imgWidth;
    private int imgHeight;

    public NewsListAdapter(List<NewsInfo> datas, Context context) {
        // 初始化变量
        this.datas = datas;
        this.context = context;
    }

//    public NewsListAdapter(Context context) {
//        // 初始化变量
//        this.context = context;
//        datas = new ArrayList<>();
//    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setItemCallBack(ItemCallBack itemCallBack) {
        this.itemCallBack = itemCallBack;
    }

    // 正常item的ViewHolder，用以缓存findView操作
    class NormalHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView describe;
        private TextView time;
        private ImageView imageView;
        private ConstraintLayout constraintLayout;

        public NormalHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title_tv);
            describe = (TextView) itemView.findViewById(R.id.news_describe_tv);
            time = (TextView) itemView.findViewById(R.id.news_time_tv);
            imageView = (ImageView) itemView.findViewById(R.id.news_img);
            constraintLayout = itemView.findViewById(R.id.news_list_item_cl);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news_list, parent, false);
        return new NormalHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        NormalHolder normalHolder = (NormalHolder) holder;
        NewsInfo newsInfo = datas.get(position);
        normalHolder.title.setText(newsInfo.getTittle());
        normalHolder.describe.setText(newsInfo.getDescription());
        normalHolder.time.setText(newsInfo.getReleaseTime());
        List<NewsImageInfo> images = newsInfo.getImage();
        if (null != images && images.size() > 0) {
            if (imgWidth <= 0) {
                imgWidth = DisplayUtil.dip2px(context, 110);
                imgHeight = DisplayUtil.dip2px(context, 80);
                LogUtil.d(TAG, "width = " + imgWidth + " height = " + imgHeight);
            }

            Bitmap bitmap = BitmapUtil.stringToBitmap(images.get(0).getData(), imgWidth, imgHeight);

            if (null != bitmap) {
                normalHolder.imageView.setImageBitmap(bitmap);
                normalHolder.imageView.setBackgroundColor(Color.TRANSPARENT);
                normalHolder.imageView.setVisibility(View.VISIBLE);
            } else {
                normalHolder.imageView.setImageBitmap(null);
                normalHolder.imageView.setVisibility(View.INVISIBLE);
            }
        }

        normalHolder.constraintLayout.setOnClickListener(view -> {
            if (null != itemCallBack) {
                itemCallBack.onClick(datas.get(position));
            }
        });
    }
}
