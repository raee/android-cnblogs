package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.HomeFragment;
import com.rae.cnblogs.fragment.KBListFragment;
import com.rae.cnblogs.fragment.MineFragment;
import com.rae.cnblogs.sdk.bean.Category;
import com.rae.core.fm.RaeFragmentAdapter;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentAdapter = new RaeFragmentAdapter(getSupportFragmentManager());

        Category kb = new Category();
        kb.setType("kb");
        kb.setName(getString(R.string.tab_library));

        Category news = new Category();
        news.setType("news");
        news.setName(getString(R.string.tab_news));

        // 初始化TAB
        addTab(R.string.tab_home, R.drawable.tab_home, HomeFragment.newInstance());
        addTab(R.string.tab_news, R.drawable.tab_news, KBListFragment.newInstance(news));
        addTab(R.string.tab_library, R.drawable.tab_library, KBListFragment.newInstance(kb));
        addTab(R.string.tab_mine, R.drawable.tab_mine, MineFragment.newInstance());

        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mFragmentAdapter);

        // 联动
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

//        IAppDialog dialog = DialogProvider.create(this, DialogProvider.TYPE_HINT_CARD);
//        dialog.setMessage("我是提示信息");
//        dialog.setTitle("大大的标题");
//        dialog.setImage(0, "http://mobike.com/wp-content/uploads/2016/11/23.jpg");
//        dialog.setButtonText(IAppDialog.BUTTON_POSITIVE, "立即查看");
////        dialog.setButtonVisibility(IAppDialog.BUTTON_POSITIVE, View.GONE);
//        dialog.show();
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


}
