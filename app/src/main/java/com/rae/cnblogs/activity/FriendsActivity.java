package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.BloggerFragment;

/**
 * 粉丝和关注
 * Created by ChenRui on 2017/2/23 00:41.
 */
public class FriendsActivity extends SwipeBackBaseActivity {

    private int mFromType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        showHomeAsUp();
        String blogApp = getIntent().getStringExtra("blogApp");
        String bloggerName = getIntent().getStringExtra("bloggerName");
        mFromType = getIntent().getIntExtra("fromType", AppRoute.ACTIVITY_FRIENDS_TYPE_FANS);
        if (TextUtils.isEmpty(blogApp)) {
            AppUI.toast(this, "博主信息为空！");
            finish();
            return;
        }

        if (TextUtils.isEmpty(bloggerName)) {
            bloggerName = "我";
        }
        if (isFansType()) {
            setTitle(getString(R.string.title_fans, bloggerName));
        } else {
            setTitle(getString(R.string.title_follow, bloggerName));
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, BloggerFragment.newInstance(blogApp, isFansType()))
                .commitNowAllowingStateLoss();
    }


    /**
     * 是否为粉丝类型
     */
    public boolean isFansType() {
        return mFromType == AppRoute.ACTIVITY_FRIENDS_TYPE_FANS;
    }
}
