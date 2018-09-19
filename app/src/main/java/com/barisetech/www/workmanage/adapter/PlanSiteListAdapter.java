package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.ItemPlanContactsListBinding;
import com.barisetech.www.workmanage.databinding.ItemPlanSiteListBinding;

import java.util.List;

public class PlanSiteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<SiteBean> mList;
    private Context ctx;
    private ItemCallBack itemCallBack;

    public PlanSiteListAdapter(List<SiteBean> list, Context context, @NonNull ItemCallBack itemCallBack) {
        this.mList = list;
        this.ctx = context;
        this.itemCallBack = itemCallBack;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlanSiteListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout
                .item_plan_site_list, parent, false);
        binding.setCallback(itemCallBack);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            SiteBean siteBean = mList.get(position);
            myHolder.binding.planPublishThirdSite.setText(siteBean.Name);
            myHolder.binding.planPublishThirdSite.setOnCheckedChangeListener(((compoundButton, b) -> {
                if (b) {
                    itemCallBack.onClick(siteBean);
                }
            }));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        final ItemPlanSiteListBinding binding;
        public MyHolder(ItemPlanSiteListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
