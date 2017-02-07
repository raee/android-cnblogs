package com.rae.cnblogs.presenter.impl.kb;

import android.content.Context;

import com.rae.cnblogs.presenter.impl.blog.BlogContentPresenterImpl;
import com.rae.cnblogs.sdk.bean.BlogBean;

/**
 * 知识库
 * Created by ChenRui on 2017/2/4 0004 14:49.
 */
public class KBContentPresenterImpl extends BlogContentPresenterImpl {

    public KBContentPresenterImpl(Context context, IBlogContentView view) {
        super(context, view);
    }

    @Override
    protected void onLoadData(BlogBean blog) {
        mBlogApi.getKbContent(blog.getBlogId(), this);
    }


    @Override
    public void doLike(boolean isCancel) {

        // 不支持取消点赞
        if (isCancel) {
            mView.onLikeError(isCancel, "您已经推荐过了");
            return;
        }
        mBlogApi.likeKb(mView.getBlog().getBlogId(), getLikeAndBookmarksListener(isCancel, true));
    }
}
