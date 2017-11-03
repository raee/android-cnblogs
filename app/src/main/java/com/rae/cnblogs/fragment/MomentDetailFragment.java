package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeViewCompat;
import com.rae.cnblogs.adapter.BaseItemAdapter;
import com.rae.cnblogs.adapter.MomentAdapter;
import com.rae.cnblogs.adapter.MomentDetailAdapter;
import com.rae.cnblogs.dialog.impl.EditCommentDialog;
import com.rae.cnblogs.model.MomentHolder;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IMomentDetailContract;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeLoadMoreView;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 闪存详情
 * Created by ChenRui on 2017/11/2 0002 15:35.
 */
public class MomentDetailFragment extends BaseFragment implements IMomentDetailContract.View {

    @BindView(R.id.recycler_view)
    RaeRecyclerView mRecyclerView;
    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholderView;
    @BindView(R.id.ptr_content)
    AppLayout mAppLayout;
    private MomentBean mData;
    private MomentDetailAdapter mAdapter;
    private IMomentDetailContract.Presenter mPresenter;

    private EditCommentDialog mEditCommentDialog;


    public static MomentDetailFragment newInstance(MomentBean data) {
        Bundle args = new Bundle();
        args.putParcelable("data", data);
        MomentDetailFragment fragment = new MomentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_moment_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = CnblogsPresenterFactory.getMomentDetailPresenter(getContext(), this);

        if (getArguments() != null) {
            mData = getArguments().getParcelable("data");
        }
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mData == null) {
            mPlaceholderView.empty("闪存数据为空");
            mAppLayout.setEnabled(false);
            return;
        }

        mEditCommentDialog = new EditCommentDialog(getContext());
        mEditCommentDialog.setOnEditCommentListener(new EditCommentDialog.OnEditCommentListener() {
            @Override
            public void onPostComment(String content, BlogCommentBean parent, boolean isReference) {
                // 发布评论
                mEditCommentDialog.showLoading();
                MomentCommentBean commentBean = mEditCommentDialog.getMomentCommentBean();
                performPostComment(content, commentBean);
            }

            private void performPostComment(String content, MomentCommentBean commentBean) {
                String ingId = mData.getId();
                String userId = commentBean == null ? mData.getUserAlias() : commentBean.getUserAlias();
                String commentId = commentBean == null ? "0" : commentBean.getId();
                if (commentBean != null && !TextUtils.isEmpty(commentBean.getUserAlias())) {
                    content = String.format("@%s：%s", commentBean.getAuthorName(), content);
                }
                mPresenter.postComment(ingId, userId, commentId, content);
            }
        });

        mPlaceholderView.dismiss();

        mAdapter = new MomentDetailAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNoMoreText(R.string.no_more_comment);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(true);
        mAdapter.setOnPlaceholderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommentClick();
            }
        });
        mAdapter.setOnBloggerClickListener(new MomentAdapter.OnBloggerClickListener() {
            @Override
            public void onBloggerClick(String blogApp) {
                AppRoute.jumpToBlogger(getContext(), blogApp);
            }
        });
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<MomentCommentBean>() {
            @Override
            public void onItemClick(MomentCommentBean item) {
                if (item != null) {
                    mEditCommentDialog.show(item);
                }
            }
        });

        // 加载更多样式
        RaeLoadMoreView footView = mRecyclerView.getFootView();
        footView.setTextColor(ContextCompat.getColor(getContext(), R.color.dividerColor));
        footView.setPadding(footView.getPaddingLeft(), footView.getPaddingTop() + 20, footView.getPaddingRight(), footView.getPaddingBottom() + 20);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
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

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.tool_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RaeViewCompat.scrollToTop(mRecyclerView);
            }
        });

        start();
    }

    public void start() {
        if (mData != null) {
            mPresenter.start();
        }
    }

    @Override
    public MomentBean getMomentInfo() {
        return mData;
    }

    @Override
    public void onEmptyComment(String message) {
        mRecyclerView.setNoMore(true);
        mAppLayout.refreshComplete();
        mRecyclerView.loadMoreComplete();
        mAdapter.empty(message);
    }

    @Override
    public void onLoadComments(List<MomentCommentBean> data, boolean hasMore) {
        mRecyclerView.loadMoreComplete();
        mAppLayout.refreshComplete();
        mAdapter.invalidate(data);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setNoMore(!hasMore);
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
        // 重新加载
        start();
    }

    @Override
    public String getBlogApp() {
        return mData.getBlogApp();
    }

    @Override
    public void onLoadBloggerInfoFailed(String msg) {
        MomentHolder holder = mAdapter.getMomentHolder();
        if (holder != null && holder.followView != null) {
            holder.followView.setEnabled(false);
            holder.followView.setText("信息异常");

        }
    }

    @Override
    public void onLoadBloggerInfo(FriendsInfoBean info) {
        MomentHolder holder = mAdapter.getMomentHolder();
        if (holder != null && holder.followView != null) {
            holder.followView.setEnabled(true);
            holder.followView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!UserProvider.getInstance().isLogin()) {
                        AppRoute.jumpToLogin(v.getContext());
                        return;
                    }
                    ((Button) v).setText("请稍后");
                    v.setEnabled(false);
                    mPresenter.follow();
                }
            });
            holder.followView.setSelected(info.isFollowed());
            holder.followView.setText(info.isFollowed() ? "取消关注" : "加关注");
        }
    }

    @Override
    public void onFollowFailed(String msg) {
        AppUI.failed(getContext(), msg);
        onFollowSuccess();
    }

    @Override
    public void onFollowSuccess() {
        MomentHolder holder = mAdapter.getMomentHolder();
        if (holder != null && holder.followView != null) {
            holder.followView.setEnabled(true);
            holder.followView.setText(mPresenter.isFollowed() ? R.string.cancel_follow : R.string.following);
        }
    }


    /**
     * 评论
     */
    @OnClick(R.id.tv_edit_comment)
    public void onCommentClick() {
        mEditCommentDialog.show();
    }


}
