package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.digitalizer.DigitalizerBean;

import java.util.List;

public class DigitizingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "SiteAdapter";

    private List<DigitalizerBean> mList;
    private Context ctx;
    private ItemCallBack callBack;
    private OnItemLongClickListener mOnItemClickListener;

    public DigitizingAdapter(List<DigitalizerBean> list, Context context) {
        this.mList = list;
        this.ctx = context;
    }

    public void onClick(ItemCallBack callBack) {
        this.callBack = callBack;
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
        ViewHolder viewHolder = (ViewHolder) holder;
        DigitalizerBean bean = mList.get(position);
        viewHolder.img.setBackgroundResource(R.drawable.ic_digitizing);
        viewHolder.info.setText(bean.Name);

        viewHolder.l_item.setOnClickListener(view -> {
            if (callBack != null) {
                callBack.onClick(mList.get(position));
            }
        });

        viewHolder.l_item.setOnLongClickListener(view -> {
            mOnItemClickListener.onItemClick(view, position);
            return true;
        });
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        CheckBox select_item;
        TextView info;
        ConstraintLayout l_item;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_img);
            select_item = itemView.findViewById(R.id.select_item);
            info = itemView.findViewById(R.id.item_data);
            l_item = itemView.findViewById(R.id.l_item);
        }
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, int position);
    }
}
