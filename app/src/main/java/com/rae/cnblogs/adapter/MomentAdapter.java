package com.rae.cnblogs.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.MomentHolder;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.swift.Rx;

/**
 * 闪存
 * Created by ChenRui on 2017/10/27 0027 10:49.
 */
public class MomentAdapter extends BaseItemAdapter<MomentBean, MomentHolder> {

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

        RaeImageLoader.displayHeaderImage(m.getAvatar(), holder.avatarView);
        holder.authorView.setText(m.getAuthorName());
        holder.dateView.setText(m.getPostTime());
        holder.summaryView.setText(m.getContent());
        holder.commentView.setText(m.getCommentCount());
    }
}
