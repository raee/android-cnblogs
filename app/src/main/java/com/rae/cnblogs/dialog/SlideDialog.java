package com.rae.cnblogs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.rae.cnblogs.R;

/**
 * 底部滑动上来的对话框
 * Created by ChenRui on 2016/12/7 22:15.
 */
public class SlideDialog extends Dialog {

    public SlideDialog(Context context) {
        super(context, R.style.SlideDialog);
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        attr.gravity = Gravity.LEFT | Gravity.RIGHT | Gravity.BOTTOM;
        attr.horizontalMargin = 0;
        attr.verticalMargin = 0;
        attr.width = WindowManager.LayoutParams.MATCH_PARENT;
        attr.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(attr);
    }

}
