package com.rae.cnblogs.dialog;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.bean.Blog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分享对话框
 * Created by ChenRui on 2016/12/7 22:15.
 */
public class BlogShareDialog extends SlideDialog {

    private final Blog mBlog;

    @BindView(R.id.tv_share_wechat)
    View mWeChatView;
    @BindView(R.id.tv_share_wechat_sns)
    View mWeChatSNSView;
    @BindView(R.id.tv_share_qq)
    View mQQView;
    @BindView(R.id.tv_share_qzone)
    View mQzoneView;
    @BindView(R.id.tv_share_sina)
    View mSinaView;

    @BindView(R.id.tv_share_source)
    View mViewSourceView;

    @BindView(R.id.tv_share_link)
    View mLinkView;

    @BindView(R.id.tv_share_browser)
    View mBrowseriew;

    private ShareAction mShareAction;


    public BlogShareDialog(Context context, Blog blog) {
        super(context);
        mBlog = blog;
        setContentView(R.layout.dialog_blog_content);
        ButterKnife.bind(this, this);

        mShareAction = new ShareAction((Activity) context);
        mShareAction.withTitle(blog.getTitle());
        mShareAction.withText(blog.getSummary());
        mShareAction.withTargetUrl(blog.getUrl());
        if (!TextUtils.isEmpty(blog.getAvatar())) {
            mShareAction.withMedia(new UMImage(getContext(), blog.getAvatar()));
        } else {
            mShareAction.withMedia(new UMImage(getContext(), R.drawable.ic_share));
        }
    }

    // 开始动画
    private void startAnim() {

        List<View> views = new ArrayList<>();
        views.add(mWeChatView);
        views.add(mWeChatSNSView);
        views.add(mQQView);
        views.add(mQzoneView);
        views.add(mSinaView);
        startAnimSet(views);

        views.clear();
        views.add(mViewSourceView);
        views.add(mLinkView);
        views.add(mBrowseriew);
        startAnimSet(views);
        views.clear();


    }

    // 开始动画效果
    private void startAnimSet(List<View> views) {
        long afterTime = 100;
        for (View view : views) {
            afterTime += 100;

            AnimationSet set = new AnimationSet(false);
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0);
            animation.setDuration(800);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setStartOffset(afterTime);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1.0f);
            alphaAnimation.setDuration(300);
            alphaAnimation.setStartOffset(afterTime);

            set.addAnimation(animation);
            set.addAnimation(alphaAnimation);
            view.startAnimation(set);
        }
    }

    @Override
    public void show() {
        super.show();
        startAnim();
    }

    private void share(SHARE_MEDIA type) {
        mShareAction.setPlatform(type);
        mShareAction.share();
    }

    @OnClick({R.id.tv_share_wechat, R.id.tv_share_wechat_sns, R.id.tv_share_qq, R.id.tv_share_qzone, R.id.tv_share_sina, R.id.tv_share_source})
    void onShareClick(View view) {
        switch (view.getId()) {
            case R.id.tv_share_wechat:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.tv_share_wechat_sns:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.tv_share_qq:
                share(SHARE_MEDIA.QQ);
                break;
            case R.id.tv_share_qzone:
                share(SHARE_MEDIA.QZONE);
                break;
            case R.id.tv_share_sina:
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.tv_share_source:
                onViewSourceClick();
                break;
        }

        dismiss();
    }

    // 查看原文
    protected void onViewSourceClick() {
    }

    @OnClick(R.id.tv_share_browser)
    void onBrowserViewClick() {
        if (mBlog == null) return;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getUrl()));
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dismiss();
    }

    protected String getUrl() {
        return mBlog == null ? null : mBlog.getUrl();
    }


    // 复制链接
    @OnClick(R.id.tv_share_link)
    void onLinkClick() {
        if (mBlog == null) return;
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("url", mBlog.getUrl()));
        AppUI.success(getContext(), R.string.copy_link_success);
        dismiss();
    }

    @OnClick(R.id.btn_share_cancel)
    void onCancelClick() {
        dismiss();
    }
}
