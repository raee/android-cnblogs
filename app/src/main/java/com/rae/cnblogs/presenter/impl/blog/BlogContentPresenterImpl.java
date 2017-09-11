package com.rae.cnblogs.presenter.impl.blog;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.presenter.impl.BasePresenter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BookmarksBean;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 博客查看实现
 * Created by ChenRui on 2016/12/7 22:03.
 */
public class BlogContentPresenterImpl extends BasePresenter<IBlogContentPresenter.IBlogContentView> implements IBlogContentPresenter {

    protected IBlogApi mBlogApi;
    private IBookmarksApi mBookmarksApi;
    private UserBlogInfo mBlogInfo; // 博客信息
    private DbBlog mDbBlog;

    public BlogContentPresenterImpl(Context context, IBlogContentView view) {
        super(context, view);
        mBlogApi = getApiProvider().getBlogApi();
        mBookmarksApi = getApiProvider().getBookmarksApi();
        mDbBlog = DbFactory.getInstance().getBlog();
    }


    @Override
    public void loadContent() {
        if (mView.getBlog() == null) {
            mView.onLoadContentFailed("该博客不存在");
            return;
        }

        // 1、先从缓存中加载博客信息
        RxObservable.newThread()
                .flatMap(new Function<Integer, ObservableSource<UserBlogInfo>>() {
                    @Override
                    public ObservableSource<UserBlogInfo> apply(Integer integer) throws Exception {
                        UserBlogInfo blogInfo = mDbBlog.get(mView.getBlog().getBlogId());
                        if (blogInfo == null || blogInfo.getBlogId() == null) {
                            blogInfo = new UserBlogInfo();
                            blogInfo.setBlogId(mView.getBlog().getBlogId());
                            blogInfo.setBlogType(mView.getBlog().getBlogType());
                        }
                        return createObservable(Observable.just(blogInfo));
                    }
                })
                .flatMap(new Function<UserBlogInfo, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(UserBlogInfo blogInfo) throws Exception {
                        mBlogInfo = blogInfo;

                        // 加载用户博客信息成功
                        mView.onLoadBlogInfoSuccess(blogInfo);

                        // 2、缓存没有则去网络下载
                        if (TextUtils.isEmpty(blogInfo.getContent())) {
                            return createContentObservable(blogInfo.getBlogId());
                        }
                        // 从缓存加载
                        return createObservable(Observable.just(blogInfo.getContent()));
                    }
                })
                // 3、缓存/网络加载成功后回调到UI
                .subscribe(new ApiDefaultObserver<String>() {
                    @Override
                    protected void onError(String message) {
                        // fix bugly #374
                        if (mView == null) return;
                        mView.onLoadContentFailed(message);
                    }

                    @Override
                    protected void accept(String content) {
                        if (mView == null) return;
                        mView.getBlog().setContent(content);
                        mView.onLoadContentSuccess(content);

                        // 4、异步更新用户博客信息
                        updateUserBlogInfo(content);
                    }

                    /**
                     * 更新用户博客信息
                     * @param content 内容
                     */
                    private void updateUserBlogInfo(String content) {
                        mBlogInfo.setContent(content);
                        mBlogInfo.setRead(true);

                        Observable.just(mBlogInfo)
                                .subscribeOn(Schedulers.io())
                                .subscribe(new ApiDefaultObserver<UserBlogInfo>() {
                                    @Override
                                    protected void onError(String message) {
                                        // 更新失败不处理
                                    }

                                    @Override
                                    protected void accept(UserBlogInfo content) {
                                        mBlogInfo.save();
                                    }
                                });
                    }
                });
    }

    protected ObservableSource<String> createContentObservable(String blogId) {
        return createObservable(mBlogApi.getBlogContent(blogId));
    }

    @Override
    public void doLike(final boolean isCancel) {
        BlogBean blog = mView.getBlog();

        Observable<Empty> observable;
        if (isCancel) {
            observable = mBlogApi.unLikeBlog(blog.getBlogId(), blog.getBlogApp());
        } else {
            observable = mBlogApi.likeBlog(blog.getBlogId(), blog.getBlogApp());
        }

        createObservable(isCancel, observable, true);
    }

    @Override
    public void doBookmarks(boolean isCancel) {


        BlogBean blog = mView.getBlog();
        BookmarksBean m = new BookmarksBean(blog.getTitle(), blog.getSummary(), blog.getUrl());
        Observable<Empty> observable;
        if (isCancel) {
            mView.onBookmarksError(true, "请到我的收藏里面取消收藏");
            return;
//            observable = mBookmarksApi.delBookmarks(blog.getUrl());
        }

        observable = mBookmarksApi.addBookmarks(m.getTitle(), m.getSummary(), m.getLinkUrl());
        createObservable(false, observable, false);
    }

    @Override
    public void reloadBookmarkStatus() {

        BlogBean blog = mView.getBlog();
        if (blog == null) return;

        // 获取用户的博客信息
        mBlogInfo = mDbBlog.get(blog.getBlogId());
        if (mBlogInfo != null) {
            mView.onLoadBlogInfoSuccess(mBlogInfo);
        }
    }

    protected void createObservable(final boolean isCancel, Observable<Empty> observable, final boolean isLike) {
        createObservable(observable).subscribe(new ApiDefaultObserver<Empty>() {
            @Override
            protected void onError(String msg) {
                if (!TextUtils.isEmpty(msg) && (msg.contains("登录") || msg.contains("Authorization"))) {
                    mView.onNeedLogin();
                    return;
                }

                // 新闻推荐过了
                if (msg.contains("您已经推荐过")) {
                    mView.onLikeSuccess(isCancel);
                    return;
                }

                if (msg.contains("网摘")) {
                    // 保存到数据库
                    if (mBlogInfo != null) {
                        mBlogInfo.setBookmarks(true);
                        mDbBlog.saveBlogInfo(mBlogInfo);
                    }
                    mView.onBookmarksSuccess(false);
                    return;
                }

                if (isLike) {
                    mView.onLikeError(isCancel, msg);
                } else {
                    mView.onBookmarksError(isCancel, msg);
                }
            }

            @Override
            protected void accept(Empty empty) {

                if (isLike) {
                    mView.onLikeSuccess(isCancel);
                    mBlogInfo.setLiked(!isCancel);

                } else {
                    mView.onBookmarksSuccess(isCancel);
                    mBlogInfo.setBookmarks(!isCancel);

                }

                // 更新信息
                mDbBlog.saveBlogInfo(mBlogInfo);
            }
        });
    }
}
