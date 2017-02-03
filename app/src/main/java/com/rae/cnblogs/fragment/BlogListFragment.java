package com.rae.cnblogs.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogListItemAdapter;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogListPresenter;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.Category;
import com.rae.cnblogs.sdk.service.BlogService;
import com.rae.cnblogs.sdk.service.BlogServiceBinder;
import com.rae.cnblogs.sdk.service.task.OnTaskFinishListener;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 博客列表
 * Created by ChenRui on 2016/12/2 00:33.
 */
public class BlogListFragment extends BaseFragment implements IBlogListPresenter.IBlogListView {

    private BlogServiceBinder mBinder;

    public static BlogListFragment newInstance(Category category) {

        Bundle args = new Bundle();
        args.putParcelable("category", category);
        BlogListFragment fragment = new BlogListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.content)
    AppLayout mAppLayout;

    @BindView(R.id.rec_blog_list)
    RaeRecyclerView mRecyclerView;

    protected Category mCategory;

    protected IBlogListPresenter mBlogListPresenter;
    protected BlogListItemAdapter mItemAdapter;
    private ServiceConnection mBlogServiceConnection;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogListPresenter = CnblogsPresenterFactory.getBlogListPresenter(getContext(), this);
        mCategory = getArguments().getParcelable("category");
        mItemAdapter = new BlogListItemAdapter();

        // 绑定服务
        bindService();

    }

    /**
     * 绑定服务
     */
    private void bindService() {
        mBlogServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder = (BlogServiceBinder) service;
                mBinder.setBlogContentTaskFinishListener(new OnTaskFinishListener() {
                    @Override
                    public void onTaskFinish(String taskName) {
                        // 重新刷新列表
                        mBlogListPresenter.refreshDataSet();
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        getContext().bindService(new Intent(getContext(), BlogService.class), mBlogServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 解绑服务
        if (mBlogServiceConnection != null) {
            getContext().unbindService(mBlogServiceConnection);
        }
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
                return mRecyclerView.isOnTop();
            }
        });
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                mBlogListPresenter.loadMore();
            }
        });

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBlogListPresenter.start();
    }

    @Override
    public void onLoadBlogList(int page, List<Blog> data) {
        if (page <= 1)
            mAppLayout.refreshComplete();
        else
            mRecyclerView.loadMoreComplete();

        mItemAdapter.invalidate(data);
        mItemAdapter.notifyDataSetChanged();

        // 异步下载博文内容
        if (mBinder != null) {
            mBinder.asyncBlogContent();
        }
    }

    @Override
    public void onLoadFailed(int page, String msg) {
        if (page <= 1)
            mAppLayout.refreshComplete();
        else
            mRecyclerView.loadMoreComplete();
    }

    @Override
    public Category getCategory() {
        return mCategory;
    }

}
