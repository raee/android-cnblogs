package com.rae.cnblogs.model;

import android.support.v7.widget.RecyclerView;
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
public class MomentHolder extends SimpleViewHolder {

//    @BindView(R.id.ll_blog_author_layout)
//    public View authorLayout;

    @BindView(R.id.img_blog_avatar)
    public ImageView avatarView;

    @BindView(R.id.tv_blog_author)
    public TextView authorView;

    @BindView(R.id.tv_blog_summary)
    public TextView summaryView;

    @BindView(R.id.tv_blog_date)
    public TextView dateView;

    @BindView(R.id.tv_blog_comment)
    public TextView commentView;

    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;


//    @BindView(R.id.img_blog_list_large_thumb)
//    public ImageView largeThumbView;
//
//    @BindView(R.id.layout_blog_list_thumb)
//    public View thumbLayout;
//
//    @BindView(R.id.img_blog_list_thumb_one)
//    public ImageView thumbOneView;
//
//    @BindView(R.id.img_blog_list_thumb_two)
//    public ImageView thumbTwoView;
//
//    @BindView(R.id.img_blog_list_thumb_three)
//    public ImageView thumbThreeView;

    public MomentHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
