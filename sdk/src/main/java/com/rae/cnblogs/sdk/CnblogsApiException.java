package com.rae.cnblogs.sdk;

import java.io.IOException;

/**
 * 博客园接口错误
 * Created by ChenRui on 2017/6/11 0011 0:36.
 */
public class CnblogsApiException extends IOException {

    private int code;

    public CnblogsApiException() {
    }

    public int getCode() {
        return code;
    }

    public CnblogsApiException(int code, String message) {
        super(message);
        this.code = code;
    }


    public CnblogsApiException(String message) {
        super(message);
    }

    public CnblogsApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public CnblogsApiException(Throwable cause) {
        super(cause);
    }
}
