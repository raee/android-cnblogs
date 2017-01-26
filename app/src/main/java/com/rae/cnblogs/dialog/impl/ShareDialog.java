package com.rae.cnblogs.dialog.impl;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.rae.cnblogs.R;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;
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
 * Created by ChenRui on 2017/1/24 0024 14:14.
 */
public class ShareDialog extends SlideDialog {

    public interface OnShareClickListener {

        void onShare(ShareDialog dialog);
    }

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

    @BindView(R.id.btn_share_cancel)
    Button mCancelView;

    @BindView(R.id.hl_ext_action_layout)
    View mExtLayout;

    ShareAction mShareAction;

    private OnShareClickListener mOnShareClickListener;


    public ShareDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_blog_content);
        ButterKnife.bind(this, this);
        mShareAction = new ShareAction((Activity) context);
        setShareIcon(R.drawable.ic_share);
    }

    public void setShareTitle(String title) {
        mShareAction.withTitle(title);
    }

    public void setShareUrl(String url) {
        mShareAction.withTargetUrl(url);
    }

    public void setShareSummary(String summary) {
        mShareAction.withText(summary);
    }

    public void setShareIcon(int resId) {
        mShareAction.withMedia(new UMImage(getContext(), resId));
    }

    public void setShareIcon(String url) {
        mShareAction.withMedia(new UMImage(getContext(), url));
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        mOnShareClickListener = onShareClickListener;
    }

    /**
     * 设置扩展栏可见性
     *
     * @param visibility
     */
    public void setExtLayoutVisibility(int visibility) {
        mExtLayout.setVisibility(visibility);
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

    /**
     * 调用友盟分享
     */
    protected void share(SHARE_MEDIA type) {
        if (mOnShareClickListener != null) {
            mOnShareClickListener.onShare(this);
        }

        mShareAction.setPlatform(type);
        mShareAction.share();
    }

    @OnClick({R.id.tv_share_wechat, R.id.tv_share_wechat_sns, R.id.tv_share_qq, R.id.tv_share_qzone, R.id.tv_share_sina, R.id.tv_share_source, R.id.tv_share_browser, R.id.tv_share_link})
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
            case R.id.tv_share_browser:
                onBrowserViewClick();
                break;
            case R.id.tv_share_link:
                onLinkClick();
                break;
        }

        dismiss();
    }

    // 查看原文
    protected void onViewSourceClick() {
    }

    // 用浏览器打开
    protected void onBrowserViewClick() {
    }

    // 复制链接
    protected void onLinkClick() {
    }

    // 取消
    @OnClick(R.id.btn_share_cancel)
    void onCancelClick() {
        dismiss();
    }

    @Override
    public void setButtonText(int buttonType, String text) {
        switch (buttonType) {
            // 取消文本
            case IAppDialog.BUTTON_NEGATIVE:
                mCancelView.setText(text);
                break;
        }
    }

    @Override
    public void setOnClickListener(int buttonType, final IAppDialogClickListener listener) {
        switch (buttonType) {
            // 取消文本
            case IAppDialog.BUTTON_NEGATIVE:
                mCancelView.setOnClickListener(newClickListener(buttonType, listener));
                break;
        }
    }
}
