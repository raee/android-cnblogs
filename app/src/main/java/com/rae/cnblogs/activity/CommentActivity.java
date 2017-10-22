package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.BlogCommentFragment;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;

/**
 * 评论
 * Created by ChenRui on 2017/10/21 0021 2:54.
 */
public class CommentActivity extends SwipeBackBaseActivity {

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
        fragment = BlogCommentFragment.newInstance((BlogBean) getIntent().getParcelableExtra("blog"), BlogType.typeOf(getIntent().getStringExtra("type")));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, fragment)
                .commit();
    }
}
