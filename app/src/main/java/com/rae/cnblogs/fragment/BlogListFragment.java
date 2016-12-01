package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogListPresenter;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.widget.AppLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by ChenRui on 2016/12/2 00:33.
 */
public class BlogListFragment extends BaseFragment implements IBlogListPresenter.IBlogListView {

    private String mId;
    private String mParentId;

    public static BlogListFragment newInstance(String id, String parentId) {

        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("parentId", parentId);
        BlogListFragment fragment = new BlogListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.content)
    AppLayout mAppLayout;

    @BindView(R.id.rec_blog_list)
    RecyclerView mRecyclerView;

    private IBlogListPresenter mBlogListPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogListPresenter = CnblogsPresenterFactory.getBlogListPresenter(getContext(), this);
        mId = getArguments().getString("id");
        mParentId = getArguments().getString("parentId");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLoadBlogList(List<Blog> data) {

    }

    @Override
    public void onLoadFailed(String msg) {

    }

    @Override
    public int getPage() {
        return 1;
    }

    @Override
    public String getCategoryId() {
        return mId;
    }

    @Override
    public String getParentId() {
        return mParentId;
    }
}
