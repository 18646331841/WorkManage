package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.alarmanalysis.AlarmAnalysis;
import com.barisetech.www.workmanage.databinding.ItemAnalysisBinding;

import java.util.List;

public class AnalysisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<AlarmAnalysis> mList;
    private Context ctx;
    private ItemCallBack itemCallBack;

    public AnalysisAdapter(List<AlarmAnalysis> list, Context context, @NonNull ItemCallBack itemCallBack) {
        this.mList = list;
        this.ctx = context;
        this.itemCallBack = itemCallBack;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAnalysisBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout
                .item_analysis, parent, false);
        binding.setCallback(itemCallBack);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analysis, parent, false);
//
//        return new MyHolder(view);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            AlarmAnalysis alarmAnalysis = mList.get(position);

            myHolder.binding.setAnalysisinfo(alarmAnalysis);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

//    class MyHolder extends RecyclerView.ViewHolder{
//
//        public MyHolder(View itemView) {
//            super(itemView);
//
//        }
//    }
    class MyHolder extends RecyclerView.ViewHolder{
        final ItemAnalysisBinding binding;
        public MyHolder(ItemAnalysisBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
