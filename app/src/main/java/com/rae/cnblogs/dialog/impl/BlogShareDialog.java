package com.rae.cnblogs.dialog.impl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.bean.Blog;

/**
 * 分享对话框
 * Created by ChenRui on 2016/12/7 22:15.
 */
public class BlogShareDialog extends ShareDialog {

    private final Blog mBlog;

    public BlogShareDialog(Context context, Blog blog) {
        super(context);
        mBlog = blog;

        setShareTitle(blog.getTitle());
        setShareSummary(blog.getSummary());
        setShareUrl(blog.getUrl());

        if (!TextUtils.isEmpty(blog.getAvatar())) {
            setShareIcon(blog.getAvatar());
        } else {
            setShareIcon(R.drawable.ic_share);
        }
    }


    protected String getUrl() {
        return mBlog == null ? null : mBlog.getUrl();
    }

    protected void onBrowserViewClick() {
        if (mBlog == null) return;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getUrl()));
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 复制链接
    protected void onLinkClick() {
        if (mBlog == null) return;
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("url", mBlog.getUrl()));
        AppUI.success(getContext(), R.string.copy_link_success);
    }

}
