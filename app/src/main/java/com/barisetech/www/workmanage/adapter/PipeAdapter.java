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
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;

import java.util.List;

public class PipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<PipeInfo> mList;
    private Context ctx;
    private ItemCallBack callBack;
    private SiteAdapter.OnItemLongClickListener mOnItemClickListener;

    public PipeAdapter(List<PipeInfo> list, Context context, ItemCallBack itemCallBack){
        this.mList = list;
        this.ctx = context;
        callBack = itemCallBack;
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
        PipeInfo pipeinfo = mList.get(position);
        viewHolder.img.setBackgroundResource(R.drawable.ic_line);
        viewHolder.tv_pipe_name.setText(pipeinfo.Name);
        viewHolder.l_item.setOnClickListener(view -> {
            if (callBack!=null){
                callBack.onClick(mList.get(position));
            }
        });

        viewHolder.l_item.setOnLongClickListener(view -> {
            if (null != mOnItemClickListener) {

                mOnItemClickListener.onItemClick(view,position);
            }
            return true;
        });
    }

    public void setOnItemLongClickListener(SiteAdapter.OnItemLongClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tv_pipe_name;
        ConstraintLayout l_item;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_img);
            tv_pipe_name = itemView.findViewById(R.id.item_data);
            l_item = itemView.findViewById(R.id.l_item);
        }
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, int position);
    }
}
