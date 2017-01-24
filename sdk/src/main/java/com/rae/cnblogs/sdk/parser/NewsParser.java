package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.NewsBean;
import com.rae.core.sdk.ApiUiArrayListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsParser extends HtmlParser<Blog> {

    public NewsParser(ApiUiArrayListener<Blog> arrayListener) {
        super(arrayListener);
    }

    @Override
    protected void onParseHtmlDocument(Document document) {
        // 解析HTML
        List<Blog> result = new ArrayList<>();
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
        mArrayListener.onApiSuccess(result);
    }
}
