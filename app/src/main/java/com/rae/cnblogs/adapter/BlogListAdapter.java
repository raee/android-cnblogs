package com.rae.cnblogs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

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

    private final FragmentManager mFragmentManager;
    private List<CategoryBean> mDataList;
    private SparseArray<Fragment> mFragmentTags = new SparseArray<>();

    public BlogListAdapter(FragmentManager fm, List<CategoryBean> dataList) {
        super(fm);
        mFragmentManager = fm;
        mDataList = dataList;
    }

    @Override
    public Fragment getItem(int position) {
        CategoryBean m = mDataList.get(position);
        Fragment fragment = BlogListFragment.newInstance(m, BlogType.BLOG);
        return fragment;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            Fragment f = (Fragment) obj;
            mFragmentTags.put(position, f);
        }
        return obj;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragmentTags.remove(position);
    }

    @Override
    public int getCount() {
        return Rx.getCount(mDataList);
    }

    public void updateDataSet(List<CategoryBean> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position).getName();
    }

    public BlogListFragment getFragment(int position) {
        return (BlogListFragment) mFragmentTags.get(position);
    }
}
