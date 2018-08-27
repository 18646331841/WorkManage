package com.barisetech.www.workmanage.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.databinding.ItemMessageBinding;

import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Myholder>{
    private static final String TAG = "MessageAdapter";
    private List<MessageInfo> mList;
    private OnItemLongClickListener mOnItemClickListener;
    public HashMap<Integer, MessageInfo> map;

    public static final int SHOW_ALL = 1;//全选
    public static final int HIDE_ALL = 2;//取消全选

    private int flag;


    public void setFlag(int i){
        flag = i;
        notifyDataSetChanged();
    }

    @Nullable
    private final ItemCallBack mItemCallBack;

    public MessageAdapter(@Nullable ItemCallBack itemCallBack, List<MessageInfo> messageInfos) {
        mItemCallBack = itemCallBack;
        map = new HashMap<>();
        mList = messageInfos;
    }

//    public void setCommentList(final List<? extends MessageInfo> messageInfos) {
//        if (mList.size() <= 0) {
//            mList.addAll(messageInfos);
//            LogUtil.d(TAG, "mListsize = " + mList.size());
//            notifyItemRangeInserted(0, mList.size());
//        } else {
//            List<MessageInfo> old = new ArrayList<>();
//            old.addAll(mList);
//
//            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//                @Override
//                public int getOldListSize() {
//                    LogUtil.d(TAG, "OldListSize = " + old.size());
//                    return old.size();
//                }
//
//                @Override
//                public int getNewListSize() {
//                    LogUtil.d(TAG, "NewListSize = " + messageInfos.size());
//                    return messageInfos.size();
//                }
//
//                @Override
//                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                    MessageInfo old = mList.get(oldItemPosition);
//                    MessageInfo messageInfo = messageInfos.get(newItemPosition);
//                    if (old.getMessageType() == messageInfo.getMessageType()) {
//                        return old.getId() == messageInfo.getId();
//                    }
//                    return false;
//                }
//
//                @Override
//                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                    MessageInfo old = mList.get(oldItemPosition);
//                    MessageInfo messageInfo = messageInfos.get(newItemPosition);
//                    return old.getContent().equals(messageInfo.getContent());
//                }
//            });
//            mList.clear();
//            mList.addAll(messageInfos);
//            diffResult.dispatchUpdatesTo(this);
//        }
//    }


    /*
     * 全选
     * */
    public void All(){
        for(int i = 0; i < mList.size(); i++) {
            map.put(i, mList.get(i));
        }
        notifyDataSetChanged();
    }

    /*
     * 反选
     * */

    public void neverall() {
        map.clear();
        notifyDataSetChanged();
    }

    /*
     * 单选
     * */
    public void singlesel(int postion) {
        map.put(postion, mList.get(postion));
        notifyDataSetChanged();
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
        } else {
            holder.binding.messageTime.getPaint().setFakeBoldText(false);
        }

        holder.binding.setMessageinfo(messageInfo);
        holder.binding.messageType.setBackgroundResource(messageInfo.getMessageType() == MessageInfo.TYPE_ALARM ?
                R.drawable.type_alarm : R.drawable.type_incident);
        if (flag == SHOW_ALL){
            holder.binding.selectItem.setVisibility(View.VISIBLE);
        }else if (flag == HIDE_ALL){
            holder.binding.selectItem.setVisibility(View.GONE);
        }

        if (null != map.get(position)) {
            holder.binding.selectItem.setChecked(true);
        } else {
            holder.binding.selectItem.setChecked(false);
        }

        holder.binding.selectItem.setOnClickListener(v -> {
            if (v instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) v;
                if (checkBox.isChecked()) {
                    map.put(position, mList.get(position));
                } else {
                    map.remove(position);
                }
            }
        });
        holder.binding.executePendingBindings();
        if (mOnItemClickListener != null){
            holder.binding.getRoot().setOnLongClickListener(v -> {
                if (flag != SHOW_ALL) {
                    mOnItemClickListener.onItemClick(holder.binding.getRoot(), position);
                }
                return true;
            });
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemClickListener) {
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
    public interface OnItemLongClickListener {
        void onItemClick(View view, int position);
    }
}
