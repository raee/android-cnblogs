package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.core.sdk.ApiUiArrayListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识库列表解析器
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class KBListParser extends HtmlParser<Blog> {

    public KBListParser(ApiUiArrayListener<Blog> arrayListener) {
        super(arrayListener);
    }

    @Override
    protected void onParseHtmlDocument(Document document) {

        // 解析HTML
        List<Blog> result = new ArrayList<>();
        Elements elements = document.select(".kb_item");
        for (Element element : elements) {
            Blog m = new Blog();
            m.setId(Utils.getNumber(element.attr("id")));
            m.setTitle(element.select(".kb_entry .kb-title").text());
            m.setTag(element.select(".kb_entry .deepred").text());
            m.setSummary(element.select(".kb_summary").text());
            m.setPostDate(Utils.getDate(element.select(".kb_footer .green").text()));
            m.setUrl("http:" + element.select(".kb_entry .kb-title").attr("href"));
            m.setViews(Utils.getNumber(element.select(".kb_footer .view").text()));
            m.setKb(true);
            result.add(m);
        }
        mArrayListener.onApiSuccess(result);
    }
}
