package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rae.cnblogs.presenter.IPostMomentContract;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.model.ImageMetaData;
import com.rae.cnblogs.sdk.model.MomentMetaData;
import com.rae.cnblogs.service.MomentIntentService;
import com.rae.swift.Rx;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布闪存
 * Created by ChenRui on 2017/10/27 0027 14:37.
 */
public class PostMomentPresenterImpl extends BasePresenter<IPostMomentContract.View> implements IPostMomentContract.Presenter {

    IMomentApi mMomentApi;
    IBlogApi mBlogApi;

    public PostMomentPresenterImpl(Context context, IPostMomentContract.View view) {
        super(context, view);
        mMomentApi = getApiProvider().getMomentApi();
        mBlogApi = getApiProvider().getBlogApi();
    }

    @Override
    public void start() {
        super.start();
        if (isLogin()) {
            createObservable(mBlogApi.checkBlogIsOpen())
                    .subscribe(new ApiDefaultObserver<Boolean>() {
                        @Override
                        protected void onError(String message) {
                            mView.onLoadBlogOpenStatus(false);
                        }

                        @Override
                        protected void accept(Boolean value) {
                            mView.onLoadBlogOpenStatus(value);
                        }
                    });
        }
    }

    @Override
    public boolean post() {
        String content = mView.getContent().trim();
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        String imageContent = withImageContent(mView.getImageUrls());
        int imageLength = 0;
        String noUrlIng;
        if (!TextUtils.isEmpty(imageContent)) {
            noUrlIng = content + imageContent;
            imageLength = replaceText(imageContent).length();
        } else {
            noUrlIng = content;
        }

        noUrlIng = replaceText(noUrlIng);
        if (noUrlIng.length() > 240) {
            mView.onPostMomentFailed("请精简一下内容，文字加图片不要超过240字。\n当前图片占用字符数：" + imageLength + "个\n当前字符数: " + noUrlIng.length() + "个");
            return false;
        }

        int size = Rx.getCount(mView.getImageUrls());

        if (size > 0) {
            // 发表图文，组合参数，发送到后台上传
            Intent intent = new Intent(mContext, MomentIntentService.class);
            MomentMetaData metaData = new MomentMetaData();
            metaData.content = content;
            metaData.images = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ImageMetaData m = new ImageMetaData();
                m.localPath = mView.getImageUrls().get(i);
                metaData.images.add(m);
            }
            intent.putExtra(Intent.EXTRA_TEXT, metaData);
            mContext.startService(intent);

            mView.onPostMomentInProgress();
            return false;
        } else {

            // 加上客户端标志
            content = "[来自Android客户端] " + content;

            createObservable(mMomentApi.publish(content, 1))
                    .subscribe(new ApiDefaultObserver<Empty>() {
                        @Override
                        protected void onError(String message) {
                            mView.onPostMomentFailed(message);
                        }

                        @Override
                        protected void accept(Empty empty) {
                            mView.onPostMomentSuccess();
                        }
                    });
        }
        return true;
    }

    @Nullable
    private String withImageContent(List<String> images) {
        if (Rx.isEmpty(images)) return null;
        // 格式：内容 #img图片地址#end
        StringBuilder sb = new StringBuilder();
        sb.append("#img");
        JSONArray array = new JSONArray();
        int size = images.size();
        for (int i = 0; i < size; i++) {
            // 占位，用于计算字符大小
            array.put("t.cn/1234567");
        }
        sb.append(array.toString());
        sb.append("#end");
        return sb.toString();
    }

    protected String replaceText(String text) {
        if (TextUtils.isEmpty(text)) return text;
        return text
                .replaceAll("(http|ftp|https):\\/\\/([^\\/:,，]+)(:\\d+)?(\\/[^\\u0391-\\uFFE5\\s,]*)?", "")
                .replaceAll("(\\s)+", "")
                .replaceAll("[^\\x00-\\xff]", "aa");
    }
}
