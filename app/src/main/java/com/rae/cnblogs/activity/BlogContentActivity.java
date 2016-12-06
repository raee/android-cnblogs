package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.fragment.BlogContentFragment;
import com.rae.cnblogs.sdk.bean.Blog;

import butterknife.BindView;

/**
 * 博文查看
 * Created by ChenRui on 2016/12/6 21:38.
 */
public class BlogContentActivity extends BaseActivity {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_content);
        bindView();
        setSupportActionBar(mToolbar);
        showHomeAsUp(mToolbar);

        Blog blog = getIntent().getParcelableExtra("blog");

        if (blog != null) {
            ImageLoader.getInstance().displayImage(blog.getAvatar(), mAvatarView, RaeImageLoader.headerOptinos());
            mAuthorView.setText(blog.getAuthor());
            if (!TextUtils.equals(blog.getComment(), "0")) {
                mCommentBadgeView.setText(blog.getComment());
                mCommentBadgeView.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.equals(blog.getLikes(), "0")) {
                mLikeBadgeView.setText(blog.getLikes());
                mLikeBadgeView.setVisibility(View.VISIBLE);
            }

        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_content, BlogContentFragment.newInstance(blog));
        transaction.commit();
    }

}
