package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 闪存解析器
 * Created by ChenRui on 2017/9/25 0025 17:16.
 */
public class MomentParser implements IHtmlParser<List<MomentBean>> {

    @Override
    public List<MomentBean> parse(Document doc, String html) throws IOException {
        List<MomentBean> result = new ArrayList<>();

        Elements elements = doc.select(".entry_a");
        elements.addAll(doc.select(".entry_b"));

        for (Element element : elements) {
            MomentBean m = new MomentBean();
            String id = ApiUtils.getNumber(element.select(".feed_body").attr("id"));
            if (TextUtils.isEmpty(id)) continue; // 跳过ID为空的

            m.setId(id); // 主键
            m.setAuthorName(element.select(".ing-author").text()); // 作者名称
            m.setAvatar(ApiUtils.getUrl(element.select(".feed_avatar a img").attr("src"))); //头像地址
            m.setContent(element.select(".ing_body").text()); // 内容
            m.setPostTime(element.select(".ing_time").text()); // 发布时间
            m.setBlogApp(ApiUtils.getBlogApp(element.select(".ing-author").attr("href"))); // blogApp

            result.add(m);
        }


        return result;
    }
}
