package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.BookmarksBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 收藏列表解析
 * Created by ChenRui on 2017/1/22 0022 14:44.
 */
public class BookmarksParser implements IHtmlParser<List<BookmarksBean>> {


    @Override
    public List<BookmarksBean> parse(String html) throws IOException {
        Elements elements = Jsoup.parse(html).select(".wz_item_content");
        List<BookmarksBean> result = new ArrayList<>();
        for (Element element : elements) {
            String id = ApiUtils.getNumber(element.select(".list_block").attr("id"));
            String title = element.select(".list_block h2 a").text();
            String url = element.select(".list_block .link_content .url a").attr("href");
            String summary = element.select(".list_block .link_content .summary").text();
            BookmarksBean m = new BookmarksBean(title, summary, url);
            m.setWzLinkId(parseInt(id));
            result.add(m);
        }
        return result;
    }

    private int parseInt(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
