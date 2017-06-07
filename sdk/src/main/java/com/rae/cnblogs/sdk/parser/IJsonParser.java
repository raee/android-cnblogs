package com.rae.cnblogs.sdk.parser;

/**
 * JSON解析器
 * Created by ChenRui on 2017/6/7 0007 19:16.
 */
public interface IJsonParser<T> {
    T parse(String json);
}
