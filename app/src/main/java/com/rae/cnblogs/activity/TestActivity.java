package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.rae.cnblogs.R;
import com.rae.cnblogs.widget.RaeDrawerLayout;
import com.rae.core.sdk.net.VolleyManager;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ChenRui on 2016/12/3 18:06.
 */
public class TestActivity extends BaseActivity {

    @BindView(R.id.tv_console)
    TextView mTextView;

    @BindView(R.id.test)
    RaeDrawerLayout mRaeDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        bindView();
    }

    private void log(String msg) {
        mTextView.setText(msg);
    }

    @OnClick(R.id.btn_test)
    void onTestClick() {

        mRaeDrawerLayout.swipeUp();

        RequestQueue requestQueue = VolleyManager.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.210.97:808/rae/index.php/welcome/cachetest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("rae", "接口返回：" + response);
                log(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null)
                    error.printStackTrace();
                log("请求错误：" + error);
            }
        }) {

            private String mResponseString;

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.w("rae", "parseNetworkResponse:" + response.statusCode);

                Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                if (cacheEntry == null) {
                    cacheEntry = new Cache.Entry();
                }

                final long cacheHitButRefreshed = 1000; // 缓存超时，没到超时时间不会发请求。
                final long cacheExpired = 24 * 60 * 60 * 1000; // 缓存过期时间
                long now = System.currentTimeMillis();
                final long softExpire = now + cacheHitButRefreshed;
                final long ttl = now + cacheExpired;
                cacheEntry.data = response.data;
                cacheEntry.softTtl = softExpire;
                cacheEntry.ttl = ttl;
                String headerValue;
                headerValue = response.headers.get("Date");
                if (headerValue != null) {
                    cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                }
                headerValue = response.headers.get("Last-Modified");
                if (headerValue != null) {
                    cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                }
                cacheEntry.responseHeaders = response.headers;

                return Response.success(parsed, cacheEntry);
            }


            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
                mResponseString = response;
            }

            @Override
            public void deliverError(VolleyError error) {
                // 如果有缓存，杜缓存
                if (mResponseString != null) {
                    return;
                }
                super.deliverError(error);
            }

            @Override
            public Request<?> setRequestQueue(RequestQueue requestQueue) {
                Log.w("rae", "setRequestQueue");
                mResponseString = null;
                return super.setRequestQueue(requestQueue);
            }
        };
        requestQueue.add(request);
    }


}
