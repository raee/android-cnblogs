package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rae.cnblogs.message.SearchEvent;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogListPresenter;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import org.greenrobot.eventbus.Subscribe;

/**
 * 博客/新闻/知识库 搜索结果
 * Created by ChenRui on 2017/8/29 0029 11:56.
 */
public class SearchBlogFragment extends BlogListFragment {

    private String mSearchText;

    public static SearchBlogFragment newInstance(BlogType type) {
        Bundle args = new Bundle();
        args.putString("type", type.getTypeName());
        SearchBlogFragment fragment = new SearchBlogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = new CategoryBean();
    }

    @Override
    protected IBlogListPresenter getBlogListPresenter() {
        return CnblogsPresenterFactory.getSearchPresenter(getContext(), mBlogType, this);
    }

    @Override
    protected void onLoadData() {
//        刚开始没有数据加载
        mPlaceholderView.dismiss();
    }


    @Subscribe
    public void onEvent(SearchEvent event) {
        mPlaceholderView.loading("正在搜索..");
        mSearchText = event.getSearchText();
        mBlogListPresenter.start();
    }

    @Override
    public CategoryBean getCategory() {
        mCategory.setName(mSearchText);
        return super.getCategory();
    }
}
