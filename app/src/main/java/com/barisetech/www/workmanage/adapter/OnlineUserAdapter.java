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
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.List;
import java.util.Map;

public class OnlineUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mList;
    private Context context;
    private Map<String, Integer> countMap;
    private ItemCallBack callBack;


    public OnlineUserAdapter(Context context, List<String> list){
        this.context = context;
        this.mList = list;
    }

    public void OnClick(ItemCallBack callBack){
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_online_user, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Holder viewholder = (Holder) holder;
        String name = mList.get(position);
        viewholder.tv_name.setText(name);

        viewholder.tv_num.setVisibility(View.GONE);
        if (countMap != null && countMap.size() > 0) {
            if (countMap.containsKey(name)) {
                viewholder.tv_num.setText(String.valueOf(countMap.get(name)));
                viewholder.tv_num.setVisibility(View.VISIBLE);
            }
        }

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

    public void setCountMap(Map<String, Integer> countMap) {
        this.countMap = countMap;
    }

    class Holder extends RecyclerView.ViewHolder{


        TextView tv_name;
        TextView tv_num;
        ConstraintLayout layout;
        public Holder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            layout = itemView.findViewById(R.id.l_contact);
            tv_num = itemView.findViewById(R.id.auth_num);
        }
    }
}
