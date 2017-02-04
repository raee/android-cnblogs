package com.rae.cnblogs.sdk.service.task;

import android.content.Context;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 知识库下载任务
 * Created by ChenRui on 2017/2/1 22:41.
 */
public class KBListTask extends BlogServiceTask {

    public KBListTask(Context context, IServiceTask task) {
        super(context, task);
    }

    @Override
    protected void runTask() {

        IBlogApi blogApi = CnblogsApiFactory.getInstance(mContext).getBlogApi();
        blogApi.setShouldCache(false);
        int pageSize = mConfig.getPageSize();
        final CountDownLatch countDownLatch = new CountDownLatch(pageSize);
        final DbBlog dbBlog = new DbBlog();
        for (int page = 0; page < pageSize; page++) {

            blogApi.getKbArticles(page, new ApiUiArrayListener<Blog>() {
                @Override
                public void onApiFailed(ApiException ex, String msg) {
                    countDownLatch.countDown();
                }

                @Override
                public void onApiSuccess(List<Blog> data) {
                    // 保存到列表中
                    dbBlog.addAll(data);
                    countDownLatch.countDown();
                }
            });
        }

    }

    @Override
    public String getTaskName() {
        return "知识库下载任务";
    }
}
