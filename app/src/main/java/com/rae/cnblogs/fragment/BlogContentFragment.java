package com.rae.cnblogs.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.BuildConfig;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeAnim;
import com.rae.cnblogs.ThemeCompat;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;
import com.rae.cnblogs.dialog.impl.HintCardDialog;
import com.rae.cnblogs.message.FontChangedEvent;
import com.rae.cnblogs.message.ThemeChangedEvent;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.sdk.AppGson;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;
import com.rae.cnblogs.widget.ImageLoadingView;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeWebView;
import com.rae.cnblogs.widget.webclient.RaeJavaScriptBridge;
import com.rae.cnblogs.widget.webclient.RaeWebViewClient;
import com.rae.swift.Rx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.InputStream;

import butterknife.BindView;


/**
 * 博文内容
 * Created by ChenRui on 2016/12/6 23:39.
 */
public class BlogContentFragment extends WebViewFragment implements IBlogContentPresenter.IBlogContentView, View.OnClickListener {

    @BindView(R.id.view_holder)
    PlaceholderView mPlaceholderView;

    private ImageView mBackView;
    private ImageView mMoreView;

    private TextView mLikeView;
    private ImageLoadingView mBookmarksView;
    private ImageLoadingView mLikeAnimView; // 点赞做动画的视图
    private BlogType mBlogType;
    private int mSourceTextZoom; // 刚进来的字体大小

    public static BlogContentFragment newInstance(BlogBean blog, BlogType type) {
        Bundle args = new Bundle();
        args.putParcelable("blog", blog);
        args.putString("type", type.getTypeName());
        BlogContentFragment fragment = new BlogContentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private BlogBean mBlog;
    private IBlogContentPresenter mContentPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBlog = getArguments().getParcelable("blog");
            mBlogType = BlogType.typeOf(getArguments().getString("type"));
            mContentPresenter = CnblogsPresenterFactory.getBlogContentPresenter(getContext(), mBlogType, this);
        }

        EventBus.getDefault().register(this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSourceTextZoom = mWebView.getSettings().getTextZoom();
        if (BuildConfig.DEBUG) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            enablePullToRefresh(false);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        // 设置字体大小
        initFontSize();

        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 重试
                mPlaceholderView.loading();
                mContentPresenter.loadContent();
            }
        });

        final int transparentId = ContextCompat.getColor(getContext(), android.R.color.transparent);

        mWebView.setOnScrollChangeListener(new RaeWebView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(int x, int y, int oldX, int oldY) {
                if (mBackView == null || mMoreView == null) return;
                if (y <= 0) {
                    // 显示默认的
                    mBackView.setBackgroundColor(transparentId);
                    mMoreView.setBackgroundColor(transparentId);

                    mBackView.setImageResource(R.drawable.ic_back);
                    mMoreView.setImageResource(R.drawable.ic_action_bar_more);
                } else {
                    int bgResId = R.drawable.bg_blog_content_back;
                    mBackView.setBackgroundResource(bgResId);
                    mMoreView.setBackgroundResource(bgResId);
                    mBackView.setImageResource(R.drawable.ic_back_white);
                    mMoreView.setImageResource(R.drawable.ic_blog_content_more);
                }

            }
        });
    }

    private void initFontSize() {
        if (mSourceTextZoom <= 0) {
            mSourceTextZoom = mWebView.getSettings().getTextZoom();
        }
        int pageTextSize = config().getPageTextSize();
        if (pageTextSize > 0) {
            // 默认字体大小
            int defaultTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics());
            int zoom = mSourceTextZoom * pageTextSize / defaultTextSize;
            mWebView.getSettings().setTextZoom(zoom);
        }
    }

    @Override
    public Object getJavascriptApi() {
        return new RaeJavaScriptBridge(getContext()) {
            @JavascriptInterface
            public String getBlog() {
                return AppGson.get().toJson(mBlog);
            }


        };
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mContentPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mBlog == null) return;
        mLikeView = (TextView) getActivity().findViewById(R.id.tv_like_badge);
        mBookmarksView = (ImageLoadingView) getActivity().findViewById(R.id.img_content_bookmarks);
        mLikeAnimView = (ImageLoadingView) getActivity().findViewById(R.id.img_content_like);
        getActivity().findViewById(R.id.ll_like).setOnClickListener(this); // 点赞布局
        getActivity().findViewById(R.id.ll_content_bookmarks).setOnClickListener(this); // 收藏布局
        mContentPresenter.loadContent();

        mBackView = getActivity().findViewById(R.id.back);
        mMoreView = getActivity().findViewById(R.id.img_action_bar_more);

//        // 测试一下
//        final ViewCaptureUtils viewCaptureUtils = new ViewCaptureUtils(getContext());
//        mBackView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                viewCaptureUtils.capture(mWebView, "/sdcard/test.jpg")
//                        .subscribe(new Consumer<File>() {
//                            @Override
//                            public void accept(File file) throws Exception {
//                                AppUI.toast(getContext(), "保存成功！");
//                            }
//                        });
//            }
//        });
    }

    @Override
    public BlogBean getBlog() {
        return mBlog;
    }

    @Override
    public void onLoadContentSuccess(String content) {
        // 可能会处于非主线程中，这里提交到主线程中去。
        // fix bugly #354 #562
        mContentLayout.post(new Runnable() {
            @Override
            public void run() {
                mPlaceholderView.dismiss();
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                try {
                    // 避免切换夜间模式闪烁问题
                    if (isDetached() || getContext() == null || !isAdded() || !isVisible())
                        return; // fix bug #638
                    InputStream stream = getResources().getAssets().open("view.html");
                    byte[] data = new byte[stream.available()];
                    stream.read(data);
                    stream.close();
                    String content = new String(data).replace("{{theme}}", ThemeCompat.isNight() ? "rae-night.css" : "rae.css");
                    mWebView.loadDataWithBaseURL("file:///android_asset/view.html", content, "text/html", "UTF-8", null);
                } catch (Throwable e) {
                    e.printStackTrace();
                    // 如果加载失败了，就从默认打开
                    mWebView.loadUrl("file:///android_asset/view.html");
                }
            }
        });

    }

    @Override
    public void onLoadContentFailed(String msg) {
        mPlaceholderView.retry(msg);
    }

    @Override
    public void onLikeError(boolean isCancel, String msg) {
        mLikeView.setEnabled(true);
        mLikeAnimView.stop();
        mLikeView.setVisibility(View.VISIBLE);
        mLikeAnimView.setVisibility(View.GONE);

        RaeAnim.scaleIn(mLikeView);
        AppUI.toastInCenter(getContext(), msg);
    }

    @Override
    public void onLikeSuccess(boolean isCancel) {
        mLikeView.setEnabled(true);
        mLikeView.setSelected(!isCancel);
        int like = Rx.parseInt(mBlog.getLikes());

        if (mLikeView.isSelected()) {
            // 点赞数量加1
            mLikeView.setText(String.valueOf(like + 1));
            mLikeAnimView.anim(new Runnable() {
                @Override
                public void run() {
                    mLikeAnimView.setVisibility(View.GONE);
                    mLikeView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mLikeView.setText(like <= 0 ? "" : String.valueOf(like));
            mLikeView.setVisibility(View.VISIBLE);
            mLikeAnimView.setVisibility(View.GONE);
        }


        if (!config().hasLikeGuide()) {
            HintCardDialog dialog = new HintCardDialog(getContext());
            dialog.setMessage(getString(R.string.dialog_tips_post_like));
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    config().likeGuide();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onBookmarksError(boolean isCancel, String msg) {
        mBookmarksView.setEnabled(true);
        mBookmarksView.stop();
        RaeAnim.scaleIn(mBookmarksView);

        // 不能取消收藏
        if (isCancel) {
            HintCardDialog dialog = new HintCardDialog(getContext());
            dialog.setTitle(getString(R.string.cancel_bookmarks_title));
            dialog.setMessage(msg);
            dialog.setEnSureText(getString(R.string.go_now));
            dialog.setOnEnSureListener(new IAppDialogClickListener() {
                @Override
                public void onClick(IAppDialog dialog, int buttonType) {
                    dialog.dismiss();
                    AppRoute.jumpToFavorites(getActivity());
                }
            });
            dialog.showCloseButton();
            dialog.show();
            return;
        }

        AppUI.toastInCenter(getContext(), msg);
    }

    @Override
    public void onBookmarksSuccess(boolean isCancel) {
        mBookmarksView.setEnabled(true);
        mBookmarksView.setSelected(!isCancel);
        mBookmarksView.anim();
//        RaeAnim.scaleIn(mBookmarksView);
    }

    @Override
    public void onNeedLogin() {
        mLikeView.setEnabled(true);
        mBookmarksView.setEnabled(true);
        mBookmarksView.stop();
        mLikeAnimView.stop();
        mLikeAnimView.setVisibility(View.GONE);
        mLikeView.setVisibility(View.VISIBLE);
        if (getContext() != null)
            AppRoute.routeToLogin(getContext());
    }

    @Override
    public void onLoadBlogInfoSuccess(UserBlogInfo infoModel) {
        // 角标处理
        mLikeView.setSelected(infoModel.isLiked());
        mBookmarksView.setSelected(infoModel.isBookmarks());
    }

    @Override
    public BlogType getBlogType() {
        return mBlogType;
    }

    @Override
    public WebViewClient getWebViewClient() {
        return new RaeWebViewClient(mProgressBar, mAppLayout) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                AppRoute.routeToWebNewTask(view.getContext(), url);
                return true;
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_like:  // 点赞
                mLikeView.setEnabled(false);
                mLikeView.setVisibility(View.GONE);
                mLikeAnimView.setVisibility(View.VISIBLE);
                mLikeAnimView.loading();
                mContentPresenter.doLike(mLikeView.isSelected());
                break;

            case R.id.ll_content_bookmarks:  // 收藏
                mBookmarksView.setEnabled(false);
                mBookmarksView.loading();
                mContentPresenter.doBookmarks(mBookmarksView.isSelected());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 删除收藏回来要刷新
        if (resultCode == Activity.RESULT_OK && requestCode == AppRoute.REQ_CODE_FAVORITES) {
            mContentPresenter.reloadBookmarkStatus();
        }
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        mWebView.scrollTo(0, 0);
    }

    @Subscribe
    public void onEvent(ThemeChangedEvent event) {
        mWebView.loadUrl("javascript:loadTheme(" + event.isNight() + ")");
    }

    @Subscribe
    public void onEvent(FontChangedEvent event) {
        initFontSize();
        mWebView.reload();
    }
}
