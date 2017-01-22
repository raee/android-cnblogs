package com.rae.cnblogs.sdk.parser;

import android.text.Html;

import com.rae.core.sdk.ApiUiListener;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 新闻内容解析器
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsContentParser extends HtmlParser<String> {

    public NewsContentParser(ApiUiListener<String> listener) {
        super(listener);
    }

    @Override
    protected void onParseHtmlDocument(Document document) {
        Elements elements = document.select("Content");
        mListener.onApiSuccess(Html.fromHtml(elements.html()).toString().replace("src=\"//", "src=\"http://"));
    }

}
