package com.rae.cnblogs.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rae.cnblogs.R;

import butterknife.BindView;

/**
 * Created by ChenRui on 2017/2/6 0006 17:01.
 */
public class ImagePreviewFragment extends BaseFragment implements ImageLoadingListener {

    private String mUrl;


    @BindView(R.id.img_preview)
    ImageView mImageView;

    public static ImagePreviewFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_image_preview;
    }

    @Override
    protected void onCreateView(View view) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString("url");
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
