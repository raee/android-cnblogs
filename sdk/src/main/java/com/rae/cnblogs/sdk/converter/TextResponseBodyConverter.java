package com.rae.cnblogs.sdk.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonParser;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.parser.IHtmlParser;
import com.rae.cnblogs.sdk.parser.IJsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 文本类型的响应解析器
 * Created by ChenRui on 2017/5/25 0025 23:46.
 */
public class TextResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Type type;
    private final TypeAdapter<T> mAdapter;
    private final Gson mGson;
    private IJsonParser<T> mJsonParser;
    private IHtmlParser<T> mHtmlParser;

    @SuppressWarnings("unchecked")
    public TextResponseBodyConverter(Type type, Annotation[] annotations, Gson gson, TypeAdapter<T> adapter) {
        this.type = type;
        mAdapter = adapter;
        mGson = gson;
        for (Annotation annotation : annotations) {
            if (annotation instanceof Parser) {
                Class<?> cls = ((Parser) annotation).value();
                try {
                    mHtmlParser = (IHtmlParser<T>) cls.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            if (annotation instanceof JsonParser) {
                Class<?> cls = ((JsonParser) annotation).value();
                try {
                    mJsonParser = (IJsonParser<T>) cls.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        // 解析
        String text = value.string();
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        text = text.trim();
        if (text.startsWith("{") || text.startsWith("[")) {
            // 解析JSON
            return json2Entity(text);
        }

        return html2Entity(text);
    }

    /**
     * HTML convert to entity
     */
    @SuppressWarnings("unchecked")
    private T html2Entity(String text) throws IOException {
        if (TextUtils.equals(text, "true") && type == Empty.class) {
            return (T) Empty.value();
        }
        if (TextUtils.equals(text, "false") && type == Empty.class) {
            return (T) Empty.value();
        }


        if (mHtmlParser == null) {
            throw new IOException("HTML 解析器为空！");
        }

        return mHtmlParser.parse(text);
    }

    /**
     * JSON String convert to entity
     */
    @SuppressWarnings("unchecked")
    private T json2Entity(String text) throws IOException {

        // 交给自定义的JSON解析
        if (mJsonParser != null) {
            return mJsonParser.parse(text);
        }


        // 删除评论的时候会返回true or false
        if (TextUtils.equals(text, "true") && type == Empty.class) {
            return (T) Empty.value();
        }

        if (TextUtils.equals(text, "false") && type == Empty.class) {
            throw new IOException("删除评论失败");
        }

        if (text.contains("用户登录")) {
            throw new IOException("用户尚未登录");
        }

        try {
            JSONObject obj = new JSONObject(text);
            boolean isSuccess = false;
            String message = null;
            Object data = null;

            if (obj.has("IsSuccess")) {
                isSuccess = obj.getBoolean("IsSuccess");
            }
            if (obj.has("IsSucceed")) {
                isSuccess = obj.getBoolean("IsSucceed");
            }
            if (obj.has("success")) {
                isSuccess = obj.getBoolean("success");
            }
            if (obj.has("Message")) {
                message = obj.getString("Message");
            }
            if (obj.has("message")) {
                message = obj.getString("message");
            }
            if (obj.has("Data")) {
                data = obj.get("Data");
            }
            if (obj.has("data")) {
                data = obj.get("data");
            }
            if (isSuccess && !obj.isNull("data") && data != null) {
                text = data.toString();
                JsonReader jsonReader = mGson.newJsonReader(new StringReader(text));
                return mAdapter.read(jsonReader);

            } else if (isSuccess && type == Void.class) {
                return null;
            } else if (isSuccess && type == Empty.class) {
                return (T) Empty.value();
            } else if (isSuccess && obj.isNull("data")) {
                throw new CnblogsApiException("数据为空");
            } else {
                message = TextUtils.isEmpty(message) ? "未知错误" : Jsoup.parse(message).text();
                throw new IOException(message);
            }
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }
}
