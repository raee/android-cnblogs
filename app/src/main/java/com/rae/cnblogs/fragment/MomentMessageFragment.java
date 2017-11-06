package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeViewCompat;
import com.rae.cnblogs.adapter.BaseItemAdapter;
import com.rae.cnblogs.adapter.MomentMessageAdapter;
import com.rae.cnblogs.dialog.impl.EditCommentDialog;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IMomentMessageContract;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 回复我的消息
 * Created by ChenRui on 2017/11/6 0006 14:21.
 */
public class MomentMessageFragment extends BaseFragment implements IMomentMessageContract.View {

    @BindView(R.id.recycler_view)
    RaeRecyclerView mRecyclerView;
    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholderView;
    @BindView(R.id.ptr_content)
    AppLayout mAppLayout;

    MomentMessageAdapter mAdapter;
    IMomentMessageContract.Presenter mPresenter;
    private EditCommentDialog mEditCommentDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_moment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = CnblogsPresenterFactory.getMomentMessagePresenter(getContext(), this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mPresenter.destroy();
        mAdapter.setOnItemClickListener(null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MomentMessageAdapter();
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<MomentCommentBean>() {
            @Override
            public void onItemClick(MomentCommentBean item) {
                // 弹出回复对话框
                mEditCommentDialog.show(item);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mPlaceholderView.dismiss();
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                start();
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
                mPresenter.loadMore();
            }
        });

        mEditCommentDialog = new EditCommentDialog(getContext());
        mEditCommentDialog.setOnEditCommentListener(new EditCommentDialog.OnEditCommentListener() {
            @Override
            public void onPostComment(String content, BlogCommentBean parent, boolean isReference) {
                // 发布评论
                mEditCommentDialog.showLoading();
                MomentCommentBean commentBean = mEditCommentDialog.getMomentCommentBean();
                if (commentBean != null) {
                    performPostComment(content, commentBean);
                }
            }

            private void performPostComment(String content, MomentCommentBean commentBean) {
                String ingId = commentBean.getIngId();
                String userId = commentBean.getUserAlias();
                String commentId = commentBean.getId();
                content = String.format("@%s：%s", commentBean.getAuthorName(), content);
                mPresenter.postComment(ingId, userId, commentId, content);
            }
        });
    }

    /**
     * 开始加载数据
     */
    protected void start() {
        mPresenter.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start();
    }

    @Override
    public void onNoMoreData() {
        mRecyclerView.setNoMore(true);
    }

    @Override
    public void onEmptyData(String msg) {
        mAppLayout.refreshComplete();
        mPlaceholderView.retry(msg);
    }

    @Override
    public void onLoadData(List<MomentCommentBean> data) {
        mRecyclerView.setNoMore(false);
        mPlaceholderView.dismiss();
        mAppLayout.refreshComplete();
        mRecyclerView.loadMoreComplete();
        mAdapter.invalidate(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoginExpired() {
        mPlaceholderView.retry(getString(R.string.login_expired));
    }


    @Override
    public void onPostCommentFailed(String message) {
        mEditCommentDialog.dismissLoading();
        AppUI.failed(getContext(), message);
    }

    @Override
    public void onPostCommentSuccess() {
        mEditCommentDialog.dismiss();
        AppUI.toastInCenter(getContext(), getString(R.string.tips_comment_success));
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        RaeViewCompat.scrollToTop(mRecyclerView);
        if (mRecyclerView.isOnTop()) {
            mAppLayout.post(new Runnable() {
                @Override
                public void run() {
                    mAppLayout.autoRefresh();
                }
            });
        }
    }
}