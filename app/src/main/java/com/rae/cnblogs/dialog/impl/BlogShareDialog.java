package com.rae.cnblogs.dialog.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.bean.BlogBean;

/**
 * 分享对话框
 * Created by ChenRui on 2016/12/7 22:15.
 */
public class BlogShareDialog extends ShareDialog {

    private BlogBean mBlog;

    public BlogShareDialog(Context context) {
        super(context);
    }


    protected String getUrl() {
        return mBlog == null ? null : mBlog.getUrl();
    }

//    protected void onBrowserViewClick() {
//        if (mBlog == null) return;
//        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(getUrl()));
//            getContext().startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void show(BlogBean blog) {
        if (blog == null) return;
        mBlog = blog;

        if (TextUtils.isEmpty(blog.getAvatar())) {
            setShareWeb(blog.getUrl(), blog.getTitle(), blog.getSummary(), R.drawable.ic_share_app);
        } else {
            setShareWeb(blog.getUrl(), blog.getTitle(), blog.getSummary(), blog.getAvatar());
        }

        show();
    }

//    // 复制链接
//    protected void onLinkClick() {
//
//    }

}
