package com.rae.cnblogs.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.R;

import butterknife.BindView;

/**
 * 博客评论View
 * Created by ChenRui on 2016/12/15 22:49.
 */
public class BlogCommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_blog_author)
    public TextView authorTitleView;

    @BindView(R.id.tv_blog_date)
    public TextView dateView;

    @BindView(R.id.tv_blog_summary)
    public TextView contentView;


    public BlogCommentViewHolder(View itemView) {
        super(itemView);
    }
}
