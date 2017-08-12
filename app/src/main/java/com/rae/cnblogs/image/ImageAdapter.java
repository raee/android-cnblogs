package com.rae.cnblogs.image;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.R;
import com.rae.cnblogs.widget.ImagePreviewLayout;
import com.rae.swift.Rx;

import java.util.List;

/**
 * 图片适配器
 * Created by ChenRui on 2017/7/26 0026 22:28.
 */
public class ImageAdapter extends PagerAdapter implements View.OnClickListener {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<String> mUrls;
    private View.OnClickListener mOnItemClickListener;

    public ImageAdapter(Context context, List<String> urls) {
        mUrls = urls;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return Rx.getCount(mUrls);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ImagePreviewLayout view = (ImagePreviewLayout) mLayoutInflater.inflate(R.layout.item_image_preview, (ViewGroup) container, false);
        String url = mUrls.get(position);
        view.display(url);
        view.setOnClickListener(this);

        ((ViewGroup) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewGroup) container).removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener == null)
            ((Activity) mContext).finish();
        else
            mOnItemClickListener.onClick(v);
    }

    public String getItem(int item) {
        return mUrls.get(item % mUrls.size());
    }
}
