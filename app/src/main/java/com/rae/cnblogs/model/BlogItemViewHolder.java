package com.rae.cnblogs.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 博客列表ITEM
 * Created by ChenRui on 2016/12/2 0002 19:43.
 */
public class BlogItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ll_blog_author_layout)
    public View authorLayout;

    @BindView(R.id.img_blog_avatar)
    public ImageView avatarView;


    @BindView(R.id.tv_blog_author)
    public TextView authorView;

    @BindView(R.id.tv_blog_title)
    public TextView titleView;

    @BindView(R.id.tv_blog_summary)
    public TextView summaryView;

    @BindView(R.id.tv_blog_date)
    public TextView dateView;

    @BindView(R.id.tv_blog_view)
    public TextView readerView;

    @BindView(R.id.tv_blog_like)
    public TextView likeView;

    @BindView(R.id.tv_blog_comment)
    public TextView commentView;

    public BlogItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
