package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.image.RaeImageView;

import butterknife.BindView;

/**
 * Created by ChenRui on 2017/2/6 0006 17:01.
 */
public class ImagePreviewFragment extends BaseFragment {

    private String mUrl;


    @BindView(R.id.img_preview)
    RaeImageView mImageView;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageLoader.getInstance().displayImage(mUrl, mImageView, RaeImageLoader.fadeOptions(800).build(), mImageView);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString("url");
    }
}
