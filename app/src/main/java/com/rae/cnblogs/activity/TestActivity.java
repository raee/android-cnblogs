package com.rae.cnblogs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;

/**
 * TEST ACTIVITY
 * Created by ChenRui on 2016/12/3 18:06.
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AppRoute.jumpToMain(TestActivity.this);
//        final HintCardDialog dialog = new HintCardDialog(this);
//        dialog.setTitle("ABC");
//        dialog.setMessage("呵呵");
//        dialog.showCloseButton();
//        dialog.show();

        findViewById(R.id.btn_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppUI.toastInCenter(getApplicationContext(), "dddd！");
//                AppRoute.jumpToMain(TestActivity.this);

                startActivity(new Intent(TestActivity.this, LauncherActivity.class));

            }
        });

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.show();
            }
        });
    }

}
