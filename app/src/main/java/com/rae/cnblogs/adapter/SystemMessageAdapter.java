package com.rae.cnblogs.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.ThemeCompat;
import com.rae.cnblogs.model.SystemMessageHolder;
import com.rae.cnblogs.sdk.bean.SystemMessageBean;

/**
 * 系统消息
 * Created by ChenRui on 2017/9/5 0005 17:23.
 */
public class SystemMessageAdapter extends BaseItemAdapter<SystemMessageBean, SystemMessageHolder> {

    @Override
    public SystemMessageHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new SystemMessageHolder(inflateView(parent, R.layout.item_system_message));
    }

    @Override
    public void onBindViewHolder(SystemMessageHolder holder, int position, SystemMessageBean m) {
        holder.getTitleView().setText(m.getSummary());
        holder.getDateView().setText(m.getCreateTime());
        RaeImageLoader.displayImage(m.getThumbUrl(), holder.getThumbImageView());
        if (ThemeCompat.isNight()) {
            holder.getThumbImageView().setAlpha(0.4f);
        }
    }

}
