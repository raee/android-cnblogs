package com.rae.cnblogs.sdk.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.core.Rae;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.net.RaeSimpleJsonResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝以及关注用户列表解析器
 * Created by ChenRui on 2017/2/7 0007 17:41.
 */
public class FriendsListParser extends RaeSimpleJsonResponse<UserInfoBean> {

    public FriendsListParser(ApiUiArrayListener<UserInfoBean> listener) {
        super(UserInfoBean.class, listener);
    }

    @Override
    protected void notifyApiSuccess(String json) {
        // 解析公共部分
        JSONObject obj = JSON.parseObject(json);
        if (!obj.containsKey("Users")) {
            notifyApiError(ApiErrorCode.ERROR_EMPTY_DATA, "没有获取到用户信息");
            return;
        }
        JSONArray users = obj.getJSONArray("Users");
        int len = users.size();
        List<UserInfoBean> result = new ArrayList<>(      );
        for (int i = 0; i < len; i++) {
            /*
                {
                "DisplayName":"RJ",
                "Alias":"cs_net",
                "Remark":null,
                "IconName":"//pic.cnblogs.com/face/u130671.gif"
                }
            */
            UserInfoBean m = new UserInfoBean();
            JSONObject user = users.getJSONObject(i);
            m.setDisplayName(user.getString("DisplayName"));
            m.setBlogApp(user.getString("Alias"));
            m.setRemarkName(user.getString("Remark"));
            m.setAvatar(Utils.getUrl(user.getString("IconName")));
            result.add(m);
        }

        if (Rae.isEmpty(result)) {
            notifyApiError(ApiErrorCode.ERROR_EMPTY_DATA, null);
        } else {
            mArrayListener.onApiSuccess(result);
        }
    }
}
