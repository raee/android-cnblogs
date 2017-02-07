package com.rae.cnblogs.presenter.impl.kb;

import android.content.Context;

import com.rae.cnblogs.presenter.impl.blog.BlogListPresenterImpl;
import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 知识库
 * Created by ChenRui on 2017/2/4 0004 14:09.
 */
public class KBListPresenterImpl extends BlogListPresenterImpl {

    public KBListPresenterImpl(Context context, IBlogListView view) {
        super(context, view);
    }

    @Override
    protected void onLoadData(CategoryBean category, int pageIndex) {
        mApi.getKbArticles(pageIndex, this);
    }
}
