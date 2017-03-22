package com.rae.cnblogs.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.R;

import butterknife.BindView;

/**
 * 动态
 * Created by ChenRui on 2017/3/16 15:58.
 */
public class FeedViewHolder extends BlogCommentViewHolder {

    @BindView(R.id.img_blog_action)
    public ImageView feedActionView;

    @BindView(R.id.tv_blog_action_title)
    public TextView feedActionTitleView;

    public FeedViewHolder(View itemView) {
        super(itemView);
    }
}
