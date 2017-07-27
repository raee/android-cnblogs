package com.rae.cnblogs.service.job;

import android.content.Context;
import android.util.Log;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.service.task.BlogContentTask;

import java.util.List;

/**
 * 博文任务
 * Created by ChenRui on 2017/7/27 0027 15:36.
 */
public class BlogContentJob extends AsyncDownloadJob {


    private final IBlogApi mBlogApi;
    private final INewsApi mNewsApi;
    private DbBlog mDbBlog;

    public BlogContentJob(Context context) {
        mBlogApi = CnblogsApiFactory.getInstance(context).getBlogApi();
        mNewsApi = CnblogsApiFactory.getInstance(context).getNewsApi();
        mDbBlog = DbFactory.getInstance().getBlog();
    }

    @Override
    public void run() {
        // 查询没有内容的博客
        List<BlogBean> blogs = mDbBlog.findAllWithoutBlogContent();

        for (BlogBean blog : blogs) {
            Log.d("Rae", "初始化任务：" + blog.getBlogId());
            execute(new BlogContentTask(mBlogApi, mNewsApi, mDbBlog, blog));
        }
    }

}
