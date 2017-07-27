package com.rae.cnblogs.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.rae.cnblogs.R;

/**
 * 预览图布局
 * Created by ChenRui on 2017/7/27 0027 14:55.
 */
public class ImagePreviewLayout extends RelativeLayout implements ImageLoadingListener, ImageLoadingProgressListener {


    private ProgressBar mProgressBar;
    private TextView mProgressText;

    public ImagePreviewLayout(Context context) {
        super(context);
    }

    public ImagePreviewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagePreviewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mProgressText = (TextView) findViewById(R.id.tv_progress);
    }

    private void showLoading(int progress) {
        mProgressText.setText(progress + "%");
        if (mProgressBar.getVisibility() != View.VISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        if (mProgressText.getVisibility() != View.VISIBLE) {
            mProgressText.setVisibility(View.VISIBLE);
        }
    }

    private void dismissLoading() {
        mProgressBar.setVisibility(View.GONE);
        mProgressText.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        showLoading(0);
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        dismissLoading();
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        dismissLoading();
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        dismissLoading();
    }

    @Override
    public void onProgressUpdate(String imageUri, View view, int current, int total) {
        float value = ((current * 0.1f) / (total * 0.1f)) * 100.0f;
        showLoading((int) value);
    }

}
