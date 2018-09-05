package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.databinding.ItemAlarmlistBinding;

import java.util.List;

public class AlarmlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MessageInfo> mList;
    private Context ctx;
    private ItemCallBack itemCallBack;

    public AlarmlistAdapter(List<MessageInfo> list, Context context, @NonNull ItemCallBack itemCallBack) {
        this.mList = list;
        this.ctx = context;
        this.itemCallBack = itemCallBack;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlarmlistBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout
                .item_alarmlist, parent, false);
        binding.setCallback(itemCallBack);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            MessageInfo messageInfo = mList.get(position);

            myHolder.binding.setMessageinfo(messageInfo);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        final ItemAlarmlistBinding binding;
        public MyHolder(ItemAlarmlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
