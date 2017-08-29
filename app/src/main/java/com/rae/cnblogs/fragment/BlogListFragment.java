package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogListItemAdapter;
import com.rae.cnblogs.message.TabEvent;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogListPresenter;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.service.JobScheduler;
import com.rae.cnblogs.service.job.JobEvent;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.swift.Rx;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 博客列表
 * Created by ChenRui on 2016/12/2 00:33.
 */
public class BlogListFragment extends BaseFragment implements IBlogListPresenter.IBlogListView {


    public static BlogListFragment newInstance(int position, CategoryBean category, BlogType type) {
        Bundle args = new Bundle();
        args.putParcelable("category", category);
        args.putString("type", type.getTypeName());
        args.putInt("position", position);
        BlogListFragment fragment = new BlogListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @BindView(R.id.content)
    AppLayout mAppLayout;

    @BindView(R.id.rec_blog_list)
    RaeRecyclerView mRecyclerView;

    protected CategoryBean mCategory;
    protected BlogType mBlogType;

    protected IBlogListPresenter mBlogListPresenter;
    protected BlogListItemAdapter mItemAdapter;
    @BindView(R.id.blog_list_placeholder)
    PlaceholderView mPlaceholderView;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getArguments().getParcelable("category");
        mBlogType = BlogType.typeOf(getArguments().getString("type"));
        mItemAdapter = new BlogListItemAdapter(this.getContext(), mBlogType, mPlaceholderView);
        mBlogListPresenter = getBlogListPresenter();
        EventBus.getDefault().register(this);
    }

    protected IBlogListPresenter getBlogListPresenter() {
        return CnblogsPresenterFactory.getBlogListPresenter(getContext(), mBlogType, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("博客列表");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("博客列表");
        // 统计分类
        if (mCategory != null) {
            AppMobclickAgent.onCategoryEvent(getContext(), mCategory.getName());
        }
    }

    @Override
    public void onDestroy() {
        mItemAdapter.destroy();
        mAppLayout.removeAllViews();
        mRecyclerView.removeAllViews();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    protected String getTitle() {
        return mCategory.getName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemAdapter.empty();
                mBlogListPresenter.start();
            }
        });

        mRecyclerView.setAdapter(mItemAdapter);
        mRecyclerView.setLoadingMoreEnabled(false);
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
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                mBlogListPresenter.loadMore();
            }
        });

        if (mBlogType == BlogType.BLOGGER) {
            mAppLayout.setEnabled(false); // 博主页面不允许刷新
        }
    }

    @Override
    protected void onLoadData() {
        mBlogListPresenter.start();
        mPlaceholderView.dismiss();
    }

    @Override
    public void onLoadBlogList(int page, List<BlogBean> data) {
        mPlaceholderView.dismiss();
        if (Rx.isEmpty(data) && page <= 1) {
            mAppLayout.refreshComplete();
            return;
        }
        mRecyclerView.setNoMore(false);
        mRecyclerView.setLoadingMoreEnabled(true);
        if (page <= 1)
            mAppLayout.refreshComplete();
        else
            mRecyclerView.loadMoreComplete();

        mItemAdapter.invalidate(new ArrayList<>(data));
        mItemAdapter.notifyDataSetChanged();

        // 通知异步下载博文内容
        EventBus.getDefault().post(new JobEvent(JobScheduler.ACTION_BLOG_CONTENT));

    }

    @Override
    public void onLoadFailed(int page, String msg) {
        if (page <= 1) {
            mPlaceholderView.retry(msg);
            mAppLayout.refreshComplete();
        } else {
            mPlaceholderView.dismiss();
            mRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public CategoryBean getCategory() {
        return mCategory;
    }

    @Override
    public void onLoadMoreEmpty() {
        mRecyclerView.loadMoreComplete();
        mRecyclerView.setNoMore(true);
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        if (mRecyclerView == null) return;

        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        int visibleCount = lastItem - firstItem;

        // 已经在顶部
        if (firstItem <= 1) {
            mAppLayout.autoRefresh();
        } else if (lastItem > visibleCount) {
            layoutManager.scrollToPosition(visibleCount + 1);
        }
        mRecyclerView.smoothScrollToPosition(0);
    }

    public void refreshCategory(CategoryBean category) {
        mCategory = category;
        getArguments().putParcelable("category", category);
    }

    @Subscribe
    public void onTabEvent(TabEvent event) {
        if (event.getPosition() == getArguments().getInt("position")) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    scrollToTop();

                }
            });
        }
    }
}
