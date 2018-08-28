package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.site.SiteBean;

import java.util.List;

public class SiteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<SiteBean> mList;
    private Context ctx;
    private ItemCallBack callBack;


    public SiteAdapter(List<SiteBean> list,Context context){
        this.mList = list;
        this.ctx = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        SiteBean bean = mList.get(position);
        viewHolder.img.setBackgroundResource(R.drawable.ic_site);
        viewHolder.tv_site_info.setText(bean.getName());
        viewHolder.l_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack!=null){
                    callBack.onClick(mList.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tv_site_info;
        ConstraintLayout l_item;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_img);
            tv_site_info = itemView.findViewById(R.id.item_data);
            l_item = itemView.findViewById(R.id.l_item);
        }
    }
}
