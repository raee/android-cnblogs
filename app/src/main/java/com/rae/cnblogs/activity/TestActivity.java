package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.dialog.impl.MenuDialog;
import com.rae.cnblogs.fragment.CategoriesFragment;
import com.rae.cnblogs.widget.ImageLoadingView;
import com.rae.cnblogs.widget.RaeDrawerLayout;
import com.rae.cnblogs.widget.RaeRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

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

    @BindView(R.id.img_test_like)
    ImageLoadingView mLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        getSupportFragmentManager().beginTransaction().add(R.id.content, new CategoriesFragment()).commit();


//        BlogCommentItemAdapter adapter = new BlogCommentItemAdapter();
//        List<BlogCommentBean> data = new ArrayList<>();
//        DecimalFormat df = new DecimalFormat("00.##");
//        for (int i = 1; i <= 60; i++) {
//            BlogCommentBean m = new BlogCommentBean();
//            m.setBody("I AM CONTENT " + df.format(i));
//            m.setAuthorName("ITEM " + df.format(i));
//            data.add(m);
//        }
//        adapter.invalidate(data);
//        mRecyclerView.setAdapter(adapter);
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

//        AppRoute.jumpToBlogger(this, "sloane");
    }

    private void log(String msg) {
        mTextView.setText(msg);
    }

    @OnClick(R.id.btn_test)
    void onTestClick() {
//        mRaeDrawerLayout.toggleSmoothScroll();

//        mLoadingView.loading();


//        AppRoute.jumpToBlogger(this, "sloane");
//
//        mLoadingView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mLoadingView.setSelected(!mLoadingView.isSelected());
//                mLoadingView.dismiss();
//            }
//        }, 300);

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
//        AppRoute.jumpToWebLogin(this);
//        AppRoute.jumpToLogin(this);

        AppRoute.jumpToImagePreview(this);
    }

    @OnLongClick(R.id.btn_test_dialog)
    boolean testDialogClick() {
//        IAppDialog dialog = DialogProvider.create(this, DialogProvider.TYPE_HINT_CARD);
//        dialog.setMessage("我是提示信息");
//        dialog.setTitle("大大的标题");
//        dialog.setImage(0, "http://mobike.com/wp-content/uploads/2016/11/23.jpg");
//        dialog.setButtonText(IAppDialog.BUTTON_POSITIVE, "立即查看");
////        dialog.setButtonVisibility(IAppDialog.BUTTON_POSITIVE, View.GONE);
//        dialog.show();

        MenuDialog dialog = new MenuDialog(this);
        dialog.addDeleteItem("删除评论");
//        dialog.addItem("新增");
//        dialog.addItem("修噶");
//        dialog.addDeleteItem("退出");
        dialog.show();

        return true;
    }

}
