package com.rae.cnblogs;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public class CnblogsTinkerApplication extends TinkerApplication {

    public CnblogsTinkerApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.rae.cnblogs.CnblogsApplicationProxy");
    }


    public Application getApplication() {
        return this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base); // 解决Tinker存在的BUG，一定要在这之前初始化
        super.attachBaseContext(base);
    }

}
