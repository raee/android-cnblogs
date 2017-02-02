package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogCommentItemAdapter;
import com.rae.cnblogs.dialog.impl.EditCommentDialog;
import com.rae.cnblogs.message.EditCommentEvent;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogCommentPresenter;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeDrawerLayout;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.cnblogs.widget.compat.RaeDragDownCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * 评论
 * Created by ChenRui on 2016/12/15 0015 19:22.
 */
public class BlogCommentFragment extends BaseFragment implements IBlogCommentPresenter.IBlogCommentView {

    public static BlogCommentFragment newInstance(Blog blog) {

        Bundle args = new Bundle();
        args.putParcelable("blog", blog);
        BlogCommentFragment fragment = new BlogCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rec_blog_comment_list)
    RaeRecyclerView mRecyclerView;

    private RaeDrawerLayout mParentView;

    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholderView;

    private BlogCommentItemAdapter mItemAdapter;
    private IBlogCommentPresenter mCommentPresenter;

    private Blog mBlog;

    private EditCommentDialog mEditCommentDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_comment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mCommentPresenter = CnblogsPresenterFactory.getBlogCommentPresenter(getContext(), this);
        if (getArguments() != null)
            mBlog = getArguments().getParcelable("blog");

        mEditCommentDialog = new EditCommentDialog(getContext(), mBlog);
        mEditCommentDialog.setOnEditCommentListener(new EditCommentDialog.OnEditCommentListener() {
            @Override
            public void onSendCommentSuccess(String body) {
                mCommentPresenter.start();
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mParentView = (RaeDrawerLayout) view.getParent();
        mParentView.setDragDownHandler(new RaeDragDownCompat.DragDownHandler() {
            @Override
            public boolean checkCanDrag(float dy, MotionEvent ev) {
                if (mPlaceholderView.getVisibility() == View.VISIBLE) {
                    return true;
                }
                if (dy < 0 && mRecyclerView.isOnTop()) {
                    return true;
                }
                return false;
            }
        });
        initView();
    }

    private void initView() {
        mItemAdapter = new BlogCommentItemAdapter();
        mRecyclerView.setNoMoreText(R.string.no_more_comment);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mCommentPresenter.loadMore();
            }
        });

        mItemAdapter.setOnBlogCommentItemClick(new BlogCommentItemAdapter.OnBlogCommentItemClick() {
            @Override
            public void onItemClick(BlogComment comment) {
                mEditCommentDialog.show(comment);
            }
        });

        mPlaceholderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditCommentDialog.show();
            }
        });

        mRecyclerView.setAdapter(mItemAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mCommentPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEditCommentSuccessEvent(EditCommentEvent event) {
        mCommentPresenter.start();
    }

    @Override
    public void onLoadCommentSuccess(List<BlogComment> data) {
        mPlaceholderView.dismiss();
        mItemAdapter.invalidate(data);
        mItemAdapter.notifyDataSetChanged();
        mRecyclerView.loadMoreComplete();
    }

    @Override
    public Blog getBlog() {
        return mBlog;
    }

    @Override
    public void onLoadCommentEmpty() {
        mPlaceholderView.empty();
    }

    @Override
    public void onLoadMoreCommentEmpty() {
        mRecyclerView.loadMoreComplete();
        mRecyclerView.setNoMore(true);
    }
}
