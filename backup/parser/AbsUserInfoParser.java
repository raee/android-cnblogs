package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.utils.ApiUtils;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.core.sdk.ApiUiListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户信息解析
 * Created by ChenRui on 2017/2/7 0007 15:31.
 */
public abstract class AbsUserInfoParser<T> extends HtmlParser<T> {

    public AbsUserInfoParser(ApiUiListener<T> listener) {
        super(listener);
    }

    protected void onParseUserInfo(UserInfoBean result, Document document) {
        Elements scripts = document.select("script");
        for (Element script : scripts) {
            String text = script.html();
            if (text.contains("currentUserId")) {
                Matcher matcher = Pattern.compile("=.*;").matcher(text);
                if (matcher.find()) {
                    // = "7659f49a-cd56-df11-ba8f-001cf0cd104b";
                    String group = matcher.group().replace("=", "").replace("\"", "").replace(";", "").trim();
                    result.setUserId(group);
                    break;
                }
            }
        }

        result.setAvatar(ApiUtils.getUrl(document.select(".img_avatar").attr("src")));
        result.setBlogApp(ApiUtils.getBlogApp(document.select(".gray").text()));
        result.setDisplayName(document.select(".display_name").text());
        result.setRemarkName(document.select("#remarkId").text());
    }
}
