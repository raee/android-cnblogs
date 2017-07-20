package com.rae.cnblogs.sdk.interceptor;

import android.text.TextUtils;
import android.webkit.CookieManager;

import com.rae.cnblogs.sdk.JsonBody;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 请求拦截器，用来处理请求参数
 * Created by ChenRui on 2017/5/25 0025 0:12.
 */
public class RequestInterceptor implements Interceptor {

    public static RequestInterceptor create() {
        return new RequestInterceptor();
    }

    CookieManager mCookieManager;

    public RequestInterceptor() {
        mCookieManager = CookieManager.getInstance();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 请求处理参数处理
        Request request = chain.request();

        // 带上COOKIE，保持登录需要用到
        Request.Builder newBuilder = request.newBuilder();
        String cookie = CookieManager.getInstance().getCookie("http://www.cnblogs.com");
        if (!TextUtils.isEmpty(cookie)) {
            newBuilder.addHeader("Cookie", cookie);
        }
        if ("post".equalsIgnoreCase(request.method())) {
            // 将URL的参数转换到body里面去
            newBuilder = convertParamToBody(request, newBuilder);
        }

        request = newBuilder.build();
        return chain.proceed(request);
    }

    private Request.Builder convertParamToBody(Request request, Request.Builder newBuilder) {
        HttpUrl url = request.url();
        RequestBody body = request.body();
        Set<String> names = url.queryParameterNames();
        if (names == null) {
            return newBuilder;
        }

        if (body == null) {
            body = new FormBody.Builder().build();
        }

        // 这里有两种类型：formBody jsonBody
        MediaType mediaType = body.contentType();
        String contentType = mediaType == null ? null : mediaType.toString();

        // 表单类型
        if (body instanceof FormBody) {
            FormBody.Builder builder = copyFormBody((FormBody) body);
            HttpUrl.Builder httUrlBuilder = url.newBuilder();
            for (String name : names) {
                String value = url.queryParameter(name);
                builder.add(name, value);
                httUrlBuilder.removeAllQueryParameters(name);
            }
            FormBody formBody = builder.build();
            return newBuilder.url(httUrlBuilder.build()).post(formBody);
        }

        // JSON BODY
        if (TextUtils.equals(contentType, JsonBody.MEDIA_TYPE)) {
            JsonBody.Builder builder = new JsonBody.Builder();
            HttpUrl.Builder httUrlBuilder = url.newBuilder();

            // 加上原来的参数
            Buffer sink = new Buffer();
            try {
                body.writeTo(sink);
                String params = sink.readString(Charset.defaultCharset());
                HttpUrl oldUrl = httUrlBuilder.query(URLDecoder.decode(params)).build();
                Set<String> oldNames = oldUrl.queryParameterNames();
                for (String name : oldNames) {
                    String value = oldUrl.queryParameter(name);
                    builder.add(name, value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // URL的参数
            for (String name : names) {
                String value = url.queryParameter(name);
                builder.add(name, value);
                httUrlBuilder.removeAllQueryParameters(name);
            }

            JsonBody jsonBody = builder.build();
            return newBuilder.url(httUrlBuilder.build()).post(jsonBody);
        }

        return newBuilder;
    }


    /**
     * 复制表单数据
     */
    private FormBody.Builder copyFormBody(FormBody body) {
        FormBody.Builder builder = new FormBody.Builder();
        int size = body.size();
        for (int i = 0; i < size; i++) {
            builder.add(body.name(i), body.value(i));
        }
        return builder;
    }
}
