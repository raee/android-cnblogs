package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;
import android.util.Log;

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
        if (!matcher.find()) {
            return text;
        }

        text = matcher.group();
        String time = text.split(" ")[1];

        // 时间间隔
        long span = (System.currentTimeMillis() - parseDate(text).getTime()) / 1000;

        // 今天过去的时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long today = (System.currentTimeMillis() - calendar.getTimeInMillis()) / 1000;
        if (span < 0) {
        } else if (span < 60) {
            text = "刚刚";
        } else if (span < 3600) {
            text = (span / 60) + "分钟前";
        } else if (span < today) {
            text = "今天 " + time;
        } else if (span < today + 86400) {
            text = "昨天 " + time;
        } else if (span < today + 2 * 86400) {
            text = "前天 " + time;
        }


        return text;
    }

    private Date parseDate(String text) {
        Date target;
        try {
            target = RaeDateUtil.parse(text, "yyyy-MM-dd HH:mm");
        } catch (Exception e) {
            Log.e("rae", "解析出错!", e);
            target = new Date();
        }
        return target;
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
