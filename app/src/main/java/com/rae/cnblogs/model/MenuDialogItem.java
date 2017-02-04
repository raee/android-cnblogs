package com.rae.cnblogs.model;

import android.support.annotation.ColorRes;

/**
 * 菜单选项
 * Created by ChenRui on 2017/2/4 0004 17:49.
 */
public class MenuDialogItem {
    private String name;
    private int colorId;

    public MenuDialogItem(String name) {
        this.name = name;
    }

    public MenuDialogItem(String name, @ColorRes int colorId) {
        this.colorId = colorId;
        this.name = name;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
