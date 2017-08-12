package com.rae.cnblogs.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.FeedViewHolder;
import com.rae.cnblogs.sdk.bean.UserFeedBean;

/**
 * 动态 - 适配器
 * Created by ChenRui on 2017/3/16 16:40.
 */
public class FeedItemAdapter extends BaseItemAdapter<UserFeedBean, FeedViewHolder> {

    @Override
    public FeedViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new FeedViewHolder(inflateView(parent, R.layout.item_blogger_feed));
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position, UserFeedBean m) {
        holder.authorTitleView.setText(m.getAuthor());
        holder.dateView.setText(m.getFeedDate());
        holder.contentView.setText(m.getContent());
//        holder.feedActionView.setText(m.getAction());
        holder.feedActionTitleView.setText(String.format("#%s# %s", m.getAction(), m.getTitle()));
        holder.itemView.setTag(m);

        if (!TextUtils.isEmpty(m.getAvatar())) {
            RaeImageLoader.displayImage(m.getAvatar(), holder.avatarView);
        }
    }
}
