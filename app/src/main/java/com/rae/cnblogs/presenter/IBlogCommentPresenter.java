package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogComment;

import java.util.List;

/**
 * 评论
 * Created by ChenRui on 2016/12/26 0026 8:48.
 */
public interface IBlogCommentPresenter extends IRaePresenter {

    void loadMore();

    interface IBlogCommentView {
        void onLoadCommentSuccess(List<BlogComment> data);

        Blog getBlog();

        void onLoadCommentEmpty();

        void onLoadMoreCommentEmpty();
    }
}
