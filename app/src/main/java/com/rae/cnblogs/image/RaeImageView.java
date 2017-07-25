package com.rae.cnblogs.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


/**
 * 图片查看
 * Created by ChenRui on 2017/7/26 0026 0:35.
 */
public class RaeImageView extends PhotoView implements ImageLoadingListener {

    public RaeImageView(Context context) {
        super(context);
    }

    public RaeImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public RaeImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
