package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.BlogCommentModel;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

//import com.alibaba.fastjson.JSON;

/**
 * 博文解析
 * Created by ChenRui on 2016/11/30 0030 17:00.
 */
public class BlogCommentParser implements IJsonParser<List<BlogCommentBean>> {

    private Gson mGson = new Gson();

    @Override
    public List<BlogCommentBean> parse(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        BlogCommentModel model = mGson.fromJson(json, BlogCommentModel.class);
        if (model == null) {
            return null;
        }
        String html = model.getCommentsHtml();
        if (TextUtils.isEmpty(html)) {
            return null;
        }

        // 解析XML
        Document document = Jsoup.parse(html);

        Elements feeds = document.select(".feedbackItem");
        List<BlogCommentBean> result = new ArrayList<>();
        for (Element feed : feeds) {
            String id = ApiUtils.getNumber(feed.select(".layer").attr("href"));
            String authorName = feed.select("#a_comment_author_" + id).text();
            String blogApp = ApiUtils.getBlogApp(feed.select("#a_comment_author_" + id).attr("href"));
            String date = ApiUtils.getDate(feed.select(".comment_date").text());
            String body = feed.select(".blog_comment_body").text();
            String like = ApiUtils.getNumber(feed.select(".comment_digg").text());
            String unlike = ApiUtils.getNumber(feed.select(".comment_bury").text());
            String avatar = feed.select(".comment_" + id + "_avatar").text();

            BlogCommentBean m = new BlogCommentBean();
            m.setId(id);
            m.setAuthorName(authorName);
            m.setAvatar(avatar);
            m.setBlogApp(blogApp);
            m.setDate(date);
            m.setBody(body);
            m.setLike(like);
            m.setUnlike(unlike);

            result.add(m);
        }
        return result;
    }
}
