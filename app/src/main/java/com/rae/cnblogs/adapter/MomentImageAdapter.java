package com.rae.cnblogs.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.GlideApp;
import com.rae.cnblogs.R;
import com.rae.cnblogs.model.MomentImageHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片
 * Created by ChenRui on 2017/11/1 0001 0:50.
 */
public class MomentImageAdapter extends BaseItemAdapter<String, MomentImageHolder> implements View.OnClickListener {

    public MomentImageAdapter(List<String> dataList) {
        super(dataList);
    }

    @Override
    public MomentImageHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
//        if (getItemCount() == 1) {
//            return new MomentImageHolder(inflateView(parent, R.layout.item_moment_image_single));
//        }
        return new MomentImageHolder(inflateView(parent, R.layout.item_moment_image));
    }

    @Override
    public void onBindViewHolder(final MomentImageHolder holder, int position, String m) {
        holder.itemView.setOnClickListener(this);
        holder.itemView.setContentDescription(m);
        GlideApp.with(holder.getImageView().getContext())
                .load(m)
                .placeholder(R.color.background_divider)
                .into(holder.getImageView());
    }

    @Override
    public void onClick(View v) {
        AppRoute.jumpToImagePreview(v.getContext(), (ArrayList<String>) mDataList, mDataList.indexOf(v.getContentDescription().toString()));
    }
}
