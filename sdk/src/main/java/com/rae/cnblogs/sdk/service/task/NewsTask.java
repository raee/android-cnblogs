package com.rae.cnblogs.sdk.service.task;

import android.content.Context;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 新闻下载任务
 * Created by ChenRui on 2017/2/1 22:41.
 */
public class NewsTask extends BlogServiceTask {

    public NewsTask(Context context, IServiceTask task) {
        super(context, task);
    }

    @Override
    protected void runTask() {

        INewsApi newsApi = CnblogsApiFactory.getInstance(mContext).getNewsApi();
        newsApi.setShouldCache(false);
        int pageSize = mConfig.getPageSize();
        final CountDownLatch countDownLatch = new CountDownLatch(pageSize);
        final DbBlog dbBlog = new DbBlog();
        for (int page = 0; page < pageSize; page++) {

            newsApi.getNews(page, new ApiUiArrayListener<BlogBean>() {
                @Override
                public void onApiFailed(ApiException ex, String msg) {
                    countDownLatch.countDown();
                }

                @Override
                public void onApiSuccess(List<BlogBean> data) {
                    // 保存到列表中
                    dbBlog.addAll(data);
                    countDownLatch.countDown();
                }
            });
        }

    }

    @Override
    public String getTaskName() {
        return "新闻下载任务";
    }
}
