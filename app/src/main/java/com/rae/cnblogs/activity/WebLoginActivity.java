package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.WebLoginFragment;

/**
 * 网页登录授权
 * Created by ChenRui on 2017/1/19 21:03.
 */
public class WebLoginActivity extends BaseActivity {


    private String mUrl = "https://passport.cnblogs.com/user/signin";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_login);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, WebLoginFragment.newInstance(mUrl)).commit();
    }
}
