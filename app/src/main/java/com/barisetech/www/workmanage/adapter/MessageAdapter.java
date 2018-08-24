package com.barisetech.www.workmanage.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.MessageInfo;
import com.barisetech.www.workmanage.databinding.ItemMessageBinding;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Myholder>{
    private static final String TAG = "MessageAdapter";
    private List<? extends MessageInfo> mList;
    private OnItemClickListener mOnItemClickListener;
    public HashMap<Integer,Boolean> map;



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

        map = new HashMap<>();
        for (int i = 0; i<mList.size();i++){
            map.put(i,false);
        }
    }


    /*
     * 全选
     * */
    public void All(){
        Set<Map.Entry<Integer,Boolean>> entries = map.entrySet();
        boolean shouldall = false;
        for (Map.Entry<Integer,Boolean>entry:entries){
            Boolean value = entry.getValue();
            if (!value){
                shouldall = true;
                break;
            }
        }
        for (Map.Entry<Integer,Boolean> entry: entries){
            entry.setValue(shouldall);
        }
        notifyDataSetChanged();
    }

    /*
     * 反选
     * */

    public void neverall() {
        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(!entry.getValue());
        }
        notifyDataSetChanged();
    }

    /*
     * 单选
     * */
    public void singlesel(int postion) {
        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(false);
        }
        map.put(postion, true);
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
        }

        holder.binding.setMessageinfo(messageInfo);
        LogUtil.d(TAG, "messageType = " + messageInfo.getMessageType());
        holder.binding.messageType.setBackgroundResource(messageInfo.getMessageType() == MessageInfo.TYPE_ALARM ?
                R.drawable.type_alarm : R.drawable.type_incident);
        if (flag==1){
            holder.binding.selectItem.setVisibility(View.VISIBLE);
        }else if (flag==2){
            holder.binding.selectItem.setVisibility(View.GONE);
        }

        holder.binding.selectItem.setChecked(map.get(position));
        holder.binding.selectItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                map.put(position, !map.get(position));
                Log.e("flag",""+map.get(position));
                //刷新适配器
                notifyDataSetChanged();

            }
        });
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
