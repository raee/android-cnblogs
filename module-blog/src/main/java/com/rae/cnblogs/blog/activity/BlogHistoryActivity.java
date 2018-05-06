package com.rae.cnblogs.blog.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.blog.R;

import butterknife.BindView;

/**
 * 浏览记录
 */
@Route(path = AppRoute.PATH_BLOG_HISTORY)
public class BlogHistoryActivity extends SwipeBackBasicActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_history);
    }
}
