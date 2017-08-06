package com.rae.cnblogs.service.job;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.service.task.BlogContentTask;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 博文任务
 * Created by ChenRui on 2017/7/27 0027 15:36.
 */
public class BlogContentJob extends AsyncDownloadJob {


    private final IBlogApi mBlogApi;
    private final INewsApi mNewsApi;
    private final ConnectivityManager mConnectivityManager;
    private DbBlog mDbBlog;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            performJob();
            return false;
        }
    });

    public BlogContentJob(Context context) {
        mBlogApi = CnblogsApiFactory.getInstance(context).getBlogApi();
        mNewsApi = CnblogsApiFactory.getInstance(context).getNewsApi();
        mDbBlog = DbFactory.getInstance().getBlog();
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void run() {
        // 延期执行，避免过多的刷新操作
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 10000);
    }

    @Override
    public void cancel() {
        super.cancel();
        mHandler.removeMessages(0);
        mHandler = null;
    }

    /**
     * 执行任务
     */
    private void performJob() {
        // 查询没有内容的博客
        Observable.just(mDbBlog.findAllWithoutBlogContent())
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<BlogBean>, ObservableSource<BlogBean>>() {
                    @Override
                    public ObservableSource<BlogBean> apply(List<BlogBean> blogBeans) throws Exception {
                        return Observable.fromIterable(blogBeans).observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Consumer<BlogBean>() {
                    @Override
                    public void accept(BlogBean blog) throws Exception {
                        execute(new BlogContentTask(mConnectivityManager, mBlogApi, mNewsApi, mDbBlog, blog.getBlogId()));
                    }
                });
    }
}
