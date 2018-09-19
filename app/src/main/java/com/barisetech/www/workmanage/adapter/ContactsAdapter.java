package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.Holder> {

    private List<String> mList;
    private Context context;

    private ItemCallBack callBack;

    public ContactsAdapter(Context context,List<String> list){
        this.context = context;
        this.mList = list;
    }

    public void OnClick(ItemCallBack callBack){
        this.callBack = callBack;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Holder viewholder = holder;
        viewholder.layout.setOnClickListener(view -> {
            if (callBack!=null){
                callBack.onClick(mList.get(position));
            }

        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder{


        TextView tv_name;
        ConstraintLayout layout;
        public Holder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            layout = itemView.findViewById(R.id.l_item);
        }
    }
}
