package com.rae.cnblogs.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rae.cnblogs.R;
import com.rae.cnblogs.widget.RaeTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ChenRui on 2017/7/16 0016 14:00.
 */
public class BookmarksViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_blog_title)
    public RaeTextView mBlogTitle;
    @BindView(R.id.tv_blog_summary)
    public RaeTextView mBlogSummary;
    @BindView(R.id.tv_blog_date)
    public RaeTextView mBlogDate;
    @BindView(R.id.tv_blog_delete)
    public RaeTextView mBlogDelete;

    public BookmarksViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
