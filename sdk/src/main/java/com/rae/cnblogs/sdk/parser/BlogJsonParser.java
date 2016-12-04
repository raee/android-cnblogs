package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.net.IApiJsonResponse;
import com.rae.core.utils.RaeDateUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            String id = getNumber(item.select(".digg .diggnum").attr("id"));
            String title = element.select(".titlelnk").text(); // 标题
            String url = element.select(".titlelnk").attr("href"); // 原文链接
            String avatar = getAvatar(element.select(".pfs").attr("src")); // 头像地址
            String summary = element.select(".post_item_summary").text(); // 摘要
            String author = element.select(".lightblue").text(); // 作者
            String authorUrl = element.select(".lightblue").attr("href"); // 作者博客地址
            String comment = getCount(getNumber(element.select(".article_comment .gray").text())); // 评论
            String views = getCount(getNumber(element.select(".article_view .gray").text())); // 阅读
            String likes = getCount(element.select(".diggnum").text()); // 点赞或者是推荐
            String date = getDate(element.select(".post_item_foot").text()); // 发布时间

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

    private String getDate(String text) {

        String regx = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";
        Matcher matcher = Pattern.compile(regx).matcher(text);
        if (matcher.find()) {
            text = matcher.group();

            // 有可能出现这种情况: 2016-07-28 20:02
            Date target;
            if (text.split(":").length < 3) {
                try {
                    target = RaeDateUtil.parse(text, "yyyy-MM-dd HH:mm");
                } catch (ParseException e) {
                    target = new Date();
                }
            } else {
                target = RaeDateUtil.parseDefaultDate(text);
            }

            long d = target.getTime();
            Date current = new Date();
            long span = System.currentTimeMillis() - d;
            String time = RaeDateUtil.format(target, "HH:mm");

            // 今天
            if (span < 86400) {
                text = String.format("今天 %s", time);
            } else if (span < 2 * 86400) {
                text = String.format("昨天 %s", time);
            } else if (span < 3 * 86400) {
                text = String.format("前天 %s", time);
            } else if (span < 365 * 86400) {
                text = text.replace(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + "-", "");
            }

        }

        return text;
    }

    private String getNumber(String text) {
        Matcher matcher = Pattern.compile("\\d+").matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return text;
    }

    private String getCount(String text) {
        if (TextUtils.isEmpty(text)) return "0";
        return text.trim();
    }
}
