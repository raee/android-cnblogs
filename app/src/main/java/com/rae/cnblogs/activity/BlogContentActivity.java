package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.dialog.impl.BlogShareDialog;
import com.rae.cnblogs.fragment.BlogCommentFragment;
import com.rae.cnblogs.fragment.BlogContentFragment;
import com.rae.cnblogs.message.EditCommentEvent;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.widget.RaeDrawerLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 博文查看
 * Created by ChenRui on 2016/12/6 21:38.
 */
public class BlogContentActivity extends SwipeBackBaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;

    @BindView(R.id.img_blog_avatar)
    ImageView mAvatarView;

    @BindView(R.id.tv_blog_author)
    TextView mAuthorView;

    @BindView(R.id.tv_comment_badge)
    TextView mCommentBadgeView;

    @BindView(R.id.tv_like_badge)
    TextView mLikeBadgeView;

    @BindView(R.id.fl_content)
    View mContentLayout;

    @BindView(R.id.tv_edit_comment)
    View mPostCommentView;

    @BindView(R.id.layout_content_comment)
    View mViewCommentView;

    @BindView(R.id.fl_comment)
    RaeDrawerLayout mCommentLayout;

    private BlogShareDialog mShareDialog;
    private BlogBean mBlog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_content);
        setSupportActionBar(mToolbar);
        showHomeAsUp(mToolbar);

        mBlog = getIntent().getParcelableExtra("blog");
        BlogType blogType = BlogType.typeOf(getIntent().getStringExtra("type"));

        if (mBlog == null) {
            AppUI.toast(this, "博客为空！");
            finish();
            return;
        }

        mShareDialog = new BlogShareDialog(this, mBlog) {
            @Override
            protected void onViewSourceClick() {
                AppRoute.jumpToWeb(getContext(), mBlog.getUrl());
            }
        };


        // 评论角标
        if (!TextUtils.equals(mBlog.getComment(), "0")) {
            mCommentBadgeView.setText(mBlog.getComment());
            mCommentBadgeView.setSelected(true);
            mCommentBadgeView.setVisibility(View.VISIBLE);
        }

        // 点赞角标
        if (!TextUtils.equals(mBlog.getLikes(), "0")) {
            mLikeBadgeView.setText(mBlog.getLikes());
            mLikeBadgeView.setBackgroundResource(R.drawable.ic_like_content_badge);
            mLikeBadgeView.setVisibility(View.VISIBLE);
        }

        // 知识库没有评论处理
        if (blogType == BlogType.KB) {
            mPostCommentView.setVisibility(View.GONE);
            mViewCommentView.setVisibility(View.GONE);
            mAuthorView.setVisibility(View.GONE);
            mAvatarView.setVisibility(View.GONE);
        } else {
            ImageLoader.getInstance().displayImage(mBlog.getAvatar(), mAvatarView, RaeImageLoader.headerOption());
            mAuthorView.setText(mBlog.getAuthor());
        }


        // 加载Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_comment, BlogCommentFragment.newInstance(mBlog, blogType));
        transaction.add(R.id.fl_content, BlogContentFragment.newInstance(mBlog, blogType));
        transaction.commit();

    }

    // 分享
    @OnClick(R.id.img_action_bar_more)
    public void onActionMenuMoreClick() {
        mShareDialog.show();
    }

    // 查看评论
    @OnClick(R.id.layout_content_comment)
    public void onCommentClick() {
        mCommentLayout.toggleSmoothScroll();
    }

    @OnClick(R.id.tv_edit_comment)
    public void onEditCommentClick() {
        // 通知里面的评论打开发表对话框
        EventBus.getDefault().post(new EditCommentEvent());
    }

    @OnClick(R.id.tool_bar)
    public void onActionBarClick() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null || fragments.size() < 1) {
            return;
        }

        if (mCommentLayout.getVisibility() == View.VISIBLE) {
            BlogCommentFragment fragment = (BlogCommentFragment) fragments.get(0);
            fragment.scrollToTop();
        } else {
            BlogContentFragment fragment = (BlogContentFragment) fragments.get(1);
            fragment.scrollToTop();
        }

    }


    // 返回键处理
    @Override
    public void onBackPressed() {
        if (mCommentLayout.getVisibility() == View.VISIBLE) {
            mCommentLayout.toggleSmoothScroll();
            return;
        }
        super.onBackPressed();
    }
}
