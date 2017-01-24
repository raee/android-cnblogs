package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.dialog.impl.BlogShareDialog;
import com.rae.cnblogs.fragment.BlogCommentFragment;
import com.rae.cnblogs.fragment.BlogContentFragment;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.widget.RaeDrawerLayout;

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

    @BindView(R.id.fl_comment)
    RaeDrawerLayout mCommentLayout;

    private BlogShareDialog mContentDialog;
    private BlogContentFragment mContentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_content);
        setSupportActionBar(mToolbar);
        showHomeAsUp(mToolbar);

        for (int i = 0; i < mToolbar.getChildCount(); i++) {
            View childAt = mToolbar.getChildAt(i);
            if (childAt.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                Log.w("Rae", params.leftMargin + "_" + params.topMargin + "_" + params.rightMargin + "_" + params.bottomMargin);
            }
            Log.d("Rae", childAt.getId() + " -- >" + childAt.toString());
        }


        Blog blog = getIntent().getParcelableExtra("blog");
        mContentDialog = new BlogShareDialog(this, blog) {
            @Override
            protected void onViewSourceClick() {
                if (mContentFragment.isHidden() || mContentFragment.isDetached()) return;
                mContentFragment.loadSourceUrl();
            }
        };

//        mCommentDialog = BlogCommentDialog.newInstance(blog);

        if (blog != null) {
            ImageLoader.getInstance().displayImage(blog.getAvatar(), mAvatarView, RaeImageLoader.headerOption());
            mAuthorView.setText(blog.getAuthor());
            // 角标处理
            if (!TextUtils.equals(blog.getComment(), "0")) {
                mCommentBadgeView.setText(blog.getComment());
                mCommentBadgeView.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.equals(blog.getLikes(), "0")) {
                mLikeBadgeView.setText(blog.getLikes());
                mLikeBadgeView.setVisibility(View.VISIBLE);
            }

        }

        mContentFragment = BlogContentFragment.newInstance(blog);
        BlogCommentFragment blogCommentFragment = BlogCommentFragment.newInstance(blog);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_comment, blogCommentFragment);
        transaction.add(R.id.fl_content, mContentFragment);
        transaction.commit();

    }

    @OnClick(R.id.img_action_bar_more)
    public void onActionMenuMoreClick() {
        mContentDialog.show();
    }

    @OnClick(R.id.layout_content_comment)
    public void onCommentClick() {
        mCommentLayout.toggleSmoothScroll();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mContentFragment != null && mContentFragment.isVisible() && mContentFragment.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mCommentLayout.getVisibility() == View.VISIBLE) {
            mCommentLayout.toggleSmoothScroll();
            return;
        }
        super.onBackPressed();

    }
}
