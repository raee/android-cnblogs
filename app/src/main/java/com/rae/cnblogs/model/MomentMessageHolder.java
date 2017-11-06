package com.rae.cnblogs.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 闪存
 * Created by ChenRui on 2017/10/27 0027 10:50.
 */
public class MomentMessageHolder extends SimpleViewHolder {

    @BindView(R.id.img_blog_avatar)
    public ImageView avatarView;

    @BindView(R.id.tv_blog_author)
    public TextView authorView;

    @BindView(R.id.tv_blog_summary)
    public TextView summaryView;

    @BindView(R.id.tv_blog_date)
    public TextView dateView;
    @BindView(R.id.tv_source)
    public TextView sourceView;


    public MomentMessageHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
