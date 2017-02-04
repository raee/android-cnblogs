package com.rae.cnblogs.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 菜单对话框
 * Created by ChenRui on 2017/2/4 0004 17:20.
 */
public class MenuDialogViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_menu_title)
    public TextView titleView;

    public MenuDialogViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
