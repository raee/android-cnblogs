package com.rae.cnblogs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rae.cnblogs.fragment.BlogListFragment;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.swift.Rx;

import java.util.List;

/**
 * 博客分类
 * Created by ChenRui on 2017/2/25 0025 12:14.
 */
public class BlogListAdapter extends FragmentStatePagerAdapter {

    //    private final Map<String, WeakReference<BlogListFragment>> mFragments = new HashMap<>();
    private final List<CategoryBean> mDataList;

    public BlogListAdapter(FragmentManager fm, List<CategoryBean> dataList) {
        super(fm);
        mDataList = dataList;
    }

    @Override
    public Fragment getItem(int position) {
        CategoryBean m = mDataList.get(position);
        return BlogListFragment.newInstance(m, BlogType.BLOG); // TODO:调优
//        String key = m.getName();
//        if (mFragments.containsKey(key) && mFragments.get(key).get() != null) {
//            return mFragments.get(key).get();
//        } else {
//            BlogListFragment fragment = BlogListFragment.newInstance(m, BlogType.BLOG);
//            mFragments.put(key, new WeakReference<>(fragment));
//            return fragment;
//        }
    }

    @Override
    public int getCount() {
        return Rx.getCount(mDataList);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position).getName();
    }
}
