package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import butterknife.BindView;

/**
 * 博客类型的列表
 * Created by ChenRui on 2017/1/18 23:49.
 */
public class BlogTypeListFragment extends BlogListFragment {

    @BindView(R.id.tv_title)
    TextView mTitleView;

    public static BlogTypeListFragment newInstance(CategoryBean category, BlogType type) {
        Bundle args = new Bundle();
        args.putParcelable("category", category);
        args.putString("type", type.getTypeName());
        BlogTypeListFragment fragment = new BlogTypeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_kb_list;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleView.setText(getTitle());
    }
}
