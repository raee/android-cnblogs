package com.rae.cnblogs.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rae.cnblogs.GlideApp;
import com.rae.cnblogs.R;

/**
 * 预览图布局
 * Created by ChenRui on 2017/7/27 0027 14:55.
 */
public class ImagePreviewLayout extends RelativeLayout implements View.OnClickListener {


    private ProgressBar mProgressBar;
//    private TextView mProgressText;
    private String mUrl;
    private ImageView mImageView;
    private OnClickListener mClickListener;

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
//        mProgressText = (TextView) findViewById(R.id.tv_progress);
        mImageView = (ImageView) findViewById(R.id.img_preview);
        mImageView.setOnClickListener(this);

    }

    private void showLoading(int progress) {
//        mProgressText.setText(String.format(Locale.getDefault(), "%d%", progress));
        if (mProgressBar.getVisibility() != View.VISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
//        if (mProgressText.getVisibility() != View.VISIBLE) {
//            mProgressText.setVisibility(View.VISIBLE);
//        }
    }

    private void dismissLoading() {
        mProgressBar.setVisibility(View.GONE);
//        mProgressText.setVisibility(View.GONE);
    }

    public void display(String url) {
        mUrl = url;
        // 显示
        GlideApp.with(this)
                .load(mUrl)
                .error(R.drawable.picture_viewer_no_pic_icon)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        mImageView.setScaleType(ImageView.ScaleType.CENTER);
                        dismissLoading();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        dismissLoading();
                        return false;
                    }
                })
                .dontAnimate()
                .into(mImageView);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        mClickListener = l;
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.onClick(v);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        this.mImageView.setOnClickListener(null);
        super.onDetachedFromWindow();
    }

    //    @Override
//    public void onLoadingStarted(String imageUri, View view) {
//        showLoading(0);
//    }
//
//    @Override
//    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//        dismissLoading();
//    }
//
//    @Override
//    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//        dismissLoading();
//    }
//
//    @Override
//    public void onLoadingCancelled(String imageUri, View view) {
//        dismissLoading();
//    }
//
//    @Override
//    public void onProgressUpdate(String imageUri, View view, int current, int total) {
//        float value = ((current * 0.1f) / (total * 0.1f)) * 100.0f;
//        showLoading((int) value);
//    }

}
