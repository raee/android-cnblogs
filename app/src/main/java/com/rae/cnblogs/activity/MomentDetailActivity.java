package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.MomentDetailFragment;
import com.rae.cnblogs.sdk.bean.MomentBean;

/**
 * 详情
 * Created by ChenRui on 2017/11/2 0002 15:01.
 */
public class MomentDetailActivity extends SwipeBackBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detail);
        showHomeAsUp();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, MomentDetailFragment.newInstance((MomentBean) getIntent().getParcelableExtra("data"))).commitNow();
    }
}
