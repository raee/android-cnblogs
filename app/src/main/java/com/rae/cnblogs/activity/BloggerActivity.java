package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.BlogListFragment;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.core.fm.RaeFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * blogger info
 * Created by ChenRui on 2017/2/9 0009 10:02.
 */
public class BloggerActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;

    @BindView(R.id.vp_blogger)
    ViewPager mViewPager;

    @BindView(R.id.tab_category)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fm_blogger_info);
        ButterKnife.bind(this);
        showHomeAsUp(mToolbar);

        RaeFragmentAdapter adapter = new RaeFragmentAdapter(getSupportFragmentManager());

        CategoryBean category = new CategoryBean();
        category.setCategoryId("webbest"); // 这里设置blogApp

        adapter.add(getString(R.string.feed), BlogListFragment.newInstance(category, BlogType.BLOGGER));
        adapter.add(getString(R.string.blog), BlogListFragment.newInstance(category, BlogType.BLOGGER));

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getHomeAsUpIndicator() {
        return R.drawable.ic_back_white;
    }
}
