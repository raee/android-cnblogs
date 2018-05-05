package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.dialog.impl.ShareDialog;
import com.rae.cnblogs.fragment.MomentDetailFragment;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.widget.PlaceholderView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 详情
 * Created by ChenRui on 2017/11/2 0002 15:01.
 */
@Route(path = AppRoute.PATH_MOMENT_DETAIL)
public class MomentDetailActivity extends SwipeBackBasicActivity {

    private MomentBean mMomentBean;

    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholderView;
    private String mIngId;
    private String mUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detail);
        showHomeAsUp();
        mMomentBean = getIntent().getParcelableExtra("data");
        mIngId = getIntent().getStringExtra("ingId");
        mUserId = getIntent().getStringExtra("userId");
        if (mMomentBean != null && !TextUtils.isEmpty(mMomentBean.getId())) {
            mPlaceholderView.dismiss();
            attachFragment();
        } else if (!TextUtils.isEmpty(mIngId) && !TextUtils.isEmpty(mUserId)) {
            // 根据闪存ID获取详情
            loadMomentDetail();
        } else {
            mPlaceholderView.empty("参数缺失");
        }
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserProvider.getInstance().isLogin()) {
                    loadMomentDetail();
                } else {
                    AppRoute.routeToLogin(v.getContext());
                }
            }
        });
    }

    private void attachFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, MomentDetailFragment.newInstance(mMomentBean)).commitNow();
    }

    private void loadMomentDetail() {
        if (!UserProvider.getInstance().isLogin()) {
            mPlaceholderView.retry("登录后更精彩");
            return;
        }

        IMomentApi momentApi = CnblogsApiFactory.getInstance(this).getMomentApi();
        RxObservable.create(momentApi.getMomentDetail(mUserId, mIngId, System.currentTimeMillis()), "moment")
                .subscribe(new ApiDefaultObserver<MomentBean>() {
                    @Override
                    protected void onError(String message) {
                        mPlaceholderView.retry(message);
                    }

                    @Override
                    protected void accept(MomentBean momentBean) {
                        mMomentBean = momentBean;
                        attachFragment();
                        mPlaceholderView.dismiss();
                    }
                });

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
