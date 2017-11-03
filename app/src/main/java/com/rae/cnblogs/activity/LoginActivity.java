package com.rae.cnblogs.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeAnim;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;
import com.rae.cnblogs.dialog.impl.DefaultDialog;
import com.rae.cnblogs.dialog.impl.HintCardDialog;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.ILoginPresenter;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * 登录
 * Created by ChenRui on 2017/1/19 0019 9:59.
 */
public class LoginActivity extends BaseActivity
        implements ILoginPresenter.ILoginView {

    @BindView(com.rae.cnblogs.R.id.ll_login_container)
    View mLoginLayout;

    @BindView(com.rae.cnblogs.R.id.ll_user_info_container)
    View mUserInfoLayout;

//    @BindView(R.id.et_login_user_name)
//    EditText mUserNameView;

//    @BindView(R.id.et_login_password)
//    EditText mPasswordView;

    @BindView(R.id.tv_loading_message)
    TextView mLoadingMsgView;

    @BindView(R.id.btn_login)
    TextView mLoginButton;

    @BindView(R.id.img_login_logo)
    ImageView mLogoView;

    @BindView(R.id.pb_loading)
    View mProgressBar;
//
//    @BindView(R.id.cb_eyes)
//    CheckBox mEyesView;

//    @BindView(R.id.img_edit_delete)
//    ImageView mPwdDelView;

//    @BindView(R.id.ll_login_tips_layout)
//    View mTipsLayout;

//    @BindView(R.id.tv_login_tips)
//    TextView mTipsView;

    private ILoginPresenter mLoginPresenter;
//    private AccountTextWatcher mAccountTextWatcher;

    private DefaultDialog mErrorTipsDialog;

    protected HintCardDialog mLoginContractDialog;

    private int mErrorTime = 0; // 登录错误次数，达到3次以上提示用户是否跳转网页登录
    private boolean mIsLoadingUserInfo; // 是否正在获取用户信息

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(com.rae.cnblogs.R.anim.slide_in_bottom, android.R.anim.fade_out);
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

//        mAccountTextWatcher = new AccountTextWatcher();
//        addAccountTextListener(mAccountTextWatcher);

        mErrorTipsDialog = new DefaultDialog(this);
        mErrorTipsDialog.setButtonText(IAppDialog.BUTTON_POSITIVE, getString(R.string.i_try_agin));
        mErrorTipsDialog.setOnClickListener(IAppDialog.BUTTON_POSITIVE, new IAppDialogClickListener() {
            @Override
            public void onClick(IAppDialog dialog, int buttonType) {
                dialog.dismiss();
                performUserInfo();
            }
        });

        mErrorTipsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dismissLoading();
            }
        });

//        mPasswordView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mPwdDelView.setVisibility(mPasswordView.length() > 0 ? View.VISIBLE : View.GONE);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        mEyesView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // 选择状态 显示明文--设置为可见的密码
//                    mPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                } else {
//                    // 默认状态显示密码--设置文本 要一起写才能起作用  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
//                    mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                }
//
//                mPasswordView.setSelection(mPasswordView.length());
//            }
//        });


        mLoginContractDialog = new HintCardDialog(this);
        mLoginContractDialog.setMessage(getString(R.string.login_contract_content));
        mLoginContractDialog.setEnSureText(getString(R.string.agree));

    }

    private void dismissLoading() {
        if (mIsLoadingUserInfo) return;
        mLoginButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                createDismissObservable().subscribe();
            }
        }, 300);
    }

//    private void addAccountTextListener(AccountTextWatcher watcher) {
//        mUserNameView.addTextChangedListener(watcher);
//        mPasswordView.addTextChangedListener(watcher);
//    }
//
//    private void removeAccountTextListener(AccountTextWatcher watcher) {
//        mUserNameView.removeTextChangedListener(watcher);
//        mPasswordView.removeTextChangedListener(watcher);
//    }


    @Override
    public void finish() {
        super.finish();
        RaeAnim.fadeIn(mBackView);
        overridePendingTransition(0, com.rae.cnblogs.R.anim.slide_out_bottom);
    }

//    @OnClick(R.id.img_edit_delete)
//    public void onPasswordDeleteClick() {
//        mPasswordView.setText("");
//    }

    /**
     * 忘记密码
     */
    @OnClick(R.id.tv_forget_password)
    public void onForgetPasswordClick() {
        AppMobclickAgent.onClickEvent(this, "ForgetPassword");
        AppRoute.jumpToWeb(this, getString(R.string.forget_password_url));
    }

    /**
     * 注册账号
     */
    @OnClick(R.id.btn_reg)
    public void onRegClick() {
        AppMobclickAgent.onClickEvent(this, "Reg");
        AppRoute.jumpToWeb(this, getString(R.string.reg_url));
    }


    /**
     * 登录点击
     */
    @OnClick(R.id.ll_login)
    public void onLoginClick() {
        if (config().hasLoginGuide()) {
            preformLogin();
        } else {
//            // 先弹键盘下去
//            InputMethodManager service = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (service != null) {
//                service.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
//            }

            mLoginContractDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    config().loginGuide();
                    preformLogin();
                }
            });
            mLoginContractDialog.show();
        }
    }

    private void preformLogin() {
//        performUserInfo();
        AppRoute.jumpToWebLogin(this);
//        mLoginPresenter.login();
//        removeAccountTextListener(mAccountTextWatcher);
//        mLoginButton.setEnabled(false);
    }


    private void performUserInfo() {
        // 开始动画
        createLoadingObservable().subscribe(new Consumer<Animation>() {
            @Override
            public void accept(Animation animation) throws Exception {
                mIsLoadingUserInfo = true;
                mLoginPresenter.loadUserInfo();
            }
        });
    }

    @OnClick(R.id.ll_login_contract)
    public void onLoginContractClick() {
//        mLoginContractDialog.setOnDismissListener(null);
//        mLoginContractDialog.show();

        HintCardDialog dialog = new HintCardDialog(this);
        dialog.setMessage(getString(R.string.login_help_message));
        dialog.setEnSureText(getString(R.string.go_now));
        dialog.setOnEnSureListener(new IAppDialogClickListener() {
            @Override
            public void onClick(IAppDialog dialog, int buttonType) {
                dialog.dismiss();
                AppRoute.jumpToWeb(getContext(), getContext().getString(R.string.url_login_help));
            }
        });
        dialog.showCloseButton();
        dialog.show();
    }


    //    @Override
//    public String getUserName() {
//        return mUserNameView.getText().toString().trim();
//    }
//
//    @Override
//    public String getPassword() {
//        return mPasswordView.getText().toString().trim();
//    }
//
    @Override
    public void onLoginSuccess(UserInfoBean userInfo) {
        mIsLoadingUserInfo = false;
        dismissLoading();
        AppUI.success(this, R.string.login_success);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onLoginError(String message) {
        mIsLoadingUserInfo = false;
        if (mErrorTime >= 3) {
            dismissLoading();
            onLoginContractClick();
            mErrorTime = 0;
            return;
        }
        mErrorTipsDialog.setMessage(message);
        mErrorTipsDialog.show();
        mErrorTime++;
    }

    @Override
    public void onLoading(String msg) {
        mLoadingMsgView.setText(msg);
    }
//
//    @Override
//    public void onLoginVerifyCodeError() {
//        onLoginCallback();
//        mErrorTipsDialog.setMessage(getString(R.string.login_verify_code_error));
//        mErrorTipsDialog.show();
//    }

//    private void onLoginCallback() {
//        mLoginButton.setEnabled(true);
//        addAccountTextListener(mAccountTextWatcher);
//        // 结束动画效果
//        stopAnim();
//    }

//    private void stopAnim() {
//        AppUI.dismiss();
//    }
//

    private Observable<Animation> createLoadingObservable() {
        return Observable.create(new ObservableOnSubscribe<Animation>() {
            @Override
            public void subscribe(final ObservableEmitter<Animation> e) throws Exception {
                Animation fadeOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
                if (mUserInfoLayout.getVisibility() == View.VISIBLE) {
                    e.onNext(fadeOut);
                    return;
                }
                mLogoView.startAnimation(fadeOut);
                mLoginLayout.startAnimation(fadeOut);
                mLogoView.setVisibility(View.GONE);
                mLoginLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);

                // 渐显
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.login_slide_in);
                mUserInfoLayout.setVisibility(View.VISIBLE);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        e.onNext(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        e.onComplete();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                mUserInfoLayout.startAnimation(animation);
            }
        });
    }

    private Observable<Animation> createDismissObservable() {
        return Observable.create(new ObservableOnSubscribe<Animation>() {

            @Override
            public void subscribe(final ObservableEmitter<Animation> e) throws Exception {
                if (mLogoView.getVisibility() == View.VISIBLE) {
                    return;
                }
                // 渐显
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.login_slide_out);
                mUserInfoLayout.startAnimation(animation);
                mProgressBar.setVisibility(View.INVISIBLE);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        e.onNext(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mUserInfoLayout.setVisibility(View.GONE);
                        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                        mLogoView.setVisibility(View.VISIBLE);
                        mLoginLayout.setVisibility(View.VISIBLE);
                        mLogoView.startAnimation(fadeIn);
                        mLoginLayout.startAnimation(fadeIn);
                        e.onComplete();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mLogoView.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {
            // 取消获取用户信息
            mIsLoadingUserInfo = false;
            mLoginPresenter.cancel();
            dismissLoading();
        }
    }

    //
//    /**
//     * 显示登录中
//     *
//     * @param msg
//     */
//    private void showLoading(String msg) {
//        IAppDialog dialog = AppUI.loading(this, msg);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                mLoginPresenter.cancel();
//                onLoginCallback();
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppRoute.REQ_CODE_WEB_LOGIN && resultCode == RESULT_OK) {
            // 登录成功，获取用户信息
            performUserInfo();

//            mErrorTime = 0;
//            showLoading(getString(R.string.loading_user_info));
//            mLoginPresenter.performUserInfo();
        }
    }

//    private class AccountTextWatcher implements TextWatcher {
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            mLoginButton.setEnabled(mUserNameView.length() > 0 && mPasswordView.length() > 0);
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    }
}
