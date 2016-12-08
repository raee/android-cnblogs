package com.rae.cnblogs.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.bean.Blog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ChenRui on 2016/12/7 22:15.
 */
public class BlogContentDialog extends SlideDialog {

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


    public BlogContentDialog(Context context, Blog blog) {
        super(context);
        mBlog = blog;
        setContentView(R.layout.dialog_blog_content);
        ButterKnife.bind(this, this);
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

    @OnClick({R.id.tv_share_wechat, R.id.tv_share_wechat_sns, R.id.tv_share_qq, R.id.tv_share_qzone, R.id.tv_share_sina, R.id.tv_share_source})
    void onShareClick(View view) {
        switch (view.getId()) {
            case R.id.tv_share_wechat:
                break;
            case R.id.tv_share_wechat_sns:
                break;
            case R.id.tv_share_qq:
                break;
            case R.id.tv_share_qzone:
                break;
            case R.id.tv_share_sina:
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
