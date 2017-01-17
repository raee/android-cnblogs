package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogCommentItemAdapter;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.cnblogs.widget.RaeDrawerLayout;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

    @BindView(R.id.rv_test)
    RaeRecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        bindView();

        BlogCommentItemAdapter adapter = new BlogCommentItemAdapter();
        List<BlogComment> data = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("00.##");
        for (int i = 1; i <= 60; i++) {
            BlogComment m = new BlogComment();
            m.setBody("I AM CONTENT " + df.format(i));
            m.setAuthorName("ITEM " + df.format(i));
            data.add(m);
        }
        adapter.invalidate(data);
        mRecyclerView.setAdapter(adapter);
        mRaeDrawerLayout.setDrawerHandler(new RaeDrawerLayout.RaeDrawerHandler() {
            @Override
            public boolean checkCanDoDrawer(RaeDrawerLayout view, MotionEvent event) {
                return mRecyclerView.isOnTop();
            }
        });
    }

    private void log(String msg) {
        mTextView.setText(msg);
    }

    @OnClick(R.id.btn_test)
    void onTestClick() {

//        CnblogsApiFactory.getBlogApi(this).getComment(1, "6219795", "everhad", new ApiUiArrayListener<BlogComment>() {
//            @Override
//            public void onApiFailed(ApiException ex, String msg) {
//                Log.e("api", msg);
//            }
//
//            @Override
//            public void onApiSuccess(List<BlogComment> data) {
//                for (BlogComment comment : data) {
//                    Log.i("api", "评论；" + comment.getBody());
//                }
//            }
//        });

        mRaeDrawerLayout.toggle();

//        mRaeDrawerLayout.swipeUp();
//
//        RequestQueue requestQueue = VolleyManager.newRequestQueue(getApplicationContext());
//        StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.210.97:808/rae/index.php/welcome/cachetest", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.i("rae", "接口返回：" + response);
//                log(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error != null)
//                    error.printStackTrace();
//                log("请求错误：" + error);
//            }
//        }) {
//
//            private String mResponseString;
//
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                Log.w("rae", "parseNetworkResponse:" + response.statusCode);
//
//                Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
//                String parsed;
//                try {
//                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                } catch (UnsupportedEncodingException e) {
//                    parsed = new String(response.data);
//                }
//                if (cacheEntry == null) {
//                    cacheEntry = new Cache.Entry();
//                }
//
//                final long cacheHitButRefreshed = 1000; // 缓存超时，没到超时时间不会发请求。
//                final long cacheExpired = 24 * 60 * 60 * 1000; // 缓存过期时间
//                long now = System.currentTimeMillis();
//                final long softExpire = now + cacheHitButRefreshed;
//                final long ttl = now + cacheExpired;
//                cacheEntry.data = response.data;
//                cacheEntry.softTtl = softExpire;
//                cacheEntry.ttl = ttl;
//                String headerValue;
//                headerValue = response.headers.get("Date");
//                if (headerValue != null) {
//                    cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                }
//                headerValue = response.headers.get("Last-Modified");
//                if (headerValue != null) {
//                    cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                }
//                cacheEntry.responseHeaders = response.headers;
//
//                return Response.success(parsed, cacheEntry);
//            }
//
//
//            @Override
//            protected void deliverResponse(String response) {
//                super.deliverResponse(response);
//                mResponseString = response;
//            }
//
//            @Override
//            public void deliverError(VolleyError error) {
//                // 如果有缓存，杜缓存
//                if (mResponseString != null) {
//                    return;
//                }
//                super.deliverError(error);
//            }
//
//            @Override
//            public Request<?> setRequestQueue(RequestQueue requestQueue) {
//                Log.w("rae", "setRequestQueue");
//                mResponseString = null;
//                return super.setRequestQueue(requestQueue);
//            }
//        };
//        requestQueue.add(request);
    }


}
