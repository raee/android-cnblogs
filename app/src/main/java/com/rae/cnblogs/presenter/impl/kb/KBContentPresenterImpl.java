package com.rae.cnblogs.presenter.impl.kb;

import android.content.Context;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.presenter.impl.blog.BlogContentPresenterImpl;
import com.rae.cnblogs.sdk.bean.Blog;

/**
 * 知识库
 * Created by ChenRui on 2017/2/4 0004 14:49.
 */
public class KBContentPresenterImpl extends BlogContentPresenterImpl {

    public KBContentPresenterImpl(Context context, IBlogContentView view) {
        super(context, view);
    }

    @Override
    protected void onLoadData(Blog blog) {
        mBlogApi.getKbContent(blog.getBlogId(), this);
    }


    @Override
    public void doLike(boolean isCancel) {
        AppUI.toast(mContext, "知识库未实现点赞功能！");
    }
}
