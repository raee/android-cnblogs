package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.MomentMessageFragment;

import butterknife.OnClick;

/**
 * 闪存消息
 * Created by ChenRui on 2017/11/6 0006 14:21.
 */
@Route(path = AppRoute.PATH_MOMENT_MESSAGE)
public class MomentMessageActivity extends SwipeBackBasicActivity {
    private MomentMessageFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_message);
        showHomeAsUp();
        mFragment = new MomentMessageFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, mFragment)
                .commitNow();
    }

    @OnClick(R.id.title_tool_bar)
    public void onToolbarClick() {
        mFragment.scrollToTop();
    }

    @OnClick(R.id.img_at_me)
    public void onAtMeClick() {
        AppRoute.jumpToMomentAtMe(this);
    }
}
