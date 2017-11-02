package com.rae.cnblogs.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.MomentHolder;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.swift.Rx;

import java.lang.ref.WeakReference;

/**
 * 闪存
 * Created by ChenRui on 2017/10/27 0027 10:49.
 */
public class MomentAdapter extends BaseItemAdapter<MomentBean, MomentHolder> implements View.OnClickListener {
    @Override
    public void onClick(View v) {

    }

    public interface OnBloggerClickListener {
        void onBloggerClick(String blogApp);
    }

    private OnBloggerClickListener mOnBloggerClickListener;

    public void setOnBloggerClickListener(OnBloggerClickListener onBloggerClickListener) {
        mOnBloggerClickListener = onBloggerClickListener;
    }

    @Override
    public MomentHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new MomentHolder(inflateView(parent, R.layout.item_moment_list));
    }

    @Override
    public void onBindViewHolder(MomentHolder holder, int position, MomentBean m) {
        holder.mRecyclerView.setVisibility(Rx.isEmpty(m.getImageList()) ? View.GONE : View.VISIBLE);

        int imageCount = Rx.getCount(m.getImageList());
        if (imageCount == 1) {
            holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        } else {
            int spanCount = imageCount == 4 || imageCount == 2 ? 2 : 3;
            holder.mRecyclerView.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), spanCount));
        }

        holder.mRecyclerView.setAdapter(new MomentImageAdapter(m.getImageList()));


        View.OnClickListener onClickListener = TextUtils.isEmpty(m.getBlogApp()) ? null : new ItemBloggerClickListener(m.getBlogApp(), mOnBloggerClickListener);
        holder.authorView.setOnClickListener(onClickListener);
        holder.avatarView.setOnClickListener(onClickListener);
        holder.dateView.setOnClickListener(onClickListener);

        RaeImageLoader.displayHeaderImage(m.getAvatar(), holder.avatarView);
        holder.authorView.setText(m.getAuthorName());
        holder.dateView.setText(m.getPostTime());
        holder.summaryView.setText(m.getContent());
        holder.commentView.setText(m.getCommentCount());
    }


    private static class ItemBloggerClickListener implements View.OnClickListener {
        private String blogApp;
        private WeakReference<OnBloggerClickListener> mOnBloggerClickListenerWeakReference;

        public ItemBloggerClickListener(String blogApp, OnBloggerClickListener onBloggerClickListener) {
            this.blogApp = blogApp;
            mOnBloggerClickListenerWeakReference = new WeakReference<OnBloggerClickListener>(onBloggerClickListener);
        }

        @Override
        public void onClick(View v) {
            if (mOnBloggerClickListenerWeakReference.get() != null) {
                mOnBloggerClickListenerWeakReference.get().onBloggerClick(blogApp);
            }
        }
    }
}
