package com.rae.cnblogs.dialog.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;

import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;
import com.rae.cnblogs.sdk.bean.VersionInfo;

/**
 * 版本更新对话框
 * Created by ChenRui on 2017/6/13 0013 0:07.
 */
public class VersionUpdateDialog extends HintCardDialog {

    public VersionUpdateDialog(Context context) {
        super(context);
        setEnSureText(context.getString(R.string.update_now));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_version_card;
    }

    @Override
    public void show() {
        super.show();
        mContentLayout.setEnabled(false);
    }

    public void setVersionInfo(final VersionInfo versionInfo) {
        setTitle(versionInfo.getVersionName());
        setMessage(Html.fromHtml(versionInfo.getAppDesc()).toString());
        setOnEnSureListener(new IAppDialogClickListener() {
            @Override
            public void onClick(IAppDialog dialog, int buttonType) {
                // 启动第三方
                dialog.dismiss();

                // 统计更新
                AppMobclickAgent.onUpdateEvent(getContext());

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(versionInfo.getDownloadUrl()));
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppUI.failed(getContext(), "未找到关联的应用程序，请前往应用市场更新。");
                }
            }
        });
    }

}
