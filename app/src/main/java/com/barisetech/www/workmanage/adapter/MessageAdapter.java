package com.barisetech.www.workmanage.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.databinding.ItemMessageBinding;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Myholder>{

    private List<? extends MessageInfo> mList;

    @Nullable
    private final ItemCallBack mItemCallBack;

    public MessageAdapter(@Nullable ItemCallBack itemCallBack) {
        mItemCallBack = itemCallBack;
    }

    public void setCommentList(final List<? extends MessageInfo> messageInfos) {
        if (mList == null) {
            mList = messageInfos;
            notifyItemRangeInserted(0, messageInfos.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mList.size();
                }

                @Override
                public int getNewListSize() {
                    return messageInfos.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    MessageInfo old = mList.get(oldItemPosition);
                    MessageInfo messageInfo = messageInfos.get(newItemPosition);
                    return old.getId() == messageInfo.getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    MessageInfo old = mList.get(oldItemPosition);
                    MessageInfo messageInfo = messageInfos.get(newItemPosition);
                    return old.getContent().equals(messageInfo.getContent());
                }
            });
            mList = messageInfos;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message,
                        parent, false);
        binding.setCallback(mItemCallBack);
        return new Myholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        MessageInfo messageInfo = mList.get(position);
        holder.binding.setMessageinfo(messageInfo);
        holder.binding.messageType.setImageResource(messageInfo.getType() == MessageInfo.TYPE_ALARM ? R.color
                .cardview_shadow_start_color : R.color.cardview_dark_background);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{

        final ItemMessageBinding binding;
        public Myholder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
