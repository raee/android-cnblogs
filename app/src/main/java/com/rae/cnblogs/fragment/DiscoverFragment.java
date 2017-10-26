package com.rae.cnblogs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.DesignTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.message.TabEvent;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.widget.RaeViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发现
 * Created by ChenRui on 2017/10/26 0026 23:29.
 */
public class DiscoverFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    DesignTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    RaeViewPager mViewPager;
    private DiscoverFragmentAdapter mAdapter;

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_discover;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new DiscoverFragmentAdapter(view.getContext(), getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        // 相互关联
        mViewPager.addOnPageChangeListener(new DesignTabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new DesignTabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.addOnTabSelectedListener(new DefaultOnTabSelectedListener());
        mTabLayout.getTabAt(0).select();
    }


    @OnClick(R.id.img_search)
    public void onSearchClick() {
        int position = mTabLayout.getSelectedTabPosition();
        if (position == 0)
            AppRoute.jumpToSearchNews(getContext());
        if (position == 1)
            AppRoute.jumpToSearchKb(getContext());
    }

    @Subscribe
    public void onTabEvent(TabEvent event) {

        // 执行刷新动作
        if (event.getPosition() == 2) {
            BlogTypeListFragment fragment = (BlogTypeListFragment) mAdapter.getItem(mViewPager.getCurrentItem());
            if (fragment != null && fragment.isAdded() && !fragment.isDetached()) {
                fragment.scrollToTop();
            }
        }
    }

    public static class DiscoverFragmentAdapter extends FragmentStatePagerAdapter {

        private final BlogTypeListFragment mNewFragment;
        private final BlogTypeListFragment mKBFragment;

        public DiscoverFragmentAdapter(Context context, FragmentManager fm) {
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
