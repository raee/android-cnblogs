package com.rae.cnblogs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.BlogListFragment;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.io.File;

import butterknife.OnClick;

/**
 * TEST ACTIVITY
 * Created by ChenRui on 2016/12/3 18:06.
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        findViewById(R.id.btn_islogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        CategoryBean categroy = new CategoryBean();
        categroy.setName("首页");
        categroy.setType("home");
        getSupportFragmentManager().beginTransaction().add(R.id.content, BlogListFragment.newInstance(0,categroy, BlogType.BLOG)).commitNowAllowingStateLoss();
    }

    @OnClick(R.id.btn_main)
    public void onMainClick() {
        startActivity(new Intent(TestActivity.this, LauncherActivity.class));
    }

    @OnClick(R.id.btn_test)
    public void onTestClick() {

    }

    @OnClick(R.id.btn_islogin)
    public void onIsLoginClick() {
        boolean login = UserProvider.getInstance().isLogin();
        AppUI.toastInCenter(getContext(), "是否登录：" + login);
    }

    @OnClick(R.id.btn_patch_test)
    public void onPatchClick() {
        StringBuffer sb = new StringBuffer();
        log(sb, "当前版本", TinkerManager.getTinkerId());
        log(sb, "补丁版本", TinkerManager.getNewTinkerId());
        File patchFile = new File(Environment.getExternalStorageDirectory(), "");
        log(sb, "补丁路径", patchFile.getPath());
        if (!patchFile.exists()) {
            log(sb, "补丁不存在！", patchFile.getPath());
            Log.e("rae-tinker", sb.toString());
            return;
        }

        TinkerManager.getInstance().setTinkerListener(new TinkerManager.TinkerListener() {
            @Override
            public void onDownloadSuccess(String s) {
                Log.i("rae-tinker", "下载补丁成功：" + s);
            }

            @Override
            public void onDownloadFailure(String s) {
                Log.e("rae-tinker", "下载补丁失败：" + s);

            }

            @Override
            public void onApplySuccess(String s) {
                Log.i("rae-tinker", "【补丁应用成功】" + s);
            }

            @Override
            public void onApplyFailure(String s) {
                Log.e("rae-tinker", "补丁应用失败" + s);
            }

            @Override
            public void onPatchRollback() {
                Log.e("rae-tinker", "补丁应用失败，回滚补丁。");
            }
        });
        TinkerManager.getInstance().applyPatch(patchFile.getPath(), true);
        Log.d("rae-tinker", sb.toString());
    }


    private void log(StringBuffer sb, String key, String value) {
        sb.append(key);
        sb.append(" = ");
        sb.append(value);
        sb.append("\n");
    }

}
