package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝以及关注用户列表解析器
 * Created by ChenRui on 2017/2/7 0007 17:41.
 */
public class FriendsListParser implements IJsonParser<List<UserInfoBean>> {

    @Override
    public List<UserInfoBean> parse(String json) {
        try {
            // 解析公共部分
            JSONObject obj = new JSONObject(json);
            if (!obj.has("Users")) {
                return null;
            }
            JSONArray users = obj.getJSONArray("Users");
            int len = users.length();
            List<UserInfoBean> result = new ArrayList<>();
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
                m.setRemarkName(user.isNull("Remark") ? null : user.getString("Remark"));
                m.setAvatar(user.isNull("IconName") ? null : ApiUtils.getUrl(user.getString("IconName")));
                result.add(m);
            }


            return result;

        } catch (JSONException ignored) {

        }
        return null;
    }
}
