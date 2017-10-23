package com.rae.cnblogs.model;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeViewCompat;
import com.rae.cnblogs.adapter.BaseItemAdapter;
import com.rae.cnblogs.adapter.FeedItemAdapter;
import com.rae.cnblogs.fragment.BaseFragment;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IFeedPresenter;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.swift.Rx;

import java.util.List;

import butterknife.BindView;

/**
 * 作者的动态列表
 * Created by ChenRui on 2017/3/16 16:08.
 */
public class FeedListFragment extends BaseFragment implements IFeedPresenter.IFeedView {

    private FeedItemAdapter mAdapter;

    public static FeedListFragment newInstance(String blogApp) {
        Bundle args = new Bundle();
        args.putString("blogApp", blogApp);
        FeedListFragment fragment = new FeedListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private IFeedPresenter mFeedPresenter;

    private String mBlogApp;


    @BindView(R.id.content)
    AppLayout mAppLayout;

    @BindView(R.id.rec_blog_list)
    RaeRecyclerView mRecyclerView;

    @BindView(R.id.blog_list_placeholder)
    PlaceholderView mPlaceholderView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBlogApp = getArguments().getString("blogApp");
            mFeedPresenter = CnblogsPresenterFactory.getFeedPresenter(getContext(), this);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_list;
    }

    @Override
    protected void onLoadData() {
        mPlaceholderView.dismiss();
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeedPresenter.start();
            }
        });
        mAppLayout.setEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        mAdapter = new FeedItemAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<UserFeedBean>() {
            @Override
            public void onItemClick(UserFeedBean item) {
                if ("发表评论".equals(item.getAction())) {
                    return;
                }
                AppRoute.jumpToWeb(getContext(), item.getUrl());
            }
        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mFeedPresenter.loadMore();
            }
        });
        mFeedPresenter.start();
    }

    @Override
    public String getBlogApp() {
        return mBlogApp;
    }

    @Override
    public void onLoadFeedFailed(String msg) {
        mPlaceholderView.retry(msg);
    }

    @Override
    public void onLoadMoreFeedFailed(String msg) {
        mPlaceholderView.dismiss();
        mRecyclerView.loadMoreComplete();
    }

    @Override
    public void onLoadFeedSuccess(List<UserFeedBean> dataList) {
        mRecyclerView.loadMoreComplete();
        if (Rx.isEmpty(dataList)) {
            mPlaceholderView.empty();
        } else {
            mPlaceholderView.dismiss();
            mAdapter.invalidate(dataList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadMoreFinish() {
        mRecyclerView.setNoMore(true);
    }


    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        RaeViewCompat.scrollToTop(mRecyclerView);
    }
}
