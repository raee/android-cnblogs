package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonBody;
import com.rae.cnblogs.sdk.JsonParser;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.parser.SimpleUserInfoParser;
import com.rae.cnblogs.sdk.parser.UserInfoParser;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 用户接口，用户管理对应{@link UserProvider} 类
 * Created by ChenRui on 2017/1/14 01:00.
 */
public interface IUserApi {
    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     */
    @POST(ApiUrls.API_SIGN_IN)
    @FormUrlEncoded
    @Headers({
            JsonBody.CONTENT_TYPE,
            JsonBody.XHR,
            "Cookie:AspxAutoDetectCookieSupport=1"
    })
    Observable<Empty> login(@Field("input1") String userName, @Field("input2") String password);

    /**
     * 获取用户信息，不需再保存登录信息了。你可以通过{@link UserProvider} 来管理用户
     */
    @POST(ApiUrls.API_USER_INFO)
    @Headers({JsonBody.XHR})
    @JsonParser(SimpleUserInfoParser.class)
    Observable<UserInfoBean> getUserInfo();

    @POST(ApiUrls.API_USER_CENTER)
    @Headers({JsonBody.XHR})
    @Parser(UserInfoParser.class)
    Observable<UserInfoBean> getUserInfo(@Path("blogApp") String blogApp);

}
