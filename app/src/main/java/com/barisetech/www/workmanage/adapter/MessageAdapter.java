package com.barisetech.www.workmanage.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.databinding.ItemMessageBinding;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Myholder>{
    private static final String TAG = "MessageAdapter";
    private List<? extends MessageInfo> mList;
    private OnItemClickListener mOnItemClickListener;


    private int flag;


    public void setFlag(int i){
        flag = i;
    }

    @Nullable
    private final ItemCallBack mItemCallBack;

    public MessageAdapter(@Nullable ItemCallBack itemCallBack) {
        mItemCallBack = itemCallBack;
    }

    public void setCommentList(final List<? extends MessageInfo> messageInfos) {
        if (mList == null) {
            mList = messageInfos;
            LogUtil.d(TAG, "mListsize = " + mList.size());
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
                    if (old.getMessageType() == messageInfo.getMessageType()) {
                        return old.getId() == messageInfo.getId();
                    }
                    return false;
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
        if (messageInfo.getMessageType() == MessageInfo.TYPE_INCIDENT && null != messageInfo.getTitle()) {
            holder.binding.messageTime.getPaint().setFakeBoldText(true);
        }

        holder.binding.setMessageinfo(messageInfo);
        LogUtil.d(TAG, "messageType = " + messageInfo.getMessageType());
        holder.binding.messageType.setBackgroundResource(messageInfo.getMessageType() == MessageInfo.TYPE_ALARM ?
                R.drawable.type_alarm : R.drawable.type_incident);
        if (flag==1){
            holder.binding.selectItem.setVisibility(View.VISIBLE);
        }else if (flag==2){
            holder.binding.selectItem.setVisibility(View.GONE);
        }else if (flag ==3){
            holder.binding.selectItem.setChecked(true);
        }

        holder.binding.executePendingBindings();
        if (mOnItemClickListener != null){
            holder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemClick(holder.binding.getRoot(), position);
                    return true;
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
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
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
