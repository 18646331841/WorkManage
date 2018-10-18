package com.barisetech.www.workmanage.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.worktask.TaskSiteBean;
import com.barisetech.www.workmanage.databinding.ItemPlanContactsListBinding;
import com.barisetech.www.workmanage.databinding.ItemPlanTaskListBinding;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;

import java.util.List;

public class PlanTaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TaskSiteBean> mList;
    private Context ctx;
    private ItemCallBack itemCallBack;
    private String curPerson;
    private String role;

    public PlanTaskListAdapter(List<TaskSiteBean> list, String person, Context context, @NonNull
            ItemCallBack itemCallBack) {
        this.mList = list;
        this.ctx = context;
        this.itemCallBack = itemCallBack;
        this.curPerson = person;
        role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlanTaskListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout
                .item_plan_task_list, parent, false);
        binding.setCallback(itemCallBack);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
//            int count = position % (mList.size() + 1);
//            boolean isTitle = count == 0;
            boolean isTitle = mList.get(position).SiteId == -1;
            if (isTitle) {
                myHolder.binding.planTaskItemTitle.setVisibility(View.VISIBLE);
                myHolder.binding.planTaskItemContent.setVisibility(View.GONE);
                StringBuilder sb = new StringBuilder();
//                int num = position / (mList.size() + 1);
//                sb.append("第").append(num + 1).append("次巡线");
//                sb.append("第").append(mList.get(position).Name).append("次巡线");
                myHolder.binding.planTaskItemNum.setText(mList.get(position).Name);
            } else {
                myHolder.binding.planTaskItemTitle.setVisibility(View.GONE);
                myHolder.binding.planTaskItemContent.setVisibility(View.VISIBLE);
//                TaskSiteBean taskSiteBean = mList.get(count - 1);
                TaskSiteBean taskSiteBean = mList.get(position);

                myHolder.binding.planTaskItemSite.setText(taskSiteBean.Name);
                myHolder.binding.planTaskItemTime.setText(TimeUtil.replaceTimeT(taskSiteBean.DateTime));
                if (taskSiteBean.SiteState != 1) {
                    myHolder.binding.planTaskItemException.setBackgroundResource(R.drawable.icon_arrow_right);
                } else {
                    myHolder.binding.planTaskItemException.setBackgroundColor(Color.TRANSPARENT);
                }

                myHolder.binding.planTaskItemPerson.setText(curPerson);
                String status = taskSiteBean.showStatus(role);
                myHolder.binding.planTaskItemStatus.setText(status);
                if (status.equals(BaseConstant.STATUS_TASK[0]) || status.equals(BaseConstant.STATUS_TASK[1])) {
                    myHolder.binding.planTaskItemStatus.setTextColor(ctx.getResources().getColor(R.color.filter_blue));
                } else {
                    myHolder.binding.planTaskItemStatus.setTextColor(ctx.getResources().getColor(R.color.message_item_gray));
                }
                myHolder.binding.planTaskItemStatus.setOnClickListener(view -> {
                    if (status.equals(BaseConstant.STATUS_TASK[0]) || status.equals(BaseConstant.STATUS_TASK[1])) {
                        itemCallBack.onClick(taskSiteBean);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
//        if (mList.size() > 0) {
//            if (curSum == 0) {
//                return mList.size() + 1;
//            }
//            int count = mList.size() * curSum + curSum;
//            return count;
//        }
        return mList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        final ItemPlanTaskListBinding binding;

        public MyHolder(ItemPlanTaskListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
