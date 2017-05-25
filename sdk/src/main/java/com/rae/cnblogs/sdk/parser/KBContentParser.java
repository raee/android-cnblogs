package com.rae.cnblogs.sdk.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 知识库内容解析器
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class KBContentParser implements IHtmlParser<String> {

    @Override
    public String parse(String html) {
        Document document = Jsoup.parse(html);
        return document.select(".contents_main").html();
    }
}
