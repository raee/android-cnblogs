package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
public class LoginActivity extends BaseActivity implements ILoginPresenter.ILoginView {

    @BindView(com.rae.cnblogs.R.id.ll_login_container)
    View mLoginLayout;

    @BindView(R.id.et_login_user_name)
    EditText mUserNameView;

    @BindView(R.id.et_login_password)
    EditText mPasswordView;

    @BindView(R.id.btn_login)
    Button mLoginButton;

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

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
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
        AppUI.toast(this, "登录成功：" + userInfo.getDisplayName());
    }

    @Override
    public void onLoginError(String message) {
        onLoginCallback();
        AppUI.toast(this, message);
    }

    private void onLoginCallback() {
        mLoginButton.setEnabled(true);
        addAccountTextListener(mAccountTextWatcher);
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
