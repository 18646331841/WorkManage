package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.utils.BitmapUtil;
import com.barisetech.www.workmanage.view.fragment.AlarmAnalysisFragment;

import java.util.List;

/**
 * Created by LJH on 2018/8/27.
 */
public class ImgSelectAdapter extends BaseAdapter {
    private static final String TAG = "ImgSelectAdapter";

    private LayoutInflater inflater;
    private List<String> mList;
    private int maxNum;
    private OnDeleteClick onDeleteClick;
    private ItemCallBack itemCallBack;

    public ImgSelectAdapter(List<String> mList, Context context, int max) {
        this.mList = mList;
        inflater = LayoutInflater.from(context);
        maxNum = max;
    }

    @Override
    public int getCount() {
        if (null == mList) {
            return 1;
        }
        if (mList.size() < AlarmAnalysisFragment.MAX_IMG_NUM) {
            return mList.size() + 1;
        } else {
            return mList.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final int sign = position;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_img_selector, null);
            holder.imgSrc = (ImageView) convertView.findViewById(R.id.alarm_analysis_item_image_view);
            holder.imgDelete = (ImageView) convertView.findViewById(R.id.alarm_analysis_delete_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == mList.size()) {
            if (position == maxNum) {
                holder.imgSrc.setVisibility(View.GONE);
            } else {
                holder.imgSrc.setImageResource(R.drawable.ic_checkbox2);
                holder.imgSrc.setOnClickListener(view -> {
                    itemCallBack.onClick(sign);
                });
            }
            holder.imgDelete.setVisibility(View.GONE);
        } else {
            int height = holder.imgSrc.getHeight();
            int width = holder.imgSrc.getWidth();
            holder.imgSrc.setImageBitmap(BitmapUtil.getSmallBitmap(mList.get(sign), width, height));
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.imgDelete.setOnClickListener(v -> {
                if (null != onDeleteClick) {
                    onDeleteClick.onDelete(sign);
                }
            });
        }
        return convertView;
    }

    public void setOnDeleteClick(OnDeleteClick onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    public void setItemCallBack(ItemCallBack itemCallBack) {
        this.itemCallBack = itemCallBack;
    }

    public class ViewHolder{
        public ImageView imgSrc;
        public ImageView imgDelete;
    }

    public interface OnDeleteClick {
        void onDelete(int position);
    }
}
