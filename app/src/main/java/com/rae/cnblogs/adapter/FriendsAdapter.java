package com.rae.cnblogs.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.FriendsViewHolder;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

/**
 * Created by ChenRui on 2017/2/23 01:36.
 */
public class FriendsAdapter extends BaseItemAdapter<UserInfoBean, FriendsViewHolder> {

    @Override
    public FriendsViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new FriendsViewHolder(inflateView(parent, R.layout.item_friends));
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position, UserInfoBean m) {
        ImageLoader.getInstance().displayImage(m.getAvatar(), holder.avatarView, RaeImageLoader.headerOption());
        holder.nameView.setText(TextUtils.isEmpty(m.getRemarkName()) ? m.getDisplayName() : m.getRemarkName());
    }
}
