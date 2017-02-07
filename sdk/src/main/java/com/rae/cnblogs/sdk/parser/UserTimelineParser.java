package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.core.sdk.ApiUiArrayListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户动态
 * Created by ChenRui on 2017/2/7 0007 14:00.
 */
public class UserTimelineParser extends HtmlParser<UserFeedBean> {

    public UserTimelineParser(ApiUiArrayListener<UserFeedBean> arrayListener) {
        super(arrayListener);
    }

    @Override
    protected void onParseHtmlDocument(Document document) {
        List<UserFeedBean> result = new ArrayList<>();
        Elements elements = document.select(".feed_item");
        for (Element element : elements) {
            UserFeedBean m = new UserFeedBean();
            m.setAvatar(Utils.getUrl(element.select(".feed_avatar a img").attr("src")));
            m.setAuthor(element.select(".feed_author").text());
            m.setBlogApp(element.select(".feed_author").attr("href").replace("/u/", "").replace("/", ""));
            m.setAction(getAction(element.select(".feed_title").text()));
            m.setTitle(element.select(".feed_title a").eq(1).text());
            m.setFeedDate(Utils.getDate(element.select(".feed_date").text()));
            m.setContent(element.select(".feed_desc").text());
            result.add(m);
        }
        notifySuccess(result);
    }

    private String getAction(String text) {
        return text.substring(text.indexOf(" "), text.indexOf("：")).trim();
    }
}
