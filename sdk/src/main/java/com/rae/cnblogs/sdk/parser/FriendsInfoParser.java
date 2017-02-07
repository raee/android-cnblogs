package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.core.sdk.ApiUiListener;

import org.jsoup.nodes.Document;

/**
 * 博主信息解析器
 * Created by ChenRui on 2017/2/7 0007 15:31.
 */
public class FriendsInfoParser extends AbsUserInfoParser<FriendsInfoBean> {

    public FriendsInfoParser(ApiUiListener<FriendsInfoBean> listener) {
        super(listener);
    }

    @Override
    protected void onParseHtmlDocument(Document document) {
        FriendsInfoBean result = new FriendsInfoBean();
        onParseUserInfo(result, document);
        result.setFollows(document.select("#following_count").text());
        result.setFans(document.select("#follower_count").text());
        result.setFollowed(!document.select("#followedPanel").attr("style").contains("none"));
        mListener.onApiSuccess(result);
    }
}
