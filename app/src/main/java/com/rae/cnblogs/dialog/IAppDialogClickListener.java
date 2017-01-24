package com.rae.cnblogs.dialog;

/**
 * 弹出窗口监听
 * Created by ChenRui on 2017/1/24 0024 13:56.
 */
public interface IAppDialogClickListener {


    /**
     * 点击时候触发
     *
     * @param dialog     窗口
     * @param buttonType 参考字段：{@link IAppDialog#BUTTON_NEGATIVE}，Button_*的常量
     */
    void onClick(IAppDialog dialog, int buttonType);

}
