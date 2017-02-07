package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;

import org.jsoup.nodes.Document;

/**
 * 用户信息解析
 * Created by ChenRui on 2017/2/7 0007 15:31.
 */
public class UserInfoParser extends AbsUserInfoParser<UserInfoBean> {

    public UserInfoParser(ApiUiListener<UserInfoBean> listener) {
        super(listener);
    }

    @Override
    protected void onParseHtmlDocument(Document document) {
        UserInfoBean result = new UserInfoBean();
        onParseUserInfo(result, document);
        if (TextUtils.isEmpty(result.getBlogApp())) {
            mListener.onApiFailed(new ApiException(ApiErrorCode.ERROR_EMPTY_DATA), ApiErrorCode.ERROR_EMPTY_DATA.getMessage());
        } else {
            mListener.onApiSuccess(result);
        }
    }
}
