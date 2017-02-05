package com.rae.cnblogs.presenter.impl.news;

import android.content.Context;

import com.rae.cnblogs.presenter.impl.blog.BlogContentPresenterImpl;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.Blog;

/**
 * 新闻内容
 * Created by ChenRui on 2017/2/4 0004 14:49.
 */
public class NewsContentPresenterImpl extends BlogContentPresenterImpl {
    private INewsApi mNewsApi;

    public NewsContentPresenterImpl(Context context, IBlogContentView view) {
        super(context, view);
        mNewsApi = getApiProvider().getNewsApi();
    }

    @Override
    protected void onLoadData(Blog blog) {
        mNewsApi.getNewsContent(blog.getBlogId(), this);
    }

    @Override
    public void doLike(boolean isCancel) {

        // 不支持取消点赞
        if (isCancel) {
            mView.onLikeError(isCancel, "您已经推荐过了");
            return;
        }
        mNewsApi.like(mView.getBlog().getBlogId(), getLikeAndBookmarksListener(isCancel, true));
    }
}
