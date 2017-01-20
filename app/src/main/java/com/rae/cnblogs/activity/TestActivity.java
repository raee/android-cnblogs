package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogCommentItemAdapter;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.cnblogs.widget.RaeDrawerLayout;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * TEST ACTIVITY
 * Created by ChenRui on 2016/12/3 18:06.
 */
public class TestActivity extends BaseActivity {

    @BindView(R.id.tv_console)
    TextView mTextView;

    @BindView(R.id.test)
    RaeDrawerLayout mRaeDrawerLayout;

    @BindView(R.id.rv_test)
    RaeRecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        BlogCommentItemAdapter adapter = new BlogCommentItemAdapter();
        List<BlogComment> data = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("00.##");
        for (int i = 1; i <= 60; i++) {
            BlogComment m = new BlogComment();
            m.setBody("I AM CONTENT " + df.format(i));
            m.setAuthorName("ITEM " + df.format(i));
            data.add(m);
        }
        adapter.invalidate(data);
        mRecyclerView.setAdapter(adapter);
//        mRaeDrawerLayout.setDragDownHandler(new RaeDragDownCompat.DragDownHandler() {
//            @Override
//            public boolean checkCanDrag(float dy, MotionEvent ev) {
//                return dy < 0 && mRecyclerView.isOnTop();
//            }
//        });
//        mRaeDrawerLayout.setDrawerHandler(new RaeDrawerLayout.RaeDrawerHandler() {
//            @Override
//            public boolean checkCanDoDrawer(RaeDrawerLayout view, MotionEvent event) {
//                return mRecyclerView.isOnTop();
//            }
//        });
    }

    private void log(String msg) {
        mTextView.setText(msg);
    }

    @OnClick(R.id.btn_test)
    void onTestClick() {
        mRaeDrawerLayout.toggleSmoothScroll();
    }

    private void startAnim() {
        getWindow().getDecorView().findViewById(android.R.id.content).startAnimation(AnimationUtils.loadAnimation(this, R.anim.at_slide_fade_in));
    }

    @OnClick(R.id.btn_main)
    void onJumpToMain() {
        AppRoute.jumpToMain(this);
    }

    @OnClick(R.id.btn_test_login)
    void onJumpToLogin() {
        AppRoute.jumpToLogin(this);
//        startActivity(new Intent(this, WebLoginActivity.class));
    }


}
