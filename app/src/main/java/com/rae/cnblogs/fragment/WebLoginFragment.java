package com.rae.cnblogs.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.rae.cnblogs.R;
import com.rae.cnblogs.widget.webclient.CnblogLoginWebViewClient;
import com.rae.cnblogs.widget.webclient.bridge.LoginListener;

/**
 * Created by ChenRui on 2017/1/19 22:06.
 */
public class WebLoginFragment extends WebViewFragment implements LoginListener {

    private CnblogLoginWebViewClient mWebViewClient;

    public static WebLoginFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        WebLoginFragment fragment = new WebLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public WebViewClient getWebViewClient() {
        mWebViewClient = new CnblogLoginWebViewClient(mWebView, mProgressBar, this);
        mWebViewClient.setLoginParams("chenrui7", "chenrui123456789");
        return mWebViewClient;
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(getContext(), "登录成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginCodeError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginCodeBitmap(Bitmap bitmap) {
        ImageView img = (ImageView) getActivity().findViewById(R.id.img_test);
        img.setImageBitmap(bitmap);
    }
}
