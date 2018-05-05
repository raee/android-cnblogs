package com.rae.cnblogs.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.BuildConfig;
import com.rae.cnblogs.CnblogsApplication;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;
import com.rae.cnblogs.dialog.impl.HintCardDialog;
import com.rae.cnblogs.dialog.impl.MenuDialog;
import com.rae.cnblogs.dialog.impl.ShareDialog;
import com.rae.cnblogs.dialog.impl.VersionUpdateDialog;
import com.rae.cnblogs.message.UserInfoEvent;
import com.rae.cnblogs.model.MenuDialogItem;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.VersionInfo;
import com.rae.cnblogs.widget.ImageLoadingView;
import com.tencent.bugly.beta.tinker.TinkerManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;


/**
 * 设置
 * Created by ChenRui on 2017/7/24 0024 1:18.
 */
@Route(path = AppRoute.PATH_SETTING)
public class SettingActivity extends SwipeBackBasicActivity {

    @BindView(R.id.img_clear_cache)
    ImageLoadingView mClearCacheView;

    @BindView(R.id.tv_clear_cache)
    TextView mClearCacheTextView;

    @BindView(R.id.ll_logout)
    View mLogoutLayout;


    @BindView(R.id.tv_check_update)
    TextView mCheckUpdateMsgView;

    @BindView(R.id.pb_check_update)
    ProgressBar mCheckUpdateProgress;

    private ShareDialog mShareDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        showHomeAsUp();

        if (!UserProvider.getInstance().isLogin()) {
            mLogoutLayout.setVisibility(View.GONE);
        }

        // 获取版本号
        mCheckUpdateMsgView.setText(getAppVersion());

    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.ll_clear_cache)
    public void onClearCacheClick() {
        MenuDialog dialog = new MenuDialog(getContext());
        dialog.addDeleteItem("确定清除");
        dialog.addItem("取消");
        dialog.setMessage("将清理所有博客以及离线缓存数据，是否确定清除？");
        dialog.setOnMenuItemClickListener(new MenuDialog.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(MenuDialog dialog, MenuDialogItem item) {
                dialog.dismiss();
                if (item.getName().contains("确定")) {
                    performClearCache();
                }
            }
        });
        dialog.show();
    }

    private void performClearCache() {
        // 显示loading
        mClearCacheView.setVisibility(View.VISIBLE);
        mClearCacheView.loading();
        mClearCacheTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        // 清除缓存
        ((CnblogsApplication) getApplication()).clearCache();
        mClearCacheTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClearCacheView.anim();
            }
        }, 1000);

        // 3秒后隐藏
        mClearCacheView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClearCacheView.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                mClearCacheView.setVisibility(View.GONE);

                mClearCacheTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.default_right_arrow, 0);
            }
        }, 5000);
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.btn_logout)
    public void onLogoutClick() {
        AppMobclickAgent.onClickEvent(getContext(), "Logout");
        HintCardDialog dialog = new HintCardDialog(getContext());
        dialog.setMessage(getString(R.string.tips_logout));
        dialog.setOnEnSureListener(new IAppDialogClickListener() {
            @Override
            public void onClick(IAppDialog dialog, int buttonType) {
                dialog.dismiss();
                AppMobclickAgent.onProfileSignOff();
                UserProvider.getInstance().logout();
                EventBus.getDefault().post(new UserInfoEvent());
                finish();
            }
        });
        dialog.showCloseButton();
        dialog.setEnSureText(getString(R.string.logout));
        dialog.show();

    }

    /**
     * 分享
     */
    @OnClick(R.id.ll_share)
    public void onShareClick() {
        AppMobclickAgent.onClickEvent(getContext(), "ShareApp");
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog(getContext());
            mShareDialog.setShareWeb(getString(R.string.share_app_url), getString(R.string.share_app_title), getString(R.string.share_app_desc), R.drawable.ic_share_app);
            mShareDialog.setExtLayoutVisibility(View.GONE);
        }
        mShareDialog.show();
    }

    /**
     * 开源项目
     */
    @OnClick(R.id.ll_github)
    public void onOpenSourceClick() {
        AppMobclickAgent.onClickEvent(getContext(), "OpenSource");
        AppRoute.routeToWeb(this.getContext(), getString(R.string.github_url));
    }

    /**
     * 开源许可
     */
    @OnClick(R.id.ll_open_source)
    public void onOpenSourceLicenseClick() {
        AppMobclickAgent.onClickEvent(getContext(), "OpenSourceLicense");
        AppRoute.routeToWeb(this.getContext(), getString(R.string.url_license));
    }

    /**
     * 内测版本
     */
    @OnClick(R.id.ll_beta_version)
    public void onBetaVersionLicenseClick() {
        AppMobclickAgent.onClickEvent(getContext(), "BetaVersion");
        AppRoute.routeToWeb(this.getContext(), getString(R.string.url_beta_version));
    }

    /**
     * 帮助中心
     */
    @OnClick(R.id.ll_help_center)
    public void onHelpCenterClick() {
        AppMobclickAgent.onClickEvent(getContext(), "HelpCenter");
        AppRoute.routeToWeb(this.getContext(), getString(R.string.url_help_center));
    }

    /**
     * 检查更新
     */
    @OnClick(R.id.ll_check_update)
    public void onCheckUpdateClick() {
        AppMobclickAgent.onClickEvent(getContext(), "CheckUpdate");
        mCheckUpdateProgress.setVisibility(View.VISIBLE);
        mCheckUpdateMsgView.setVisibility(View.GONE);
        RxObservable.create(CnblogsApiFactory.getInstance(this)
                .getRaeServerApi()
                .versionInfo(getVersionCode(), getVersionName(), getChannel(), BuildConfig.BUILD_TYPE), "checkUpdate")
                .subscribe(new ApiDefaultObserver<VersionInfo>() {
                    @Override
                    protected void onError(String message) {
                        mCheckUpdateProgress.setVisibility(View.INVISIBLE);
                        mCheckUpdateMsgView.setVisibility(View.VISIBLE);
                        mCheckUpdateMsgView.setText("已是最新版本");
                    }

                    @Override
                    protected void accept(VersionInfo versionInfo) {
                        mCheckUpdateProgress.setVisibility(View.INVISIBLE);
                        mCheckUpdateMsgView.setVisibility(View.GONE);
                        VersionUpdateDialog dialog = new VersionUpdateDialog(getContext());
                        dialog.setVersionInfo(versionInfo);
                        dialog.show();
                    }
                });

    }

    /**
     * 好评
     */
    @OnClick(R.id.praises)
    public void onPraisesClick() {
        AppMobclickAgent.onClickEvent(getContext(), "Praises");
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_url))));
        } catch (Exception e) {
            AppUI.failed(this, getString(R.string.praises_error));
        }
    }

    @OnClick(R.id.ll_font_setting)
    public void onFontSettingClick() {
        AppRoute.routeToFontSetting(this);
    }

    @OnClick(R.id.ll_about_me)
    public void onAboutMeClick() {
        AppRoute.jumpToAboutMe(this);
    }

    @OnLongClick(R.id.tv_check_update)
    public boolean onVersionLongClick() {
        String newTinkerId = TinkerManager.getNewTinkerId();
        String tinkerId = TinkerManager.getTinkerId();
        if (!TextUtils.isEmpty(newTinkerId)) {
            AppUI.toastInCenter(this, "基准版本：" + tinkerId + "；补丁版本：" + newTinkerId);
        } else {
            AppUI.toastInCenter(this, "当前版本暂无补丁版本！");
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxObservable.dispose("checkUpdate");
    }

    public String getAppVersion() {
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            return "V" + packageInfo.versionName;
        } catch (Exception ignored) {

        }
        return "";
    }

}
