package com.rae.cnblogs.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.BuildConfig;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.ThemeCompat;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;
import com.rae.cnblogs.dialog.impl.HintCardDialog;
import com.rae.cnblogs.dialog.impl.VersionUpdateDialog;
import com.rae.cnblogs.fragment.DiscoverFragment;
import com.rae.cnblogs.fragment.HomeFragment;
import com.rae.cnblogs.fragment.MineFragment;
import com.rae.cnblogs.fragment.SNSFragment;
import com.rae.cnblogs.message.TabEvent;
import com.rae.cnblogs.message.ThemeChangedEvent;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.bean.VersionInfo;
import com.rae.cnblogs.service.CnblogsService;
import com.rae.cnblogs.service.CnblogsServiceBinder;
import com.rae.cnblogs.service.job.JobEvent;
import com.rae.swift.app.RaeFragmentAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

@Route(path = AppRoute.PATH_APP_HOME)
public class MainActivity extends BasicActivity {

    @BindView(R.id.vp_main)
    ViewPager mViewPager;

    @BindView(R.id.tab_main)
    TabLayout mTabLayout;

    private RaeFragmentAdapter mFragmentAdapter;

    private long mBackKeyDownTime;

    private ServiceConnection mServiceConnection;
    private CnblogsServiceBinder mCnblogsServiceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mCnblogsServiceBinder = (CnblogsServiceBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        // 绑定服务
        bindService(new Intent(this, CnblogsService.class), mServiceConnection, BIND_AUTO_CREATE);
        mFragmentAdapter = new RaeFragmentAdapter(getSupportFragmentManager());


        // 初始化TAB
        addTab(R.string.tab_home, R.drawable.tab_home, HomeFragment.newInstance());
//        addTab(R.string.tab_sns, R.drawable.tab_news, BlogTypeListFragment.newInstance(1, news, BlogType.NEWS));
//        addTab(R.string.tab_discover, R.drawable.tab_library, BlogTypeListFragment.newInstance(2, kb, BlogType.KB));
        addTab(R.string.tab_sns, R.drawable.tab_news, SNSFragment.newInstance());
        addTab(R.string.tab_discover, R.drawable.tab_library, DiscoverFragment.newInstance());
        addTab(R.string.tab_mine, R.drawable.tab_mine, MineFragment.newInstance());

        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mFragmentAdapter);

        // 联动
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 统计分类点击
                int position = tab.getPosition();
                CharSequence title = mFragmentAdapter.getPageTitle(position);
                AppMobclickAgent.onCategoryEvent(getContext(), title.toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                EventBus.getDefault().post(new TabEvent(tab.getPosition()));
            }
        });

        // 统计打开时间
        AppMobclickAgent.onAppOpenEvent(this);

        // 检查更新
        RxObservable.create(CnblogsApiFactory
                .getInstance(getContext())
                .getRaeServerApi()
                .versionInfo(getVersionCode(), getVersionName(), getChannel(), BuildConfig.BUILD_TYPE), "MainActivity")
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


        // 请求权限
        requestPermissions();

        autoSelectedTab();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        autoSelectedTab();
    }

    /**
     * 自动跳转标签
     */
    private void autoSelectedTab() {
        int tab = getIntent().getIntExtra("tab", -1);
        if (tab >= 0 && mTabLayout != null) {
            mViewPager.setCurrentItem(tab);
        }
    }


    /**
     * 申请权限
     */
    private void requestPermissions() {
        // 检查权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            final HintCardDialog dialog = new HintCardDialog(this);
            dialog.setTitle(getString(R.string.title_request_permissions));
            dialog.setMessage(getString(R.string.permission_request_message));
            dialog.setEnSureText(getString(R.string.allow));
            dialog.showCloseButton();
            dialog.setOnEnSureListener(new IAppDialogClickListener() {
                @Override
                public void onClick(IAppDialog dialog, int buttonType) {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
            });

            dialog.show();
        }
    }

    private void addTab(int resId, int iconId, Fragment fragment) {
        TabLayout.Tab tab = mTabLayout.newTab();
        View tabView = getLayoutInflater().inflate(R.layout.tab_view, null);
        TextView v = (TextView) tabView.findViewById(R.id.tv_tab_view);
        ImageView iconView = (ImageView) tabView.findViewById(R.id.img_tab_icon);
        v.setText(resId);
        iconView.setImageResource(iconId);
//        v.setCompoundDrawablesWithIntrinsicBounds(0, iconId, 0, 0);
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
        EventBus.getDefault().unregister(this);
        // 停止服务
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
            mServiceConnection = null;
            stopService(new Intent(this, CnblogsService.class));
        }
        RxObservable.disposeAll(); // 释放所有请求
        RaeImageLoader.clearMemoryCache(getApplicationContext()); // 清除图片内存
        config().setMainExitTimeMillis(System.currentTimeMillis());
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 授权返回
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // 用户拒绝授权
            HintCardDialog dialog = new HintCardDialog(this);
            dialog.setTitle(getString(R.string.title_request_permissions));
            dialog.setMessage(getString(R.string.permission_tips_message));
            dialog.setEnSureText(getString(R.string.permission_granted));
            dialog.setOnEnSureListener(new IAppDialogClickListener() {
                @Override
                public void onClick(IAppDialog dialog, int buttonType) {
                    dialog.dismiss();
                    requestPermissions();
                }
            });
            dialog.show();
        }
    }

    @Subscribe
    public void onEvent(ThemeChangedEvent event) {
        ThemeCompat.refreshStatusColor(this, true); // 深色状态栏
    }

    @Subscribe
    public void onEvent(JobEvent event) {
        if (mCnblogsServiceBinder == null) return;
        mCnblogsServiceBinder.getJobScheduler().start(event.getAction());
    }


}
