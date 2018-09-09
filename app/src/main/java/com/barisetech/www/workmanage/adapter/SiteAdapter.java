package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.site.SiteBean;

import java.util.HashMap;
import java.util.List;

public class SiteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final String TAG = "SiteAdapter";

    private List<SiteBean> mList;
    private Context ctx;
    private ItemCallBack callBack;
    private OnItemLongClickListener mOnItemClickListener;
    public HashMap<Integer,SiteBean> map;

    public static final int SHOW_ALL = 1;
    public static final int HIDE_ALL = 2;

    private int flag = HIDE_ALL;

    public void setFlag(int i){
        flag = i;
    }


    public SiteAdapter(List<SiteBean> list,Context context){
        this.mList = list;
        map = new HashMap<>();
        this.ctx = context;
    }

    public void OnClick(ItemCallBack callBack){
        this.callBack = callBack;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage, parent, false);
        return new ViewHolder(view);
    }


    /*
     * 全选
     * */
    public void All(){
        for(int i = 0; i < mList.size(); i++) {
            map.put(i, mList.get(i));
        }
    }

    /*
     * 反选
     * */

    public void neverall() {
        map.clear();
    }

    /*
     * 单选
     * */
    public void singlesel(int postion) {
        map.put(postion, mList.get(postion));
        notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        SiteBean bean = mList.get(position);
        viewHolder.img.setBackgroundResource(R.drawable.ic_site);
        viewHolder.tv_site_info.setText(bean.Name);
        if (flag == SHOW_ALL){
            viewHolder.select_item.setVisibility(View.VISIBLE);
            viewHolder.img.setVisibility(View.GONE);
        }else if (flag == HIDE_ALL){
            viewHolder.select_item.setVisibility(View.GONE);
            viewHolder.img.setVisibility(View.VISIBLE);
        }

        if (null != map.get(position)) {
            viewHolder.select_item.setChecked(true);
        } else {
            viewHolder.select_item.setChecked(false);
        }

        viewHolder.select_item.setOnClickListener(v -> {
            if (v instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) v;
                if (checkBox.isChecked()) {
                    map.put(position, mList.get(position));
                } else {
                    map.remove(position);
                }
            }
        });
        viewHolder.l_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack!=null){
                    callBack.onClick(mList.get(position));
                }
            }
        });

        viewHolder.l_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnItemClickListener.onItemClick(view,position);
                return true;
            }
        });
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        CheckBox select_item;
        TextView tv_site_info;
        ConstraintLayout l_item;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_img);
            select_item = itemView.findViewById(R.id.select_item);
            tv_site_info = itemView.findViewById(R.id.item_data);
            l_item = itemView.findViewById(R.id.l_item);
        }
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, int position);
    }
}
