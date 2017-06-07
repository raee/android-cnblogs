package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 用户信息解析
 * Created by ChenRui on 2017/2/7 0007 15:31.
 */
public class UserInfoParser extends AbsUserInfoParser<UserInfoBean> {

    @Override
    public UserInfoBean parse(String html) {
        Document document = Jsoup.parse(html);
        UserInfoBean result = new UserInfoBean();
        onParseUserInfo(result, document);
        if (TextUtils.isEmpty(result.getBlogApp())) {
            return null;
        }

        // 保存登录信息
        UserProvider.getInstance().setLoginUserInfo(result);

        return result;
    }
}
