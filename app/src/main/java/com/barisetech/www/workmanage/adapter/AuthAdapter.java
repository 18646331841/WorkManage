package com.barisetech.www.workmanage.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.AuthBean;
import com.barisetech.www.workmanage.bean.auth.AuthInfo;
import com.barisetech.www.workmanage.databinding.ItemAuthBinding;

import java.util.List;

public class AuthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "AuthAdapter";
    private List<AuthBean> mList;
    private OnClickButtonListener onClickButtonListener;

    public AuthAdapter(List<AuthBean> authBeans, OnClickButtonListener onClickButtonListener) {
        mList = authBeans;
        this.onClickButtonListener = onClickButtonListener;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAuthBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_auth,
                        parent, false);
//        binding.setCallback(mItemCallBack);
        return new Myholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Myholder) {
            Myholder myholder = (Myholder) holder;

            AuthBean authBean = mList.get(position);
            myholder.binding.setBean(authBean);

            myholder.binding.accept.setOnClickListener(view -> {
                onClickButtonListener.accept(authBean);
            });

            myholder.binding.refuse.setOnClickListener(view -> {
                onClickButtonListener.refuse(authBean);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Myholder extends RecyclerView.ViewHolder {

        final ItemAuthBinding binding;

        public Myholder(ItemAuthBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickButtonListener {
        void accept(AuthBean authBean);

        void refuse(AuthBean authBean);
    }
}
