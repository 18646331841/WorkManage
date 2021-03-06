package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.workplan.PlanBean;
import com.barisetech.www.workmanage.databinding.ItemIncidentBinding;
import com.barisetech.www.workmanage.databinding.ItemPlanListBinding;

import java.util.List;

public class PlanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<PlanBean> mList;
    private Context ctx;
    private ItemCallBack itemCallBack;

    public PlanListAdapter(List<PlanBean> list, Context context, @NonNull ItemCallBack itemCallBack) {
        this.mList = list;
        this.ctx = context;
        this.itemCallBack = itemCallBack;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlanListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout
                .item_plan_list, parent, false);
        binding.setCallback(itemCallBack);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            PlanBean planBean = mList.get(position);

            myHolder.binding.setPlanbean(planBean);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        final ItemPlanListBinding binding;
        public MyHolder(ItemPlanListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
