package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogListItemAdapter;
import com.rae.cnblogs.sdk.bean.Category;

import butterknife.BindView;

/**
 * 知识库
 * Created by ChenRui on 2017/1/18 23:49.
 */
public class KBListFragment extends BlogListFragment {

    @BindView(R.id.tv_title)
    TextView mTitleView;

    public static KBListFragment newInstance(Category category) {
        Bundle args = new Bundle();
        args.putParcelable("category", category);
        KBListFragment fragment = new KBListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_kb_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemAdapter.setViewType(BlogListItemAdapter.VIEW_TYPE_WITHOUT_AVATAR);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleView.setText(mCategory.getName());
    }
}
