package com.rae.cnblogs.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rae.cnblogs.R;
import com.rae.cnblogs.model.BlogCommentViewHolder;
import com.rae.cnblogs.sdk.bean.BlogComment;

/**
 * 评论列表ITEM
 * Created by ChenRui on 2016/12/15 22:48.
 */
public class BlogCommentItemAdapter extends BaseItemAdapter<BlogComment, BlogCommentViewHolder> {


    @Override
    public BlogCommentViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BlogCommentViewHolder(inflateView(parent, R.layout.item_blog_comment));
    }

    @Override
    public void onBindViewHolder(BlogCommentViewHolder holder, int position, BlogComment m) {
        holder.authorTitleView.setText(m.getAuthorName());
        holder.dateView.setText(m.getDate());
        holder.contentView.setText(m.getBody());
    }
}
