package com.rae.cnblogs.dialog;

import android.content.DialogInterface;

/**
 * 弹出窗口
 * Created by ChenRui on 2017/1/24 0024 13:53.
 */
public interface IAppDialog {

    /**
     * 表示消极的按钮类型，如：取消、放弃、返回 、关闭
     */
    int BUTTON_NEGATIVE = 0;


    /**
     * 表示积极的按钮类型，如：确定
     */
    int BUTTON_POSITIVE = 1;

    /**
     * 显示窗口
     */
    void show();

    /**
     * 是否已经显示
     */
    boolean isShowing();

    /**
     * 关闭窗口
     */
    void dismiss();

    /**
     * 设置窗口标题
     *
     * @param title 标题文本
     */
    void setTitle(String title);

    /**
     * 设置提示文本
     *
     * @param message 文本信息
     */
    void setMessage(String message);


    /**
     * 设置点击事件
     *
     * @param buttonType 参考{@link #BUTTON_NEGATIVE}，Button_*的字段
     * @param listener   回调
     */
    void setOnClickListener(int buttonType, IAppDialogClickListener listener);

    /**
     * 设置按钮的显示的文本信息
     *
     * @param buttonType 参考{@link #BUTTON_NEGATIVE}，Button_*的字段
     * @param text       文本信息
     */
    void setButtonText(int buttonType, String text);

    /**
     * 设置按钮的可见状态
     *
     * @param buttonType 按钮类型
     * @param visibility 可见状态
     */
    void setButtonVisibility(int buttonType, int visibility);

    /**
     * 是否可以取消的
     */
    void setCancelable(boolean cancelable);

    /**
     * 点击外部是否可以取消
     */
    void setCanceledOnTouchOutside(boolean canceledOnTouchOutside);

    /**
     * 设置图片URL
     *
     * @param type
     * @param url
     */
    void setImage(int type, String url);


    /**
     * 取消监听
     *
     * @param listener
     */
    void setOnDismissListener(DialogInterface.OnDismissListener listener);
}
