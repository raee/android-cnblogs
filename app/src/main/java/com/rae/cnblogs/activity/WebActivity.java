package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.dialog.impl.ShareDialog;
import com.rae.cnblogs.fragment.WebViewFragment;

import butterknife.BindView;
import butterknife.OnClick;
import skin.support.SkinCompatManager;

/**
 * 网页
 * Created by ChenRui on 2017/1/25 0025 9:32.
 */
public class WebActivity extends SwipeBackBaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;

    @BindView(R.id.tv_web_title)
    TextView mTitleView;

    @BindView(R.id.img_action_bar_more)
    ImageView mShareView;

    @BindView(R.id.view_holder)
    View mNightView;

    WebViewFragment mWebViewFragment;

    private ShareDialog mShareDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getUrl();
        if (TextUtils.isEmpty(url)) {
            AppUI.toast(this, "网页路径为空！");
            finish();
            return;
        }
        setContentView(R.layout.activity_web);
        showHomeAsUp(mToolbar);
        mShareDialog = new ShareDialog(this);
        mShareDialog.setOnShareClickListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onShare(ShareDialog dialog) {

                String url = mWebViewFragment.getUrl();
                if (!url.contains("?")) {
                    url += "?share_from=rae_cnblogs";
                }
                if (url.contains("&")) {
                    url += "&share_from=rae_cnblogs";
                }

                dialog.setShareWeb(url, getTitle().toString(), String.format("%s - 来自博客园APP", getTitle()), null);
            }
        });
        mShareDialog.setExtLayoutVisibility(View.GONE);
        mWebViewFragment = getWebViewFragment(url);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, mWebViewFragment).commit();

        if ("night".equalsIgnoreCase(SkinCompatManager.getInstance().getCurSkinName())) {
            mNightView.setVisibility(View.VISIBLE);
        }

    }

    protected WebViewFragment getWebViewFragment(String url) {
        return WebViewFragment.newInstance(url);
    }

    @Nullable
    protected String getUrl() {
        if (getIntent().getData() == null) return null;
        return getIntent().getData().toString();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleView.setText(title);
    }

    @Override
    protected int getHomeAsUpIndicator() {
        return R.drawable.ic_close;
    }

    @OnClick(R.id.img_action_bar_more)
    public void onActionMenuMoreClick() {
        mShareDialog.show();
    }
}
