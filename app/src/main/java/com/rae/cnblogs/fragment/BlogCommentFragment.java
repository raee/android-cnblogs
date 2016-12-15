package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogCommentItemAdapter;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 评论
 * Created by ChenRui on 2016/12/15 0015 19:22.
 */
public class BlogCommentFragment extends BaseFragment {

    public static BlogCommentFragment newInstance(Blog blog) {

        Bundle args = new Bundle();
        args.putParcelable("blog", blog);
        BlogCommentFragment fragment = new BlogCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rec_blog_comment_list)
    RaeRecyclerView mRecyclerView;
    private BlogCommentItemAdapter mItemAdapter;

    private Blog mBlog;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_comment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mBlog = getArguments().getParcelable("blog");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mItemAdapter = new BlogCommentItemAdapter();
        List<Blog> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new Blog());
        }
        mItemAdapter.invalidate(data);
        mRecyclerView.setAdapter(mItemAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
