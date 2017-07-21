package com.rae.cnblogs.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogCommentItemAdapter;
import com.rae.cnblogs.dialog.impl.CommentMenuDialog;
import com.rae.cnblogs.dialog.impl.EditCommentDialog;
import com.rae.cnblogs.dialog.impl.HintCardDialog;
import com.rae.cnblogs.dialog.impl.MenuDialog;
import com.rae.cnblogs.message.EditCommentEvent;
import com.rae.cnblogs.model.MenuDialogItem;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogCommentPresenter;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.BlogType;
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
public class BlogCommentFragment extends BaseFragment implements IBlogCommentPresenter.IBlogCommentView, EditCommentDialog.OnEditCommentListener {

    public static BlogCommentFragment newInstance(BlogBean blog, BlogType type) {
        Bundle args = new Bundle();
        args.putString("type", type.getTypeName());
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


    TextView mCommentBadgeView;

    private BlogCommentItemAdapter mItemAdapter;
    private IBlogCommentPresenter mCommentPresenter;

    private BlogBean mBlog;

    private EditCommentDialog mEditCommentDialog;

    private CommentMenuDialog mCommentMenuDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_comment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mBlog = getArguments().getParcelable("blog");
            BlogType blogType = BlogType.typeOf(getArguments().getString("type"));
            mCommentPresenter = CnblogsPresenterFactory.getBlogCommentPresenter(getContext(), blogType, this);
        }

        mEditCommentDialog = new EditCommentDialog(getContext());
        mEditCommentDialog.setOnEditCommentListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommentBadgeView = (TextView) getActivity().findViewById(R.id.tv_comment_badge);
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
        mPlaceholderView.registerAdapterDataObserver(mItemAdapter);

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


        mCommentMenuDialog = new CommentMenuDialog(getContext());
        mCommentMenuDialog.addDeleteItem(getString(R.string.delete_comment));
        mCommentMenuDialog.setOnMenuItemClickListener(new MenuDialog.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(MenuDialog dialog, MenuDialogItem item) {
                // 执行删除
                if (mCommentMenuDialog.getBlogComment() != null) {
                    AppUI.loading(getContext());
                    mCommentPresenter.delete(mCommentMenuDialog.getBlogComment());
                }
            }
        });


        mItemAdapter.setOnBlogCommentItemClick(new BlogCommentItemAdapter.OnBlogCommentItemClick() {
            @Override
            public void onItemClick(BlogCommentBean comment) {
                mEditCommentDialog.show(comment);
            }
        });

        // 长按删除评论
        mItemAdapter.setOnBlogCommentItemLongClick(new BlogCommentItemAdapter.OnBlogCommentItemClick() {
            @Override
            public void onItemClick(BlogCommentBean comment) {
                // 判断当前评论是否属于自己的
                UserProvider instance = UserProvider.getInstance();
                if (instance.isLogin() && instance.getLoginUserInfo().getDisplayName().equalsIgnoreCase(comment.getAuthorName().trim())) {
                    mCommentMenuDialog.setBlogComment(comment);
                    mCommentMenuDialog.show();
                } else {
                    mEditCommentDialog.show(comment);
                }
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
    public void onEditCommentOpenEvent(EditCommentEvent event) {
        mEditCommentDialog.setBlogComment(null);
        mEditCommentDialog.show();
    }

    @Override
    public void onLoadCommentSuccess(List<BlogCommentBean> data) {
        mPlaceholderView.dismiss();
        mItemAdapter.invalidate(data);
        mItemAdapter.notifyDataSetChanged();
        mRecyclerView.loadMoreComplete();
    }

    @Override
    public BlogBean getBlog() {
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

    @Override
    public String getCommentContent() {
        return mEditCommentDialog.getCommentContent();
    }

    @Override
    public void onPostCommentFailed(String msg) {
        AppUI.dismiss();
        AppUI.failed(getContext(), msg);
        mEditCommentDialog.dismiss();
    }

    @Override
    public void onPostCommentSuccess() {
        AppUI.dismiss();
        mEditCommentDialog.dismiss();
        mPlaceholderView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCommentPresenter.start(); // 重新加载评论列表
            }
        }, 1000);


        // 评论数量加1
        int comment = parseInt(mBlog.getComment()) + 1;
        mCommentBadgeView.setText(String.valueOf(comment));

        if (config().hasCommentGuide()) {
            AppUI.toastInCenter(getContext(), "您伟大的讲话发表成功");
        } else {
            HintCardDialog dialog = new HintCardDialog(getContext());
            dialog.setMessage(getString(R.string.dialog_tips_post_comment));
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    config().commentGuide();
                }
            });
            dialog.show();
        }
    }

    @Override
    public boolean enableReferenceComment() {
        return mEditCommentDialog.enableReferenceComment();
    }

    @Override
    public void onDeleteCommentSuccess(BlogCommentBean item) {
        AppUI.dismiss();
        // 删除成功
        mItemAdapter.remove(item);
        mItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteCommentFailed(String msg) {
        AppUI.dismiss();
        AppUI.toast(getContext(), msg);
    }

    @Override
    public void onPostComment(String content, BlogCommentBean parent, boolean isReference) {
        // 发表评论
        AppUI.loading(getContext(), "正在发表..");
        mCommentPresenter.post(parent);
        mEditCommentDialog.dismiss();
    }


    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        if (mRecyclerView == null) return;
        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if (manager.findFirstVisibleItemPosition() <= 1) {
            return;
        }
        mRecyclerView.smoothScrollToPosition(0);
    }
}
