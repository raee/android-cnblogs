package com.rae.cnblogs.sdk.parser;

import com.rae.core.sdk.ApiUiListener;

import org.jsoup.nodes.Document;

/**
 * 知识库内容解析器
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class KBContentParser extends HtmlParser<String> {

    public KBContentParser(ApiUiListener<String> listener) {
        super(listener);
    }

    @Override
    protected void onParseHtmlDocument(Document document) {
        String content = document.select(".contents_main").html();
        mListener.onApiSuccess(content);
    }
}
