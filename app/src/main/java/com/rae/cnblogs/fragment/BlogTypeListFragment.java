package com.rae.cnblogs.fragment;

import android.os.Bundle;

import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 博客类型的列表
 * Created by ChenRui on 2017/1/18 23:49.
 */
public class BlogTypeListFragment extends BlogListFragment {

    public static BlogTypeListFragment newInstance(int position, CategoryBean category, BlogType type) {
        Bundle args = new Bundle();
        args.putParcelable("category", category);
        args.putString("type", type.getTypeName());
        args.putInt("position", position);
        BlogTypeListFragment fragment = new BlogTypeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void performTabEvent() {

    }
}
