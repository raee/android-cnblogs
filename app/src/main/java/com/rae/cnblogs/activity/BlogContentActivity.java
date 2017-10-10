package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.dialog.DialogProvider;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.impl.BlogShareDialog;
import com.rae.cnblogs.fragment.BlogCommentFragment;
import com.rae.cnblogs.fragment.BlogContentFragment;
import com.rae.cnblogs.message.EditCommentEvent;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeDrawerLayout;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 博文查看
 * Created by ChenRui on 2016/12/6 21:38.
 */
public class BlogContentActivity extends SwipeBackBaseActivity {

//    @BindView(R.id.tool_bar)
//    Toolbar mToolbar;

//    @BindView(R.id.img_blog_avatar)
//    ImageView mAvatarView;

//    @BindView(R.id.tv_blog_author)
//    TextView mAuthorView;

    @BindView(R.id.tv_comment_badge)
    TextView mCommentBadgeView;

    @BindView(R.id.tv_like_badge)
    TextView mLikeBadgeView;

    @BindView(R.id.fl_content)
    View mContentLayout;

    @BindView(R.id.tv_edit_comment)
    View mPostCommentView;

    @BindView(R.id.layout_content_comment)
    View mViewCommentView;

    @BindView(R.id.fl_comment)
    RaeDrawerLayout mCommentLayout;

    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholderView;

    private BlogShareDialog mShareDialog;
    private BlogBean mBlog;
    private BlogType mBlogType;
    private BlogCommentFragment mBlogCommentFragment;
    private BlogContentFragment mBlogContentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_content);
//        setSupportActionBar(mToolbar);
//        showHomeAsUp(mToolbar);

        final String blogId = getIntent().getStringExtra("blogId");
        mBlog = getIntent().getParcelableExtra("blog");
        mBlogType = BlogType.typeOf(getIntent().getStringExtra("type"));

        if (TextUtils.isEmpty(blogId) && mBlog == null) {
            AppUI.toast(this, "博客为空！");
            finish();
            return;
        }

        mShareDialog = new BlogShareDialog(this) {
            @Override
            protected void onViewSourceClick() {
                AppRoute.jumpToWeb(getContext(), mBlog.getUrl());
            }
        };

        if (mBlog != null) {
            mPlaceholderView.dismiss();
            onLoadData(mBlog);
            // 加载博客摘要
            createBlogObservable(mBlog.getBlogId())
                    .subscribe(new ApiDefaultObserver<BlogBean>() {
                        @Override
                        protected void onError(String message) {
                            mBlog.setSummary(mBlog.getTitle());
                        }

                        @Override
                        protected void accept(BlogBean blogBean) {
                            mBlog.setSummary(blogBean.getSummary());
                        }
                    });
        }
        // 根据blogId 获取博客信息
        else if (!TextUtils.isEmpty(blogId)) {
            loadBlogFromDatabase(blogId);
        } else {
            mPlaceholderView.empty("博客不存在");
        }

    }

    /**
     * 从数据库加载博文
     *
     * @param blogId 博客ID
     */
    private void loadBlogFromDatabase(final String blogId) {
        createBlogObservable(blogId)
                .subscribe(new ApiDefaultObserver<BlogBean>() {
                    @Override
                    protected void onError(String message) {
                        IAppDialog dialog = DialogProvider.create(getContext());
                        dialog.setMessage("博客加载失败，请退出后刷新列表重试。");
                        dialog.show();
                        mPlaceholderView.empty(message);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NullPointerException) {
                            mPlaceholderView.empty("博客不存在，可能由于缓存已经被清除，请返回首页列表刷新后再重新进入吧。");
                            return;
                        }
                        super.onError(e);
                    }

                    @Override
                    protected void accept(BlogBean blog) {
                        mPlaceholderView.dismiss();
                        onLoadData(blog);
                    }
                });
    }

    private Observable<BlogBean> createBlogObservable(final String blogId) {
        return RxObservable.newThread()
                .flatMap(new Function<Integer, ObservableSource<BlogBean>>() {
                    @Override
                    public ObservableSource<BlogBean> apply(@NonNull Integer integer) throws Exception {
                        // 从数据库加载
                        DbBlog db = DbFactory.getInstance().getBlog();
                        BlogBean blog = db.getBlog(blogId);
                        if (blog == null) {
                            throw new NullPointerException();
                        }
                        return RxObservable.create(Observable.just(blog), "blogContent").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void onLoadData(BlogBean blog) {
        mBlog = blog;

        // 从搜索进来的标题处理
        String text = Jsoup.parse(blog.getTitle()).text();
        mBlog.setTitle(text);


        // 评论角标
        if (!TextUtils.equals(mBlog.getComment(), "0")) {
            mCommentBadgeView.setText(mBlog.getComment());
            mCommentBadgeView.setSelected(true);
            mCommentBadgeView.setVisibility(View.VISIBLE);
        }

        // 点赞角标
        if (!TextUtils.equals(mBlog.getLikes(), "0")) {
            mLikeBadgeView.setText(mBlog.getLikes());
//            mLikeBadgeView.setBackgroundResource(R.drawable.ic_like_content_badge);
            mLikeBadgeView.setVisibility(View.VISIBLE);
        }

        // 知识库没有评论处理
        if (mBlogType == BlogType.KB || (mBlogType == BlogType.NEWS && TextUtils.isEmpty(mBlog.getAuthor()))) {
            mPostCommentView.setVisibility(View.GONE);
            mViewCommentView.setVisibility(View.GONE);
//            mAuthorView.setVisibility(View.GONE);
//            mAvatarView.setVisibility(View.GONE);
        }
//        else {
//            RaeImageLoader.displayHeaderImage(mBlog.getAvatar(), mAvatarView);
//            mAuthorView.setText(mBlog.getAuthor());
//        }


        // 评论
        mBlogCommentFragment = BlogCommentFragment.newInstance(mBlog, mBlogType);
        // 内容
        mBlogContentFragment = BlogContentFragment.newInstance(mBlog, mBlogType);


        // 加载Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_comment, mBlogCommentFragment);
        transaction.add(R.id.fl_content, mBlogContentFragment);
        // fix bugly #472
        transaction.commitAllowingStateLoss();
    }

    // 分享
    @OnClick(R.id.img_action_bar_more)
    public void onActionMenuMoreClick() {
        mShareDialog.show(mBlog);
    }

    // 查看评论
    @OnClick(R.id.layout_content_comment)
    public void onCommentClick() {
        mCommentLayout.toggleSmoothScroll();
    }

    // 发表评论
    @OnClick(R.id.tv_edit_comment)
    public void onEditCommentClick() {
        // 通知里面的评论打开发表对话框
        EventBus.getDefault().post(new EditCommentEvent());
    }

//    // 作者头像
//    @OnClick({R.id.img_blog_avatar, R.id.tv_blog_author})
//    public void onBloggerClick() {
//        if (mBlog == null) return;
//        if (mBlogType != BlogType.BLOG) {
//            return;
//        }
//
//        AppRoute.jumpToBlogger(this, mBlog.getBlogApp());
//    }

    @OnClick(R.id.tool_bar)
    public void onActionBarClick() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null || fragments.size() < 1) {
            return;
        }

        if (mCommentLayout.getVisibility() == View.VISIBLE) {
            mBlogCommentFragment.scrollToTop();
        } else {
            mBlogContentFragment.scrollToTop();
        }
    }


    // 返回键处理
    @Override
    public void onBackPressed() {
        if (mCommentLayout.getVisibility() == View.VISIBLE) {
            mCommentLayout.toggleSmoothScroll();
            return;
        }
        super.onBackPressed();
    }
}
