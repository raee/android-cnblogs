package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.utils.ApiUtils;
import com.rae.cnblogs.sdk.bean.BookmarksBean;
import com.rae.core.Rae;
import com.rae.core.sdk.ApiUiArrayListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏列表解析
 * Created by ChenRui on 2017/1/22 0022 14:44.
 */
public class BookmarksParser extends HtmlParser<BookmarksBean> {

    public BookmarksParser(ApiUiArrayListener<BookmarksBean> arrayListener) {
        super(arrayListener);
    }

    @Override
    protected void onParseHtmlDocument(Document document) {
        Elements elements = document.select(".wz_item_content");
        List<BookmarksBean> result = new ArrayList<>();
        for (Element element : elements) {
            String id = ApiUtils.getNumber(element.select(".list_block").attr("id"));
            String title = element.select(".list_block h2 a").text();
            String url = element.select(".list_block .link_content .url a").attr("href");
            String summary = element.select(".list_block .link_content .summary").text();
            BookmarksBean m = new BookmarksBean(title, summary, url);
            m.setWzLinkId(Rae.parseInt(id, 0));
            result.add(m);
        }

        mArrayListener.onApiSuccess(result);
    }
}
