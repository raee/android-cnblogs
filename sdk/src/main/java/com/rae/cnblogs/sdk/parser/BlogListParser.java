package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;
import com.rae.core.sdk.ApiUiArrayListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客列表解析器
 * Created by ChenRui on 2016/11/30 00:13.
 */
public class BlogListParser extends HtmlParser<BlogBean> {

    DbBlog mDbBlog;

    public BlogListParser(ApiUiArrayListener<BlogBean> listener) {
        super(listener);
        mDbBlog = new DbBlog();
    }

    @Override
    protected void onParseHtmlDocument(Document document) {
        // 解析HTML
        List<BlogBean> result = new ArrayList<>();
        Elements elements = document.select(".post_item");
        for (Element item : elements) {

            Elements element = item.select(".post_item_body");

            String id = Utils.getNumber(item.select(".digg .diggnum").attr("id"));
            String title = element.select(".titlelnk").text(); // 标题
            String url = element.select(".titlelnk").attr("href"); // 原文链接
            String avatar = getAvatar(element.select(".pfs").attr("src")); // 头像地址
            String summary = element.select(".post_item_summary").text(); // 摘要
            String author = element.select(".lightblue").text(); // 作者
            String authorUrl = element.select(".lightblue").attr("href"); // 作者博客地址
            String blogApp = Utils.getBlogApp(authorUrl);
            String comment = Utils.getCount(Utils.getNumber(element.select(".article_comment .gray").text())); // 评论
            String views = Utils.getCount(Utils.getNumber(element.select(".article_view .gray").text())); // 阅读
            String likes = Utils.getCount(element.select(".diggnum").text()); // 点赞或者是推荐
            String date = Utils.getDate(element.select(".post_item_foot").text()); // 发布时间

            // 博客ID为空不添加
            if (TextUtils.isEmpty(id)) {
                continue;
            }

            BlogBean m = new BlogBean();
            m.setBlogId(id);
            m.setTitle(title);
            m.setUrl(url);
            m.setAvatar(avatar);
            m.setSummary(summary);
            m.setAuthor(author);
            m.setAuthorUrl(authorUrl);
            m.setBlogApp(blogApp);
            m.setComment(comment);
            m.setViews(views);
            m.setPostDate(date);
            m.setLikes(likes);
            m.setBlogType(BlogType.BLOG.getTypeName());

            cacheThumbUrls(m);
            result.add(m);
        }

        notifySuccess(result);
    }

    /**
     * 缓存小图
     *
     * @param m
     */
    void cacheThumbUrls(BlogBean m) {
        // 小图处理：从数据库中获取
        BlogBean dbBlog = mDbBlog.getBlog(m.getBlogId());
        if (dbBlog != null && !TextUtils.isEmpty(dbBlog.getThumbUrls())) {
            m.setThumbUrls(dbBlog.getThumbUrls()); // 存在有小图
        } else {
            // 获取小图
            UserBlogInfo blogInfo = mDbBlog.get(m.getBlogId());
            if (dbBlog != null && blogInfo != null && !TextUtils.isEmpty(blogInfo.getContent())) {
                m.setThumbUrls(createThumbUrls(blogInfo.getContent()));
                dbBlog.setThumbUrls(m.getThumbUrls());
                // 更新小图
                mDbBlog.updateBlog(dbBlog);
            }
        }
    }

    /**
     * 获取小图
     *
     * @param content 博文
     */
    String createThumbUrls(String content) {
        try {
            List<String> result = new ArrayList<>();
            Elements elements = Jsoup.parse(content).select("img");
            for (Element element : elements) {
                String src = element.attr("src");

                // 过滤一些没有用的图片
                if (TextUtils.isEmpty(src) || src.contains(".gif")) {
                    continue;
                }

                result.add(Utils.getUrl(src));
            }

            return JSON.toJSONString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    String getAvatar(String src) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }
        if (!src.startsWith("http")) {
            src = "http:" + src;
        }
        return src;
    }

}
