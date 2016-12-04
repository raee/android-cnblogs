package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IHomePresenter;
import com.rae.cnblogs.sdk.bean.Category;
import com.rae.core.fm.RaeFragmentAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 首页
 * Created by ChenRui on 2016/12/1 22:34.
 */
public class HomeFragment extends BaseFragment implements IHomePresenter.IHomeView {

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;

    @BindView(R.id.tab_category)
    TabLayout mTabLayout;

    @BindView(R.id.vp_blog_list)
    ViewPager mViewPager;

    private IHomePresenter mHomePresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHomePresenter = CnblogsPresenterFactory.getHomePresenter(getContext(), this);
        mHomePresenter.start();
    }

    @Override
    public void onLoadCategory(List<Category> data) {
        RaeFragmentAdapter adapter = new RaeFragmentAdapter(getChildFragmentManager());

        for (Category category : data) {
            adapter.add(category.getName(), BlogListFragment.newInstance(category));
        }

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onLoadFailed(String msg) {

    }
}
