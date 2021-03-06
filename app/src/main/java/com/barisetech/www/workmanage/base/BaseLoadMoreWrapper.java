package com.barisetech.www.workmanage.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.utils.LogUtil;

/**
 * Created by LJH on 2018/8/26.
 */
public class BaseLoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Context
    private Context mContext;
    // Origin adapter
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    // General view
    private final int TYPE_ITEM = 1;
    // Footer view
    private final int TYPE_FOOTER = 2;
    // The current loading state, the default is loading complete.
    private int mLoadingState = 2;
    // Loading
    public final int LOADING = 1;
    // Loading done
    public final int LOADING_COMPLETE = 2;
    // Loading end
    public final int LOADING_END = 3;
    // Loading view
    private View mLoadingView;
    // Loading end view
    private View mLoadingEndView;
    // Loading complete view
    private View mLoadingCompleteView;
    // Loading view height
    private int mLoadingViewHeight;

    public BaseLoadMoreWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public int getItemViewType(int position) {
        // The last item is set to footer view.
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get context
        this.mContext = parent.getContext();

        // Create view by the view type.
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_footer, parent, false);
            return new LoadMoreViewHolder(view);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;

            // Set loading view height.
            loadMoreViewHolder.rlLoadingFooter.removeAllViews();
            if (mLoadingViewHeight > 0) {
                ViewGroup.LayoutParams params = loadMoreViewHolder.rlLoadingFooter.getLayoutParams();
                params.height = mLoadingViewHeight;
                loadMoreViewHolder.rlLoadingFooter.setLayoutParams(params);
            }

            // Display loading view.
            switch (mLoadingState) {
                case LOADING: // Loading
                    if (mLoadingView != null) {
                        // Custom loading view
                        loadMoreViewHolder.rlLoadingFooter.addView(mLoadingView);
                    } else {
                        // Default loading view
                        View loadingView = View.inflate(mContext, R.layout.layout_loading, null);
                        loadMoreViewHolder.rlLoadingFooter.addView(loadingView);
                    }
                    break;

                case LOADING_COMPLETE: // Loading done
                    if (mLoadingCompleteView != null) {
                        // Custom loading end view
                        loadMoreViewHolder.rlLoadingFooter.addView(mLoadingCompleteView);
                    } else {
                        // Default loading end view
                        View loadingEndView = View.inflate(mContext, R.layout.layout_loading_complete, null);
                        loadMoreViewHolder.rlLoadingFooter.addView(loadingEndView);
                    }
                    break;

                case LOADING_END: // Loading end
                    if (mLoadingEndView != null) {
                        // Custom loading end view
                        loadMoreViewHolder.rlLoadingFooter.addView(mLoadingEndView);
                    } else {
                        // Default loading end view 根据需求,改成和加载成功一样的样子
//                        View loadingEndView = View.inflate(mContext, R.layout.layout_loading_end, null);
                        View loadingEndView = View.inflate(mContext, R.layout.layout_loading_complete, null);
                        loadMoreViewHolder.rlLoadingFooter.addView(loadingEndView);
                    }
                    break;

                default:
                    break;
            }
        } else {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        // GridLayoutManager
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // If the current position is footer view, then the item occupy two cells,
                    // Normal item occupy a cell.
                    return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    public void setmLoadingCompleteView(View mLoadingCompleteView) {
        this.mLoadingCompleteView = mLoadingCompleteView;
    }

    /**
     * Loading holder
     */
    private class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlLoadingFooter;

        LoadMoreViewHolder(View itemView) {
            super(itemView);
            rlLoadingFooter = itemView.findViewById(R.id.rl_loading_footer);
        }
    }

    /**
     * Set the pull-up state.
     *
     * @param loadingState 0.Loading 1.Loading done 2.Loading end
     */
    public void setLoadState(int loadingState) {
        this.mLoadingState = loadingState;
        LogUtil.d("BaseLoadMore", "setLoadState = " + loadingState);
        notifyDataSetChanged();
    }

    /**
     * Set loading view.
     *
     * @param view Loading view
     */
    public void setLoadingView(View view) {
        this.mLoadingView = view;
    }

    /**
     * Set loading end view.
     *
     * @param view Loading end view
     */
    public void setLoadingEndView(View view) {
        this.mLoadingEndView = view;
    }

    /**
     * Set loading view height.
     *
     * @param height Height/px
     */
    public void setLoadingViewHeight(int height) {
        this.mLoadingViewHeight = height;
    }
}