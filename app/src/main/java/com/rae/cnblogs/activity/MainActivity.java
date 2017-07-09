package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.AppStatusBar;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.dialog.impl.VersionUpdateDialog;
import com.rae.cnblogs.fragment.BlogTypeListFragment;
import com.rae.cnblogs.fragment.HomeFragment;
import com.rae.cnblogs.fragment.MineFragment;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.sdk.bean.VersionInfo;
import com.rae.swift.app.RaeFragmentAdapter;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_main)
    ViewPager mViewPager;

    @BindView(R.id.tab_main)
    TabLayout mTabLayout;

    private RaeFragmentAdapter mFragmentAdapter;

    private long mBackKeyDownTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppStatusBar.setStatusbarToDark(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentAdapter = new RaeFragmentAdapter(getSupportFragmentManager());

        CategoryBean kb = new CategoryBean();
        kb.setType("kb");
        kb.setName(getString(R.string.tab_library));

        CategoryBean news = new CategoryBean();
        news.setType("news");
        news.setName(getString(R.string.tab_news));

        // 初始化TAB
        addTab(R.string.tab_home, R.drawable.tab_home, HomeFragment.newInstance());
        addTab(R.string.tab_news, R.drawable.tab_news, BlogTypeListFragment.newInstance(news, BlogType.NEWS));
        addTab(R.string.tab_library, R.drawable.tab_library, BlogTypeListFragment.newInstance(kb, BlogType.KB));
        addTab(R.string.tab_mine, R.drawable.tab_mine, MineFragment.newInstance());

        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mFragmentAdapter);

        // 联动
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                // 首页
                if (tab.getPosition() == 0) {
                    HomeFragment fragment = (HomeFragment) mFragmentAdapter.getItem(0);
                    fragment.onLogoClick();
                }

                // 新闻
                if (tab.getPosition() == 1 || tab.getPosition() == 2) {
                    BlogTypeListFragment fragment = (BlogTypeListFragment) mFragmentAdapter.getItem(tab.getPosition());
                    fragment.scrollToTop();
                }

            }
        });

        // 统计打开时间
        AppMobclickAgent.onAppOpenEvent(this);
        mViewPager.setCurrentItem(0);

        // 检查更新
        RxObservable.create(CnblogsApiFactory
                .getInstance(getContext())
                .getRaeServerApi()
                .versionInfo(getVersionCode()), "MainActivity")
                .subscribe(new ApiDefaultObserver<VersionInfo>() {
                    @Override
                    protected void onError(String message) {
                        // 不用处理
                        Log.e("rae", message);
                    }

                    @Override
                    protected void accept(VersionInfo versionInfo) {
                        VersionUpdateDialog dialog = new VersionUpdateDialog(getContext());
                        dialog.setVersionInfo(versionInfo);
                        dialog.show();
                    }
                });

    }

    private void addTab(int resId, int iconId, Fragment fragment) {
        TabLayout.Tab tab = mTabLayout.newTab();
        View tabView = getLayoutInflater().inflate(R.layout.tab_view, null);
        TextView v = (TextView) tabView.findViewById(R.id.tv_tab_view);
        v.setText(resId);
        v.setCompoundDrawablesWithIntrinsicBounds(0, iconId, 0, 0);
        tab.setCustomView(tabView);
        mTabLayout.addTab(tab);
        if (fragment != null)
            mFragmentAdapter.add(getString(resId), fragment);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mBackKeyDownTime) > 2000) {
            AppUI.toast(this, "再按一次退出");
            mBackKeyDownTime = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxObservable.dispose(); // 释放所有请求
    }
}
