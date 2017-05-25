package com.rae.cnblogs.sdk.interceptor;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 响应拦截器，
 * Created by ChenRui on 2017/5/25 0025 0:12.
 */
public class ResponseInterceptor implements Interceptor {

    public static ResponseInterceptor create() {
        return new ResponseInterceptor();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        String text = bufferBody(response);
        return response;
    }

    private String bufferBody(Response response) throws IOException {
        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("UTF-8");
        return buffer.clone().readString(charset);
    }
}
