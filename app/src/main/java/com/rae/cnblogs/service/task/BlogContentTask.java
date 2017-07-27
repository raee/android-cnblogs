package com.rae.cnblogs.service.task;

import android.text.TextUtils;
import android.util.Log;

import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.DbBlog;

import java.io.IOException;

import retrofit2.Response;

/**
 * 任务执行
 * Created by ChenRui on 2017/7/27 0027 16:37.
 */
public class BlogContentTask implements Runnable {

    private final DbBlog mDbBlog;
    private IBlogApi mBlogApi;
    private INewsApi mNewsApi;
    private BlogBean mBlog;

    public BlogContentTask(IBlogApi blogApi, INewsApi newsApi, DbBlog dbBlog, BlogBean blog) {
        mBlogApi = blogApi;
        mNewsApi = newsApi;
        mBlog = blog;
        mDbBlog = dbBlog;
    }

    @Override
    public void run() {
        if (!TextUtils.isEmpty(mBlog.getContent())) {
            return;
        }
        BlogType type = BlogType.typeOf(mBlog.getBlogType());
        String blogId = mBlog.getBlogId();

        if (type == BlogType.BLOG) {
            try {
                Log.i("rae", "正在执行任务：" + blogId);
                Response<String> response = mBlogApi.syncGetBlogContent(blogId).execute();
                String content = response.body();

                mDbBlog.updateBlogContent(blogId, mBlog.getBlogType(), content);
                Log.i("rae", "执行任务成功：[" + mBlog.getBlogType() + "] --> " + blogId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
