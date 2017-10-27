package com.rae.cnblogs.presenter;

import android.content.Context;

import com.rae.cnblogs.presenter.impl.BloggerPresenterImpl;
import com.rae.cnblogs.presenter.impl.FeedPresenterImpl;
import com.rae.cnblogs.presenter.impl.HomePresenterImpl;
import com.rae.cnblogs.presenter.impl.LauncherPresenterImpl;
import com.rae.cnblogs.presenter.impl.LoginPresenterImpl;
import com.rae.cnblogs.presenter.impl.MomentPresenterImpl;
import com.rae.cnblogs.presenter.impl.PostMomentPresenterImpl;
import com.rae.cnblogs.presenter.impl.SearchPresenterImpl;
import com.rae.cnblogs.presenter.impl.blog.BlogCommentPresenterImpl;
import com.rae.cnblogs.presenter.impl.blog.BlogContentPresenterImpl;
import com.rae.cnblogs.presenter.impl.blog.BlogListPresenterImpl;
import com.rae.cnblogs.presenter.impl.blog.BloggerListPresenterImpl;
import com.rae.cnblogs.presenter.impl.blog.SearchBlogPresenterImpl;
import com.rae.cnblogs.presenter.impl.kb.KBContentPresenterImpl;
import com.rae.cnblogs.presenter.impl.kb.KBListPresenterImpl;
import com.rae.cnblogs.presenter.impl.kb.KbCommentPresenterImpl;
import com.rae.cnblogs.presenter.impl.news.NewsCommentPresenterImpl;
import com.rae.cnblogs.presenter.impl.news.NewsContentPresenterImpl;
import com.rae.cnblogs.presenter.impl.news.NewsListPresenterImpl;
import com.rae.cnblogs.sdk.bean.BlogType;

/**
 * Created by ChenRui on 2016/12/2 00:23.
 */
public final class CnblogsPresenterFactory {

    /**
     * 首页
     */
    public static IHomePresenter getHomePresenter(Context context, IHomePresenter.IHomeView view) {
        return new HomePresenterImpl(context, view);
    }

    /**
     * 首页博客列表
     */
    public static IBlogListPresenter getBlogListPresenter(Context context, BlogType type, IBlogListPresenter.IBlogListView view) {
        IBlogListPresenter presenter;
        switch (type) {
            case NEWS:
                presenter = getNewsListPresenter(context, view);
                break;
            case KB:
                presenter = getKbListPresenter(context, view);
                break;
            case BLOGGER:
                presenter = new BloggerListPresenterImpl(context, view);
                break;
            case UNKNOWN:
            case BLOG:
            default:
                presenter = new BlogListPresenterImpl(context, view);
                break;
        }

        return presenter;
    }

    /**
     * 新闻列表
     */
    private static IBlogListPresenter getNewsListPresenter(Context context, IBlogListPresenter.IBlogListView view) {
        return new NewsListPresenterImpl(context, view);
    }

    /**
     * 知识库列表
     */
    private static IBlogListPresenter getKbListPresenter(Context context, IBlogListPresenter.IBlogListView view) {
        return new KBListPresenterImpl(context, view);
    }

    /**
     * 博文
     */
    public static IBlogContentPresenter getBlogContentPresenter(Context context, BlogType type, IBlogContentPresenter.IBlogContentView view) {
        IBlogContentPresenter presenter;
        switch (type) {
            case NEWS:
                presenter = getNewsContentPresenter(context, view);
                break;
            case KB:
                presenter = getKbContentPresenter(context, view);
                break;
            case UNKNOWN:
            case BLOG:
            default:
                presenter = new BlogContentPresenterImpl(context, view);
                break;
        }

        return presenter;
    }

    private static IBlogContentPresenter getKbContentPresenter(Context context, IBlogContentPresenter.IBlogContentView view) {
        return new KBContentPresenterImpl(context, view);
    }

    private static IBlogContentPresenter getNewsContentPresenter(Context context, IBlogContentPresenter.IBlogContentView view) {
        return new NewsContentPresenterImpl(context, view);
    }

    /**
     * 启动页
     */
    public static ILauncherPresenter getLauncherPresenter(Context context, ILauncherPresenter.ILauncherView view) {
        return new LauncherPresenterImpl(context, view);
    }

    /**
     * 评论
     */
    public static IBlogCommentPresenter getBlogCommentPresenter(Context context, BlogType blogType, IBlogCommentPresenter.IBlogCommentView view) {
        IBlogCommentPresenter presenter = null;
        switch (blogType) {
            case NEWS:
                presenter = getNewsCommentPresenter(context, view);
                break;
            case KB:
                presenter = getKbCommentPresenter(context, view);
                break;
            case UNKNOWN:
            case BLOG:
            default:
                presenter = new BlogCommentPresenterImpl(context, view);
                break;
        }
        return presenter;
    }

    private static IBlogCommentPresenter getKbCommentPresenter(Context context, IBlogCommentPresenter.IBlogCommentView view) {
        return new KbCommentPresenterImpl(context, view);
    }

    private static IBlogCommentPresenter getNewsCommentPresenter(Context context, IBlogCommentPresenter.IBlogCommentView view) {
        return new NewsCommentPresenterImpl(context, view);
    }

    /**
     * 登录
     */
    public static ILoginPresenter getLoginPresenter(Context context, ILoginPresenter.ILoginView view) {
        return new LoginPresenterImpl(context, view);
    }

    /**
     * 博主
     */
    public static IBloggerPresenter getBloggerPresenter(Context context, IBloggerPresenter.IBloggerView view) {
        return new BloggerPresenterImpl(context, view);
    }

    /**
     * 动态列表
     */
    public static IFeedPresenter getFeedPresenter(Context context, IFeedPresenter.IFeedView view) {
        return new FeedPresenterImpl(context, view);
    }

    /**
     * 搜索
     */
    public static ISearchContract.Presenter getSearchPresenter(Context context, ISearchContract.View view) {
        return new SearchPresenterImpl(context, view);
    }

    /**
     * 搜索博客列表
     */
    public static IBlogListPresenter getSearchPresenter(Context context, BlogType type, IBlogListPresenter.IBlogListView view) {
        return new SearchBlogPresenterImpl(context, view, type);
    }

    /**
     * 闪存列表
     */
    public static IMomentContract.Presenter getMomentPresenter(Context context, IMomentContract.View view) {
        return new MomentPresenterImpl(context, view);
    }

    /**
     * 闪存列表
     */
    public static IPostMomentContract.Presenter getPostMomentPresenter(Context context, IPostMomentContract.View view) {
        return new PostMomentPresenterImpl(context, view);
    }

}
