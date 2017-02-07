package com.rae.cnblogs.dialog.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.bean.BlogCommentBean;

/**
 * 评论的菜单
 * Created by ChenRui on 2017/2/5 23:04.
 */
public class CommentMenuDialog extends MenuDialog {

    private BlogCommentBean mBlogComment;

    public CommentMenuDialog(Context context) {
        super(context);
    }

    public void setBlogComment(BlogCommentBean blogComment) {
        mBlogComment = blogComment;
    }

    public BlogCommentBean getBlogComment() {
        return mBlogComment;
    }
}
