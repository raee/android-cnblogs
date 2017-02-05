package com.rae.cnblogs.dialog.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.bean.BlogComment;

/**
 * 评论的菜单
 * Created by ChenRui on 2017/2/5 23:04.
 */
public class CommentMenuDialog extends MenuDialog {

    private BlogComment mBlogComment;

    public CommentMenuDialog(Context context) {
        super(context);
    }

    public void setBlogComment(BlogComment blogComment) {
        mBlogComment = blogComment;
    }

    public BlogComment getBlogComment() {
        return mBlogComment;
    }
}
