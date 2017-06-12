package com.rae.cnblogs.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.ImagePreviewHolder;
import com.rae.swift.Rx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 预览图适配器
 * Created by ChenRui on 2017/2/6 0006 15:51.
 */
public class ImagePreviewAdapter extends PagerAdapter implements ImageLoadingListener {

    private final List<String> mDataList = new ArrayList<>();


    @Override
    public int getCount() {
        return Rx.getCount(mDataList);
    }

    public void addItem(String url) {
        mDataList.add(url);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewGroup) container).removeView((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(View container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image_preview, (ViewGroup) container, false);
        ImagePreviewHolder holder = new ImagePreviewHolder(view);
        ((ViewGroup) container).addView(view);

        ImageLoader.getInstance().displayImage(mDataList.get(position), holder.mImageView, RaeImageLoader.fadeOptions(300).build(), this);
        return view;

    }


    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
        // 获取图片类型，获取本地保存的路径
        try {
            File file = ImageLoader.getInstance().getDiskCache().get(imageUri);
            if (file == null) return;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            BitmapFactory.decodeStream(new FileInputStream(file), null, opts);

            if (!TextUtils.isEmpty(opts.outMimeType) && opts.outMimeType.contains("gif")) {

            }

            Log.w("rae", "文件路径：" + file + ";类型：" + opts.outMimeType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
