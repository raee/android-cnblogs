package com.rae.cnblogs.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 粉丝和关注
 * Created by ChenRui on 2017/2/23 01:36.
 */
public class FriendsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_user_name)
    public TextView nameView;

    @BindView(R.id.img_user_avatar)
    public ImageView avatarView;

    public FriendsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
