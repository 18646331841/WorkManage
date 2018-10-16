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
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;

import java.util.List;

public class PipeCollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<PipeCollections> mList;
    private Context ctx;
    private ItemCallBack callBack;
    private SiteAdapter.OnItemLongClickListener mOnItemClickListener;

    public PipeCollectionAdapter(List<PipeCollections> list, Context context, ItemCallBack itemCallBack){
        this.mList = list;
        this.ctx = context;
        this.callBack = itemCallBack;
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
        PipeCollections pipeCollections = mList.get(position);
        viewHolder.img.setBackgroundResource(R.drawable.ic_line_collection);
        viewHolder.tv_pipe_name.setText(pipeCollections.getName());
        viewHolder.l_item.setOnClickListener(view -> {
            if (callBack!=null){
                callBack.onClick(mList.get(position));
            }
        });

        viewHolder.l_item.setOnLongClickListener(view -> {
            mOnItemClickListener.onItemClick(view,position);
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
