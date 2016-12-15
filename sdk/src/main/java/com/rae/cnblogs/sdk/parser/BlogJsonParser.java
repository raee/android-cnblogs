package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.net.IApiJsonResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客列表解析器
 * Created by ChenRui on 2016/11/30 00:13.
 */
public class BlogJsonParser implements IApiJsonResponse {
    private final ApiUiArrayListener<Blog> mListener;

    public BlogJsonParser(ApiUiArrayListener<Blog> listener) {
        mListener = listener;
    }

    /**
     * @param json
     */
    @Override
    public void onJsonResponse(String json) {
        // 解析HTML
        List<Blog> result = new ArrayList<>();
        Document document = Jsoup.parse(json);
        Elements elements = document.select(".post_item");
        for (Element item : elements) {

            Elements element = item.select(".post_item_body");

            String id = Utils.getNumber(item.select(".digg .diggnum").attr("id"));
            String title = element.select(".titlelnk").text(); // 标题
            String url = element.select(".titlelnk").attr("href"); // 原文链接
            String avatar = getAvatar(element.select(".pfs").attr("src")); // 头像地址
            String summary = element.select(".post_item_summary").text(); // 摘要
            String author = element.select(".lightblue").text(); // 作者
            String authorUrl = element.select(".lightblue").attr("href"); // 作者博客地址
            String comment = Utils.getCount(Utils.getNumber(element.select(".article_comment .gray").text())); // 评论
            String views = Utils.getCount(Utils.getNumber(element.select(".article_view .gray").text())); // 阅读
            String likes = Utils.getCount(element.select(".diggnum").text()); // 点赞或者是推荐
            String date = Utils.getDate(element.select(".post_item_foot").text()); // 发布时间

            Blog m = new Blog();
            m.setId(id);
            m.setTitle(title);
            m.setUrl(url);
            m.setAvatar(avatar);
            m.setSummary(summary);
            m.setAuthor(author);
            m.setAuthorUrl(authorUrl);
            m.setComment(comment);
            m.setViews(views);
            m.setPostDate(date);
            m.setLikes(likes);

            result.add(m);

        }

        mListener.onApiSuccess(result);

    }

    private String getAvatar(String src) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }
        if (!src.startsWith("http")) {
            src = "http:" + src;
        }
        return src;
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        ApiErrorCode code = ApiErrorCode.valueOf(errorCode);
        mListener.onApiFailed(new ApiException(), e == null ? code.getMessage() : e.getMessage());
    }


}
