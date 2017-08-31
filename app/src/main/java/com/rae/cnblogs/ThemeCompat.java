package com.rae.cnblogs;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Window;

import com.rae.cnblogs.message.ThemeChangedEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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


    /**
     * 刷新状态栏颜色
     *
     * @param nightMode 是否为深色模式
     */
    public static void refreshStatusColor(Activity context, boolean nightMode) {
        changeMiUIStatusMode(context.getWindow(), nightMode);
    }

    /**
     * 修改小米手机系统的状态栏字体颜色
     *
     * @param dark 状态栏黑色字体
     */
    private static void changeMiUIStatusMode(Window window, boolean dark) {
        if (!Build.BRAND.toLowerCase().equalsIgnoreCase("xiaomi")) {
            return;
        }

        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
            } catch (Exception ignored) {
            }
        }
    }
}
