package com.rae.cnblogs.sdk.service.task;

import android.content.Context;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbCategory;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.core.Rae;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 博客列表下载任务
 * 任务描述：
 * 下载所有用户可用分类列表的分页数据
 * Created by ChenRui on 2017/2/1 22:22.
 */
public class BlogListTask extends BlogServiceTask {

    public BlogListTask(Context context, IServiceTask task) {
        super(context, task);
    }

    @Override
    protected void runTask() {
        IBlogApi blogApi = CnblogsApiFactory.getInstance(mContext).getBlogApi();
        DbCategory db = DbFactory.getInstance().getCategory();
        List<CategoryBean> categories = db.getUserList();
        if (Rae.isEmpty(categories)) {
            return;
        }

        int pageSize = mConfig.getPageSize();
        final CountDownLatch countDownLatch = new CountDownLatch(pageSize * categories.size());
        final DbBlog dbBlog = DbFactory.getInstance().getBlog();

        // 遍历分类
        for (CategoryBean m : categories) {
            // 页码
            for (int i = 0; i < pageSize; i++) {
                blogApi.getBlogList(i, m.getType(), m.getParentId(), m.getCategoryId(), new ApiUiArrayListener<BlogBean>() {
                    @Override
                    public void onApiFailed(ApiException ex, String msg) {
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onApiSuccess(List<BlogBean> data) {
                        // 保存到数据库中
                        dbBlog.addAll(data);
                        countDownLatch.countDown();
                    }
                });
            }
        }

        try {
            // 等待
            countDownLatch.await(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTaskName() {
        return "博客列表下载任务";
    }

}
