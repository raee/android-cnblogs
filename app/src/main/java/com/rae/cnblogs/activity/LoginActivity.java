package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeAnim;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.ILoginPresenter;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 * Created by ChenRui on 2017/1/19 0019 9:59.
 */
public class LoginActivity extends BaseActivity implements ILoginPresenter.ILoginView
//        , WebLoginListener
{

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
        startLoginAnim();
        mLoginPresenter.login();
        removeAccountTextListener(mAccountTextWatcher);
        mLoginButton.setEnabled(false);
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
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onLoginError(String message) {
        onLoginCallback();
        AppUI.failed(this, message);
    }

    private void onLoginCallback() {
        mLoginButton.setEnabled(true);
        addAccountTextListener(mAccountTextWatcher);
        // 结束动画效果
        stopAnim();
    }

    private void stopAnim() {
        AppUI.dismiss();
    }

    // 开始登录动画
    private void startLoginAnim() {
        AppUI.loading(this, R.string.signing);
    }

    private class AccountTextWatcher implements TextWatcher {

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
