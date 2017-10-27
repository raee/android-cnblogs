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
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.widget.RaeViewPager;
import com.rae.swift.session.SessionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
        mAdapter = new SNSFragmentAdapter(view.getContext(), getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        // 相互关联
        mViewPager.addOnPageChangeListener(new DesignTabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new DesignTabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.addOnTabSelectedListener(new DefaultOnTabSelectedListener());
        mTabLayout.getTabAt(SessionManager.getDefault().isLogin() ? 0 : 1).select();
    }

    @OnClick(R.id.tv_post)
    public void onPostClick() {
        AppRoute.jumpToPostMoment(getActivity());
    }

    public static class SNSFragmentAdapter extends FragmentStatePagerAdapter {

        private final List<MomentFragment> mFragments = new ArrayList<>();

        public SNSFragmentAdapter(Context context, FragmentManager fm) {
            super(fm);
            mFragments.add(MomentFragment.newInstance(IMomentApi.MOMENT_TYPE_FOLLOWING));
            mFragments.add(MomentFragment.newInstance(IMomentApi.MOMENT_TYPE_ALL));
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Subscribe
    public void onTabEvent(TabEvent event) {
        if (event.getPosition() == 1) {
            performTabEvent();
        }
    }

    private void performTabEvent() {
        int position = mViewPager.getCurrentItem();
        MomentFragment fragment = (MomentFragment) mAdapter.getItem(position);
        fragment.scrollToTop();
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
            performTabEvent();
        }
    }
}
