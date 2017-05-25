package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.utils.ApiUtils;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.core.sdk.ApiUiArrayListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenRui on 2017/2/8 0008 9:47.
 */
public class SearchBloggerParser extends HtmlParser<UserInfoBean> {

    public SearchBloggerParser(ApiUiArrayListener<UserInfoBean> arrayListener) {
        super(arrayListener);
    }

    @Override
    protected void onParseHtmlDocument(Document document) {
        List<UserInfoBean> result = new ArrayList<>();
        Elements elements = document.select("entry");
        for (Element element : elements) {
            UserInfoBean m = new UserInfoBean();
            m.setBlogApp(element.select("blogapp").text());
            m.setDisplayName(element.select("title").text());
            m.setAvatar(element.select("avatar").text());
            m.setJoinDate(ApiUtils.getDate(element.select("updated").text()));
            result.add(m);
        }
        notifySuccess(result);
    }
}
