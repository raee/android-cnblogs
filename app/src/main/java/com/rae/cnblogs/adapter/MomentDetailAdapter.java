package com.rae.cnblogs.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.MomentCommentHolder;
import com.rae.cnblogs.model.MomentHolder;
import com.rae.cnblogs.model.SimpleViewHolder;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.swift.Rx;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 闪存详情
 * Created by ChenRui on 2017/11/2 0002 15:54.
 */
public class MomentDetailAdapter extends BaseItemAdapter<MomentCommentBean, SimpleViewHolder> {

    private static final int VIEW_TYPE_DETAIL = 10;
    private final MomentBean mMomentBean;
    private boolean mIsEmpty;
    private String mEmptyMessage;
    private int mDetailLayoutHeight;

    // 显示类型
    private int mViewTypeCount = 1;
    private WeakReference<ViewGroup> mViewParent;

    public MomentDetailAdapter(MomentBean momentBean) {
        mMomentBean = momentBean;
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();

        // 没有评论或者初始化的时候
        if (count <= 0) {
            mViewTypeCount = 2;
        } else {
            mViewTypeCount = 1;
        }

        return count + mViewTypeCount;
    }

    @Override
    public MomentCommentBean getDataItem(int position) {
        return super.getDataItem(Math.max(0, position - mViewTypeCount));
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPE_DETAIL;
        } else if (position == 1 && Rx.getCount(mDataList) <= 0) {
            return VIEW_TYPE_EMPTY;
        }

        return super.getItemViewType(position);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {

        if (mViewParent == null || mViewParent.get() == null) {
            mViewParent = new WeakReference<>(parent);
        }
        switch (viewType) {
            // 空视图
            case VIEW_TYPE_EMPTY:
                return new SimpleViewHolder(inflateView(parent, R.layout.item_comment_placeholder));
            // 详情
            case VIEW_TYPE_DETAIL:
                return new MomentHolder(inflateView(parent, R.layout.item_moment_detail_info));
            default:
                return new MomentCommentHolder(inflateView(parent, R.layout.item_moment_comment));
        }

    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position, MomentCommentBean m) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            // 空视图
            case VIEW_TYPE_EMPTY:
                onBindEmptyViewHolder((PlaceholderView) holder.itemView);
                return;
            // 详情
            case VIEW_TYPE_DETAIL:
                onBindDetailInfoViewHolder((MomentHolder) holder, mMomentBean);
                mDetailLayoutHeight = holder.itemView.getMeasuredHeight();
                return;
            default:
                onBindCommentViewHolder((MomentCommentHolder) holder, m);
                return;
        }

    }

    /**
     * 评论
     */
    private void onBindCommentViewHolder(MomentCommentHolder holder, MomentCommentBean m) {
        holder.authorView.setText(m.getAuthorName());
        holder.dateView.setText(m.getPostTime());
        holder.summaryView.setText(m.getContent());
    }

    /**
     * 详情
     */
    private void onBindDetailInfoViewHolder(MomentHolder holder, MomentBean m) {
        holder.mRecyclerView.setVisibility(Rx.isEmpty(m.getImageList()) ? View.GONE : View.VISIBLE);

        int imageCount = Rx.getCount(m.getImageList());
        if (imageCount == 1) {
            holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        } else {
            int spanCount = imageCount == 4 || imageCount == 2 ? 2 : 3;
            holder.mRecyclerView.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), spanCount));
        }

        holder.mRecyclerView.setAdapter(new MomentImageAdapter(m.getImageList()));
        RaeImageLoader.displayHeaderImage(m.getAvatar(), holder.avatarView);
        holder.authorView.setText(m.getAuthorName());
        holder.dateView.setText(m.getPostTime());
        holder.summaryView.setText(m.getContent());
        holder.commentView.setText(m.getCommentCount());
    }

    /**
     * 空视图
     */
    private void onBindEmptyViewHolder(final PlaceholderView view) {
        // 重新计算高度
        if (mViewParent != null && mViewParent.get() != null) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup parent = mViewParent.get();
                    int height = parent.getMeasuredHeight();
                    int[] location = new int[2];
                    view.getLocationInWindow(location);
                    view.getLayoutParams().height = height - location[1];
                }
            });
        }

        // 有评论
        if (getItemCount() > mViewTypeCount) {
            view.dismiss();
        } else if (mIsEmpty) {
            view.empty(mEmptyMessage);
        } else {
            view.loading();
        }

    }

    @Override
    public void invalidate(List<MomentCommentBean> data) {
        mIsEmpty = false;
        super.invalidate(data);
    }

    public void empty(String message) {
        mIsEmpty = true;
        mEmptyMessage = message;
        clear();
        notifyDataSetChanged();
    }
}
