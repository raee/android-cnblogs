package com.rae.cnblogs.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.rae.cnblogs.dialog.impl.AppDialog;

/**
 * 分类弹出窗口
 * Created by ChenRui on 2017/7/19 0019 1:05.
 */
public class CategoryDialog extends AppDialog {

    private int mTopMargin;

    public CategoryDialog(Context context) {
        super(context);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
    }

    public void setTopMargin(int value) {
        mTopMargin = value;
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();

        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getAttributes().gravity = Gravity.START | Gravity.END | Gravity.TOP;
            window.getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
            window.getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setDimAmount(0);

            View contentView = getWindow().findViewById(android.R.id.content);
            if (contentView == null) return;
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
            lp.topMargin = mTopMargin;
            contentView.setLayoutParams(lp);
        }
    }
}
