package com.rae.cnblogs.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.widget.RaeGifView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;

/**
 * Created by ChenRui on 2017/2/6 0006 17:01.
 */
public class ImagePreviewFragment extends BaseFragment implements ImageLoadingListener {

    private String mUrl;


    @BindView(R.id.img_preview)
    ImageView mImageView;

    @BindView(R.id.gif_view_preview)
    RaeGifView mGifView;

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
        if (!loadGif(mUrl, mImageView)) {
            ImageLoader.getInstance().displayImage(mUrl, mImageView, RaeImageLoader.fadeOptions(300).build(), this);
        }
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
        loadGif(imageUri, view);
    }

    private boolean loadGif(String imageUri, View view) {
        // 获取图片类型，获取本地保存的路径
        try {
            File file = ImageLoader.getInstance().getDiskCache().get(imageUri);
            if (file == null) return false;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            BitmapFactory.decodeStream(new FileInputStream(file), null, opts);

            if (!TextUtils.isEmpty(opts.outMimeType) && opts.outMimeType.contains("gif")) {
                mGifView.setGifFromFile(file);
                view.setVisibility(View.GONE);
                ((ViewGroup) view.getParent()).removeView(view);
                return true;
            }

            Log.w("rae", "文件路径：" + file + ";类型：" + opts.outMimeType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
