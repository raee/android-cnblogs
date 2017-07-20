package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻评论
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsCommentParser implements IHtmlParser<List<BlogCommentBean>> {

    @Override
    public List<BlogCommentBean> parse(Document document, String json) {
        // 解析HTML
        List<BlogCommentBean> result = new ArrayList<>();
        Elements elements = document.select(".user_comment");
        for (Element element : elements) {
            BlogCommentBean m = new BlogCommentBean();
            m.setId(ApiUtils.getNumber(element.select(".comment_main").attr("id")));
            m.setAuthorName(element.select(".comment-author").text());
            m.setLike(ApiUtils.getCount(".agree_" + m.getId()));
            m.setBody(element.select(".comment_main").text());
            m.setDate(ApiUtils.getDate(element.select(".time").text().replace("发表于 ", "").trim()));
            result.add(m);
        }
        return result;
    }
}
