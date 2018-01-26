package com.rae.cnblogs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.ThemeCompat;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * TEST ACTIVITY
 * Created by ChenRui on 2016/12/3 18:06.
 */
public class TestActivity extends BaseActivity {

    @BindView(R.id.tv_message)
    TextView mMsgView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @OnClick(R.id.btn_main)
    public void onMainClick() {
        startActivity(new Intent(TestActivity.this, LauncherActivity.class));
    }

    @OnClick(R.id.btn_test)
    public void onTestClick() {
        ThemeCompat.switchNightMode();
//        AppUI.toastInCenter(this, "我是热更新！");
    }

    @OnClick(R.id.btn_sdk_patch_test)
    public void onSdkPatchClick() {
        CnblogsApiProvider provider = CnblogsApiFactory.getInstance(this);
        AppUI.toastInCenter(this, "SDK版本号：" + provider.getApiVersion());
    }


    @OnClick(R.id.btn_patch_test)
    public void onPatchClick() {
        StringBuffer sb = new StringBuffer();
        log(sb, "当前版本", TinkerManager.getTinkerId());
        log(sb, "补丁版本", TinkerManager.getNewTinkerId());
        File patchFile = new File(Environment.getExternalStorageDirectory(), "cnblogs.patch");
        log(sb, "补丁路径", patchFile.getPath());
        if (!patchFile.exists()) {
            log(sb, "补丁不存在！", patchFile.getPath());
            mMsgView.setText(sb.toString());
            Log.e("rae-tinker", sb.toString());
            return;
        }

        TinkerManager.getInstance().setTinkerListener(new TinkerManager.TinkerListener() {
            @Override
            public void onDownloadSuccess(String s) {
                Log.i("rae-tinker", "下载补丁成功：" + s);
                mMsgView.append("下载补丁成功：" + s);
            }

            @Override
            public void onDownloadFailure(String s) {
                Log.e("rae-tinker", "下载补丁失败：" + s);
                mMsgView.append("下载补丁失败：" + s);

            }

            @Override
            public void onPatchStart() {

                Log.i("rae-tinker", "【onPatchStart】");
                mMsgView.append("onPatchStart");
            }

            @Override
            public void onApplySuccess(String s) {
                Log.i("rae-tinker", "【补丁应用成功】" + s);
                mMsgView.append("补丁应用成功：" + s);
            }

            @Override
            public void onApplyFailure(String s) {
                Log.e("rae-tinker", "补丁应用失败" + s);
                mMsgView.append("补丁应用失败：" + s);
            }

            @Override
            public void onPatchRollback() {
                Log.e("rae-tinker", "补丁应用失败，回滚补丁。");
                mMsgView.append("补丁应用失败，回滚补丁!");
            }
        });
        TinkerManager.getInstance().applyPatch(patchFile.getPath(), true);
        mMsgView.setText(sb.toString());
        Log.d("rae-tinker", sb.toString());
    }


    private void log(StringBuffer sb, String key, String value) {
        sb.append(key);
        sb.append(" = ");
        sb.append(value);
        sb.append("\n");
    }

}
