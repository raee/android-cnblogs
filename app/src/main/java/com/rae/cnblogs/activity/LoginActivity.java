package com.rae.cnblogs.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeAnim;
import com.rae.cnblogs.fragment.WebLoginFragment;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.ILoginPresenter;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.widget.webclient.bridge.WebLoginListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 * Created by ChenRui on 2017/1/19 0019 9:59.
 */
public class LoginActivity extends BaseActivity implements ILoginPresenter.ILoginView, WebLoginListener {

    @BindView(com.rae.cnblogs.R.id.ll_login_container)
    View mLoginLayout;

    @BindView(R.id.et_login_user_name)
    EditText mUserNameView;

    @BindView(R.id.et_login_password)
    EditText mPasswordView;

    @BindView(R.id.btn_login)
    Button mLoginButton;

    @BindView(R.id.img_login_logo)
    ImageView mLogoView;
    @BindView(R.id.tv_login_tips)
    TextView mTipsView;
    @BindView(R.id.ll_login_tips_layout)
    View mTipsLayout;

    private ILoginPresenter mLoginPresenter;
    private AccountTextWatcher mAccountTextWatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(com.rae.cnblogs.R.anim.slide_in_bottom, 0);
        super.onCreate(savedInstanceState);
        setContentView(com.rae.cnblogs.R.layout.activity_login);
        mLoginPresenter = CnblogsPresenterFactory.getLoginPresenter(this, this);

        mBackView.setVisibility(View.INVISIBLE);
        mBackView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBackView.setVisibility(View.VISIBLE);
                RaeAnim.fadeIn(mBackView);
            }
        }, 300);

        mAccountTextWatcher = new AccountTextWatcher();
        addAccountTextListener(mAccountTextWatcher);

        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, WebLoginFragment.newInstance()).commit();
    }

    private void addAccountTextListener(AccountTextWatcher watcher) {
        mUserNameView.addTextChangedListener(watcher);
        mPasswordView.addTextChangedListener(watcher);
    }

    private void removeAccountTextListener(AccountTextWatcher watcher) {
        mUserNameView.removeTextChangedListener(watcher);
        mPasswordView.removeTextChangedListener(watcher);
    }


    @Override
    public void finish() {
        super.finish();
        RaeAnim.fadeIn(mBackView);
        overridePendingTransition(0, com.rae.cnblogs.R.anim.slide_out_bottom);
    }

    /**
     * 登录点击
     */
    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        // 先WEB登录
//        EventBus.getDefault().post(new LoginEventMessage(getUserName(), getPassword()));

        // 模拟
        mLoginButton.postDelayed(new Runnable() {
            @Override
            public void run() {
//                onLoginCallback();
            }
        }, 5000);

        removeAccountTextListener(mAccountTextWatcher);
        mLoginButton.setEnabled(false);
        startLoginAnim();
    }

    @Override
    public String getUserName() {
        return mUserNameView.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return mPasswordView.getText().toString().trim();
    }

    @Override
    public void onLoginSuccess(UserInfoBean userInfo) {
        onLoginCallback();
        AppUI.toast(this, "官方接口登录成功：" + userInfo.getDisplayName());
    }

    @Override
    public void onLoginError(String message) {
        onLoginCallback();
        AppUI.toast(this, "官方接口登录失败：" + message);
    }

    @Override
    public void onWebLoginSuccess() {
        AppUI.toast(this, "WEB登录成功");
        // 登录官方接口
        mLoginPresenter.login();
    }

    @Override
    public void onWebLoginError(String message) {
        onLoginCallback();
        AppUI.toast(this, "WEB登录失败：" + message);
    }

    @Override
    public void onWebLoginCodeError(String msg) {
        onLoginCallback();
        AppUI.toast(this, "WEB登录失败：" + msg);
    }

    @Override
    public void onWebLoginCodeBitmap(Bitmap bitmap) {
        AppUI.toast(this, "WEB登录失败：需要验证码");
    }

    private void onLoginCallback() {
        mLoginButton.setEnabled(true);
        addAccountTextListener(mAccountTextWatcher);

        // 结束动画效果
//        mLoginLayout.clearAnimation();
//        mLogoView.clearAnimation();
//        mTipsLayout.clearAnimation();


        long duration = 800;

        // LOGO 下移
        // 获取位移差值
        float dy = mLoginLayout.getHeight() / 2;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0, Animation.ABSOLUTE, dy, Animation.ABSOLUTE, 0);
        translateAnimation.setDuration(duration);
        translateAnimation.setFillAfter(true);
        mLogoView.startAnimation(translateAnimation);

        // 提示信息上移
        AnimationSet tipsLayoutAnimSet = new AnimationSet(true);

        TranslateAnimation tipsLayoutAnim = new TranslateAnimation(0, 0, 0, 0, Animation.ABSOLUTE, -dy, Animation.ABSOLUTE, 0);
        tipsLayoutAnim.setDuration(duration);

        AlphaAnimation tipsLayoutAlphaAnimation = new AlphaAnimation(1, 0);
        tipsLayoutAlphaAnimation.setDuration(duration);

        tipsLayoutAnimSet.addAnimation(tipsLayoutAnim);
        tipsLayoutAnimSet.addAnimation(tipsLayoutAlphaAnimation);
        tipsLayoutAnimSet.setFillAfter(true);
        mTipsLayout.startAnimation(tipsLayoutAnimSet);

        // 登录框渐入
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setStartOffset(duration);
        mLoginLayout.startAnimation(alphaAnimation);
    }

    // 开始登录动画
    private void startLoginAnim() {
        long duration = 500;
        // 登录框渐隐
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        mLoginLayout.startAnimation(alphaAnimation);

        // LOGO 下移
        // 获取位移差值
        float dy = mLoginLayout.getHeight() / 2;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, dy);
        translateAnimation.setDuration(duration);
        translateAnimation.setFillAfter(true);
        mLogoView.startAnimation(translateAnimation);

        // 提示信息上移
        AnimationSet tipsLayoutAnimSet = new AnimationSet(true);

        TranslateAnimation tipsLayoutAnim = new TranslateAnimation(0, 0, 0, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -dy);
        tipsLayoutAnim.setDuration(duration);

        AlphaAnimation tipsLayoutAlphaAnimation = new AlphaAnimation(0, 1);
        tipsLayoutAlphaAnimation.setDuration(duration);

        AnimationSet fadeSet = new AnimationSet(true);
        AlphaAnimation fadeInAnim = new AlphaAnimation(1, 0.5f);
        fadeInAnim.setDuration(800);
        AlphaAnimation fadeOutAnim = new AlphaAnimation(0.5f, 1f);
        fadeOutAnim.setDuration(800);
        fadeOutAnim.setStartOffset(800);
        fadeSet.setRepeatCount(-1);

        tipsLayoutAnimSet.addAnimation(tipsLayoutAnim);
        tipsLayoutAnimSet.addAnimation(tipsLayoutAlphaAnimation);
        fadeSet.addAnimation(fadeInAnim);
//        fadeSet.addAnimation(fadeOutAnim);
        tipsLayoutAnimSet.addAnimation(fadeSet);
        tipsLayoutAnimSet.setFillAfter(true);


        mTipsLayout.setVisibility(View.VISIBLE);
        mTipsLayout.startAnimation(tipsLayoutAnimSet);


    }

    private class AccountTextWatcher implements TextWatcher {

        public AccountTextWatcher() {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mLoginButton.setEnabled(mUserNameView.length() > 0 && mPasswordView.length() > 0);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
