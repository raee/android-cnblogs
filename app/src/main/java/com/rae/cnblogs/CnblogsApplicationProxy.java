package com.rae.cnblogs;

import android.app.Application;
import android.content.Intent;

import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.db.DbCnblogs;
import com.rae.swift.session.SessionManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.tinker.TinkerApplicationLike;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 应用程序
 * Created by ChenRui on 2016/12/1 21:35.
 */
public class CnblogsApplicationProxy extends TinkerApplicationLike {


    public CnblogsApplicationProxy(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        DbCnblogs.init(getApplication());
        RaeImageLoader.initImageLoader(getApplication());
        initUmengShareConfig();
        UserProvider.init(getApplication());
        SessionManager.initWithConfig(new SessionManager.ConfigBuilder().context(getApplication()).userClass(UserInfoBean.class).build());
//        if (!BuildConfig.BUILD_TYPE.equals("debug")) {
            // 日志上报
            Bugly.init(getApplication(), BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG);
//        }
        if (BuildConfig.DEBUG) {
//            LeakCanary.install(this);
        }
    }

    /**
     * 友盟分享
     */
    private void initUmengShareConfig() {
        UMShareAPI.get(getApplication());
        PlatformConfig.setWeixin(AppConstant.WECHAT_APP_ID, AppConstant.WECHAT_APP_SECRET);
        PlatformConfig.setSinaWeibo(AppConstant.WEIBO_APP_ID, AppConstant.WEIBO_APP_SECRET, "http://www.raeblog.com/cnblogs/index.php/share/weibo/redirect");
        PlatformConfig.setQQZone(AppConstant.QQ_APP_ID, AppConstant.QQ_APP_SECRET);
    }


}
