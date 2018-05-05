package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.BlogCommentFragment;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;

/**
 * 评论
 * Created by ChenRui on 2017/10/21 0021 2:54.
 */
@Route(path = AppRoute.PATH_BLOG_COMMENT)
public class CommentActivity extends SwipeBackBasicActivity {

    private BlogCommentFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        showHomeAsUp();
        mToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.scrollToTop();
            }
        });
        BlogBean blog = getIntent().getParcelableExtra("blog");
        String type = getIntent().getStringExtra("type");
        fragment = BlogCommentFragment.newInstance(blog, BlogType.typeOf(type));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, fragment)
                .commit();
    }
}
