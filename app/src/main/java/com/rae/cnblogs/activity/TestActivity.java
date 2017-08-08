package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.tencent.bugly.beta.tinker.TinkerManager;

/**
 * TEST ACTIVITY
 * Created by ChenRui on 2016/12/3 18:06.
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Log.i("rae", "基类版本：" + TinkerManager.getTinkerId());
        Log.i("rae", "补丁版本：" + TinkerManager.getNewTinkerId());

        findViewById(R.id.btn_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUI.toastInCenter(getApplicationContext(), "dddd！");
            }
        });

        TinkerManager.getInstance().setTinkerListener(new TinkerManager.TinkerListener() {
            @Override
            public void onDownloadSuccess(String s) {

            }

            @Override
            public void onDownloadFailure(String s) {

            }

            @Override
            public void onApplySuccess(String s) {
                AppUI.toastInCenter(getApplicationContext(), "补丁应用成功！" + s);
            }

            @Override
            public void onApplyFailure(String s) {
                AppUI.toastInCenter(getApplicationContext(), "补丁应用失败！" + s);
            }

            @Override
            public void onPatchRollback() {

            }
        });
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinkerManager.getInstance().applyPatch("/sdcard/patch_signed_7zip.apk", true);
            }
        });
    }

}
