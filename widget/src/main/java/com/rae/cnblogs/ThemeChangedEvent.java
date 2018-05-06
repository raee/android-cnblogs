package com.rae.cnblogs;

/**
 * 主题更改事件
 */
public class ThemeChangedEvent {
    private boolean isNight; // 是否为夜间模式

    public ThemeChangedEvent(boolean isNight) {
        this.isNight = isNight;
    }

    public boolean isNight() {
        return isNight;
    }
}
