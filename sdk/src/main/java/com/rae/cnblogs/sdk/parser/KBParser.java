package com.rae.cnblogs.sdk.parser;

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
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class KBParser implements IApiJsonResponse {
    private final ApiUiArrayListener<Blog> mListener;

    public KBParser(ApiUiArrayListener<Blog> listener) {
        mListener = listener;
    }

    @Override
    public void onJsonResponse(String json) {

        // 解析HTML
        List<Blog> result = new ArrayList<>();
        Document document = Jsoup.parse(json);
        Elements elements = document.select(".kb_item");
        for (Element element : elements) {
            Blog m = new Blog();
            m.setId(Utils.getNumber(element.attr("id")));
            m.setTitle(element.select(".kb_entry").text());
            m.setSummary(element.select(".kb_summary").text());
            m.setPostDate(Utils.getDate(element.select(".kb_footer .green").text()));
            m.setUrl("http:" + element.select(".kb_entry .kb-title").attr("href"));
//            m.setLikes(element.select("diggs").text());
            m.setViews(Utils.getNumber(element.select(".kb_footer .view").text()));
            result.add(m);
        }
        mListener.onApiSuccess(result);
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        mListener.onApiFailed(new ApiException(ApiErrorCode.valueOf(errorCode)), e == null ? ApiErrorCode.valueOf(errorCode).getMessage() : e.getMessage());
    }
}
