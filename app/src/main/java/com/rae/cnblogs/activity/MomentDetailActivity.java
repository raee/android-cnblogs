package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.rae.cnblogs.R;
import com.rae.cnblogs.dialog.impl.ShareDialog;
import com.rae.cnblogs.fragment.MomentDetailFragment;
import com.rae.cnblogs.sdk.bean.MomentBean;

import butterknife.OnClick;

/**
 * 详情
 * Created by ChenRui on 2017/11/2 0002 15:01.
 */
public class MomentDetailActivity extends SwipeBackBaseActivity {

    private MomentBean mMomentBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detail);
        showHomeAsUp();
        mMomentBean = getIntent().getParcelableExtra("data");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, MomentDetailFragment.newInstance(mMomentBean)).commitNow();
    }


    /**
     * 分享
     */
    @OnClick(R.id.btn_share)
    public void onShareClick() {
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.setExtLayoutVisibility(View.GONE);
        shareDialog.setShareWeb(mMomentBean.getSourceUrl(), mMomentBean.getAuthorName() + "：" + mMomentBean.getContent(), mMomentBean.getContent(), mMomentBean.getAvatar());
        shareDialog.show();
    }
}
