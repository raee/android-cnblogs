package com.rae.cnblogs.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.rae.cnblogs.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预览图模型
 * Created by ChenRui on 2017/2/6 0006 15:51.
 */
public class ImagePreviewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_preview)
    public ImageView mImageView;

    public ImagePreviewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
