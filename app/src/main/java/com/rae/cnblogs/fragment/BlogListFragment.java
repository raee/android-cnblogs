package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogListItemAdapter;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogListPresenter;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.widget.AppLayout;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

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
    XRecyclerView mRecyclerView;

    private IBlogListPresenter mBlogListPresenter;
    private BlogListItemAdapter mItemAdapter;

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
        mItemAdapter = new BlogListItemAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        mBlogListPresenter.start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setAdapter(mItemAdapter);
        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mBlogListPresenter.start();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
            }
        });
    }

    @Override
    public void onLoadBlogList(List<Blog> data) {
        mAppLayout.refreshComplete();
        mItemAdapter.invalidate(data);
        mItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFailed(String msg) {
        mAppLayout.refreshComplete();
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
