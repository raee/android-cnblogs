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
import com.rae.cnblogs.sdk.bean.Category;
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

    private Category mCategory;
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
        mCategory = getArguments().getParcelable("category");
        mItemAdapter = new BlogListItemAdapter();
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

        mBlogListPresenter.start();
    }

    @Override
    public void onLoadBlogList(int page, List<Blog> data) {
        showToast("博客加载成功!");
        if (page <= 1)
            mAppLayout.refreshComplete();
        else
            mRecyclerView.loadMoreComplete();

        mItemAdapter.invalidate(data);
        mItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFailed(int page, String msg) {
        showToast(msg);
        if (page <= 1)
            mAppLayout.refreshComplete();
        else
            mRecyclerView.loadMoreComplete();
    }

    @Override
    public Category getCategory() {
        return mCategory;
    }

    private void showToast(String msg) {
       /* Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_toast_home, null);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setText(msg);
        toast.setView(view);
        toast.setGravity(Gravity.TOP | Gravity.LEFT | Gravity.RIGHT, 0, mRecyclerViewTop);
        toast.show();*/
    }
}
