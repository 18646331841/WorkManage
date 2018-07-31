package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.MessageBean;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Myholder>{


    private List<MessageBean> mList = new ArrayList();
    private Context ctx;

    public MessageAdapter(Context context,List<MessageBean> list){
        this.ctx = context;
        this.mList = list;
    }


    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{

        ImageView ic_type;
        TextView msg_time;
        TextView msg_content;
        public Myholder(View itemView) {
            super(itemView);
            ic_type = itemView.findViewById(R.id.message_type);
            msg_time = itemView.findViewById(R.id.message_time);
            msg_content = itemView.findViewById(R.id.message_content);
        }
    }
}
