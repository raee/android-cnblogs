package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.rae.cnblogs.sdk.ICategoryApi;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.sdk.db.DbCategory;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.core.Rae;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 博客分类
 * Created by ChenRui on 2016/11/30 0030 17:36.
 */
public class CategoryApiImpl extends CnblogsBaseApi implements ICategoryApi {

    public CategoryApiImpl(Context context) {
        super(context);
    }

    @Override
    public void getCategory(ApiUiArrayListener<CategoryBean> listener) {
        // 从数据库中获取
        DbCategory db = DbFactory.getInstance().getCategory();
        List<CategoryBean> list = db.list();

        // 没有数据,开始初始化数据
        if (Rae.isEmpty(list)) {
            list = getFromAssets();
            db.reset(list);
        }

        // 再次判断数据
        if (Rae.isEmpty(list)) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_EMPTY_DATA), null);
            return;
        }

        listener.onApiSuccess(list);

    }

    private List<CategoryBean> getFromAssets() {
        String json = readString("category.json");
        if (json == null) return null;
        try {
            return JSON.parseArray(json, CategoryBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readString(String fileName) {
        try {
            InputStream stream = mContext.getAssets().open(fileName);
            BufferedInputStream bis = new BufferedInputStream(stream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[128];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            String result = outputStream.toString();
            outputStream.close();
            bis.close();
            stream.close();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
