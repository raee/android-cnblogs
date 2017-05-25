package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.utils.ApiUtils;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.core.sdk.ApiUiArrayListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 搜索博客列表解析
 * Created by ChenRui on 2017/2/8 0008 10:05.
 */
public class SearchBlogListParser extends BlogListParser {

    private final BlogType mBlogType;

    public SearchBlogListParser(ApiUiArrayListener<BlogBean> listener, BlogType type) {
        super(listener);
        mBlogType = type;
    }

    @Override
    protected void onParseHtmlDocument(Document document) {
        // 解析HTML
        List<BlogBean> result = new ArrayList<>();
        Elements elements = document.select(".searchItem");
        for (Element element : elements) {

            String id = getId(element.select(".searchURL").text());

            String title = element.select(".searchItemTitle").html(); // 标题
            String url = element.select(".searchURL").text(); // 原文链接
//            String avatar = getAvatar(element.select(".pfs").attr("src")); // 头像地址
            String summary = element.select(".searchCon").html(); // 摘要
            String author = element.select(".searchItemInfo-userName").text(); // 作者
            String authorUrl = element.select(".searchItemInfo-userName a").attr("href"); // 作者博客地址
            String blogApp = ApiUtils.getBlogApp(authorUrl);
            String comment = ApiUtils.getCount(ApiUtils.getNumber(element.select(".searchItemInfo-comments").text())); // 评论
            String views = ApiUtils.getCount(ApiUtils.getNumber(element.select(".searchItemInfo-views").text())); // 阅读
            String likes = ApiUtils.getCount(ApiUtils.getNumber(element.select(".searchItemInfo-good").text())); // 点赞或者是推荐
            String date = ApiUtils.getDate(element.select(".searchItemInfo-publishDate").text()); // 发布时间

            // 博客ID为空不添加
            if (TextUtils.isEmpty(id)) {
                continue;
            }

            BlogBean m = new BlogBean();
            m.setBlogId(id);
            m.setTitle(title);
            m.setUrl(url);
//            m.setAvatar(avatar);
            m.setSummary(summary);
            m.setAuthor(author);
            m.setAuthorUrl(authorUrl);
            m.setBlogApp(blogApp);
            m.setComment(comment);
            m.setViews(views);
            m.setPostDate(date);
            m.setLikes(likes);
            m.setBlogType(mBlogType.getTypeName());

            cacheThumbUrls(m);
            result.add(m);
        }

        notifySuccess(result);
    }

    private String getId(String text) {
        if (TextUtils.isEmpty(text)) return null;

        switch (mBlogType) {
            case BLOG:
                Matcher matcher = Pattern.compile("/\\d+\\.html").matcher(text);
                if (matcher.find()) {
                    return matcher.group().replace(".html", "").replace("/", "");
                }
                break;
            default:
                return ApiUtils.getNumber(text);
        }

        return null;
    }
}
