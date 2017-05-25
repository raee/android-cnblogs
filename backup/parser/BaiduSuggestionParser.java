package com.rae.cnblogs.sdk.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.net.RaeSimpleJsonResponse;

import java.util.Arrays;

/**
 * 百度搜索建议解析
 * Created by ChenRui on 2017/2/8 0008 9:30.
 */
public class BaiduSuggestionParser extends RaeSimpleJsonResponse<String> {

    public BaiduSuggestionParser(ApiUiArrayListener<String> listener) {
        super(String.class, listener);
    }

    @Override
    protected void notifyApiSuccess(String json) {
        json = json.replace("cnblogs(", "").replace(");", "");
        JSONObject object = JSON.parseObject(json);
        if (object.containsKey("s")) {
            JSONArray array = object.getJSONArray("s");
            String[] result = new String[array.size()];
            result = array.toArray(result);
            mArrayListener.onApiSuccess(Arrays.asList(result));
        } else {
            notifyApiError(ApiErrorCode.ERROR_EMPTY_DATA, null);
        }
    }
}
