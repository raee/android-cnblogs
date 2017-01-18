package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.NewsBean;
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
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsParser implements IApiJsonResponse {
    private final ApiUiArrayListener<Blog> mListener;

    public NewsParser(ApiUiArrayListener<Blog> listener) {
        mListener = listener;
    }

    @Override
    public void onJsonResponse(String json) {

        // 解析HTML
        List<Blog> result = new ArrayList<>();
        Document document = Jsoup.parse(json);
        Elements elements = document.select("entry");
        for (Element element : elements) {
            NewsBean m = new NewsBean();
            m.setId(element.select("id").text());
            m.setTitle(element.select("title").text());
            m.setSummary(element.select("summary").text());
            m.setPostDate(Utils.getDate(element.select("updated").text()));
            m.setUrl(element.select("link").attr("href"));
            m.setLikes(element.select("diggs").text());
            m.setViews(element.select("views").text());
            m.setComment(element.select("comments").text());
            m.setAuthor(element.select("sourceName").text());
            m.setAvatar(element.select("topicIcon").text().replace("images0.cnblogs.com/news_topic///", ""));
            m.setNews(true);
            result.add(m);
        }
        mListener.onApiSuccess(result);
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        mListener.onApiFailed(new ApiException(ApiErrorCode.valueOf(errorCode)), e == null ? ApiErrorCode.valueOf(errorCode).getMessage() : e.getMessage());
    }
}
