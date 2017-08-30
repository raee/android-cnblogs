package com.rae.cnblogs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.rae.cnblogs.message.ThemeChangedEvent;

import org.greenrobot.eventbus.EventBus;

import skin.support.SkinCompatManager;

/**
 * 主题资源扩展类
 * Created by ChenRui on 2017/8/30 0030 16:46.
 */
public final class ThemeCompat {

    private static int getResourceId(Context context, String name, String type) {
        Resources resources = context.getResources();
        String themeName = name;
        if (isNight()) {
            themeName += "_night";
        }
        try {
            return resources.getIdentifier(themeName, type, context.getPackageName());
        } catch (Exception ignored) {

        }
        return resources.getIdentifier(name, type, context.getPackageName());
    }

    /**
     * 是否为夜间模式
     */
    public static boolean isNight() {
        return "night".equalsIgnoreCase(SkinCompatManager.getInstance().getCurSkinName());
    }


    /**
     * 获取颜色，根据不通的模式返回不通的资源
     *
     * @param name 名称
     */
    public static int getColor(Context context, String name) {
        return getResourceId(context, name, "color");
    }

    /**
     * 获取图片
     *
     * @param name 名称
     */
    public static int getDrawableId(Context context, String name) {
        return getResourceId(context, name, "drawable");
    }

    /**
     * 获取图片
     *
     * @param name 名称
     */
    public static Drawable getDrawable(Context context, String name) {
        return context.getResources().getDrawable(getDrawableId(context, name));
    }

    /**
     * 切换夜间模式
     */
    public static void switchNightMode() {
        if (isNight()) {
            // 切换正常模式
            SkinCompatManager.getInstance().restoreDefaultTheme();
        } else {
            SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
        }

        // 发出通知
        EventBus.getDefault().post(new ThemeChangedEvent(ThemeCompat.isNight()));
    }
}
