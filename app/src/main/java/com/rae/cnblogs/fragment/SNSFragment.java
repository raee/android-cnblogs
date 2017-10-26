package com.rae.cnblogs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.DesignTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.widget.RaeViewPager;
import com.rae.swift.session.SessionManager;

import butterknife.BindView;

/**
 * 朋友圈（闪存）
 * Created by ChenRui on 2017/10/26 0026 23:31.
 */
public class SNSFragment extends BaseFragment {

    public static SNSFragment newInstance() {
        return new SNSFragment();
    }

    @BindView(R.id.tab_layout)
    DesignTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    RaeViewPager mViewPager;
    private SNSFragmentAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_sns;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SNSFragmentAdapter(view.getContext(), getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        // 相互关联
        mViewPager.addOnPageChangeListener(new DesignTabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new DesignTabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.addOnTabSelectedListener(new DefaultOnTabSelectedListener());
        mTabLayout.getTabAt(SessionManager.getDefault().isLogin() ? 0 : 1).select();
    }

    public static class SNSFragmentAdapter extends FragmentStatePagerAdapter {

        private final BlogTypeListFragment mNewFragment;
        private final BlogTypeListFragment mKBFragment;

        public SNSFragmentAdapter(Context context, FragmentManager fm) {
            super(fm);
            CategoryBean kb = new CategoryBean();
            kb.setType("kb");
            kb.setName(context.getString(R.string.tab_library));

            CategoryBean news = new CategoryBean();
            news.setType("news");
            news.setName(context.getString(R.string.tab_news));

            mNewFragment = BlogTypeListFragment.newInstance(30, news, BlogType.NEWS);
            mKBFragment = BlogTypeListFragment.newInstance(31, kb, BlogType.KB);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return mNewFragment;
            else
                return mKBFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    class DefaultOnTabSelectedListener implements DesignTabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(DesignTabLayout.Tab tab) {
            int count = mTabLayout.getTabCount();
            for (int i = 0; i < count; i++) {
                DesignTabLayout.Tab tabAt = mTabLayout.getTabAt(i);
                if (tabAt == null) continue;
                tabAt.setTextStyle(tab == tabAt ? 1 : 0);
            }
        }

        @Override
        public void onTabUnselected(DesignTabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(DesignTabLayout.Tab tab) {
            onTabSelected(tab);
        }
    }
}
