package com.rae.cnblogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.rae.cnblogs.widget.SkinRoundedImageView;

import skin.support.app.SkinLayoutInflater;

/**
 * 自定义View不支持的皮肤扩展
 * Created by ChenRui on 2017/8/30 0030 17:25.
 */
public class CnblogsLayoutInflater implements SkinLayoutInflater {

    @Override
    public View createView(@NonNull Context context, String name, @NonNull AttributeSet attrs) {

        Log.i("rae-skin", "创建类型：" + name);

        switch (name) {
            case "com.makeramen.roundedimageview.RoundedImageView":
                return new SkinRoundedImageView(context, attrs);
        }

        return null;
    }
}
