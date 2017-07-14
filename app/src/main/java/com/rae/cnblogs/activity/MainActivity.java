package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
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
import com.rae.cnblogs.sdk.BuildConfig;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
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

        // 模拟登录
        if(BuildConfig.DEBUG) {
            UserInfoBean userInfo = new UserInfoBean();
            userInfo.setAvatar("http://pic.cnblogs.com/avatar/446312/20170124105915.png");
            userInfo.setBlogApp("chenrui7");
            userInfo.setDisplayName("RAE");
            userInfo.setUserId("fdeed5f3-11fb-e111-aa3f-842b2b196315");
            CookieManager.getInstance().setCookie("http://www.cnblogs.com", ".CNBlogsCookie=80FDCAB0EBBE7772388EACB741A3FB57CA6D859BC6EF577A885F4ECF0B23631860BF05414E174E9CE32A6E0DC54E899B5650CD080777D5EC7B20247D094488A04EB45327B679DAD0FA7CC591CCDD39B603D017C0; .Cnblogs.AspNetCore.Cookies=CfDJ8PhlBN8IFxtHhqIV3s0LCDlTnIsf_KkYB04opjBUp_qeRD4_oKqtnDvr1zdsQ07TA8HmfUCuI7TWBwU6U_XPxHECcqUhtHpq9ecBa3wQ3MzTyr7fxF8Y6VKEum-uCZ3y0umuwXEWfxujWtgyHmJpSd7TXAsa6YxQEcT-JtW32yjgEDFbxr3dQg1fuiPA3GIa8sJl1xS9prbQi5NEFDd2YK_3cqmpnhSvsFQqCGzCc12o6XfrgSaOSCnXu3XqF_c7zcp4Xm93gmUZiirCe1EKZqr7ZKcJr_A2a6G89TcXl9jIoaN2JpXOk1WX8oLcI8jBqw; CONTAINERID=b73f4cdef6c2af5e595f530502ac2814eeafc0b0e474fd47e4b5f3fce8a7a65c");
            UserProvider.getInstance().setLoginUserInfo(userInfo);
        }

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
