package com.rae.cnblogs.sdk.service.task;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;
import com.rae.core.Rae;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 博客内容离线下载任务
 * Created by ChenRui on 2017/2/1 17:01.
 */
public class BlogContentTask extends BlogServiceTask {

    private final Queue<Blog> mBlogQueue = new LinkedList<>(); // 待离线下载列表

    private IBlogApi mBlogApi;
    private final static String TAG = "BlogContentTask";
    private INewsApi mNewsApi;

    public BlogContentTask(Context context, IServiceTask task) {
        super(context, task);
        mBlogApi = CnblogsApiFactory.getInstance(context).getBlogApi();
        mNewsApi = CnblogsApiFactory.getInstance(context).getNewsApi();
    }

    @Override
    public String getTaskName() {
        return "博文离线下载任务";
    }

    @Override
    protected void runTask() {

        // 查询要下载的博客列表
        final DbBlog db = new DbBlog();
        List<Blog> list = db.findAll();
        if (Rae.isEmpty(list)) {
            return;
        }

        for (Blog model : list) {
            // 查找是否需要同步
            UserBlogInfo blogInfo = db.get(model.getBlogId());
            if (blogInfo == null || TextUtils.isEmpty(blogInfo.getContent())) {
                // 添加到队列中去
                mBlogQueue.add(model);
            }
        }


        while (!mBlogQueue.isEmpty()) {
            if (isFinish()) {
                mBlogQueue.clear();
                break;
            }

            // 获取博文内容
            final Blog blog = mBlogQueue.poll();

            // 等待下载完成

            Log.d(TAG, "正在离线下载：" + blog.getBlogType() + blog.getBlogId() + " = " + blog.getTitle());

            ApiListener listener = new ApiListener(blog);

            switch (BlogType.typeOf(blog.getBlogType())) {
                case BLOG:  // 博文
                case UNKNOWN:
                    mBlogApi.getBlogContent(blog.getBlogId(), listener);
                    break;
                case NEWS:  // 新闻
                    mNewsApi.getNewsContent(blog.getBlogId(), listener);
                    break;
                case KB: // 知识库
                    mBlogApi.getKbContent(blog.getBlogId(), listener);
                    break;
            }

            listener.await();

        }

    }

    private class ApiListener implements ApiUiListener<String> {

        private Blog mBlog;
        private CountDownLatch mCountDownLatch;
        private DbBlog mDbBlog;

        ApiListener(Blog blog) {
            mBlog = blog;
            mDbBlog = new DbBlog();
            mCountDownLatch = new CountDownLatch(1);
        }

        @Override
        public void onApiFailed(ApiException ex, String msg) {
            mCountDownLatch.countDown();
        }

        @Override
        public void onApiSuccess(String data) {
            // 保存到数据库中
            UserBlogInfo blogInfo = new UserBlogInfo();
            blogInfo.setContent(data);
            blogInfo.setBlogId(mBlog.getBlogId());
            blogInfo.setBlogType(mBlog.getBlogType());
            mDbBlog.saveBlogInfo(blogInfo);
            mCountDownLatch.countDown();
        }

        void await() {
            try {
                mCountDownLatch.await(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
