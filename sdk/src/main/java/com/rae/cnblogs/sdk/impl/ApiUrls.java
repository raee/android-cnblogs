package com.rae.cnblogs.sdk.impl;

/**
 * 接口地址
 * Created by ChenRui on 2016/11/30 0030 16:46.
 */
final class ApiUrls {

    static final String RAE_API_DOMAIN = "http://www.timeplay.cn/cnblogs/index.php";

    // 博客列表
    static final String API_URL_HOME = "http://www.cnblogs.com/mvc/AggSite/PostList.aspx";

    // 博文
    static final String API_URL_CONTENT = "http://wcf.open.cnblogs.com/blog/post/body/";

    // 评论列表
    static final String API_URL_COMMENT = "http://www.cnblogs.com/mvc/blog/GetComments.aspx";

    // 登录接口
    static final String API_LOGIN_TOKEN = "https://api.cnblogs.com/token";

    // 用户信息
    static final String API_USER_INFO = "https://api.cnblogs.com/api/Users";

    // 添加收藏
    static final String API_BOOK_MARKS_ADD = "https://api.cnblogs.com/api/Bookmarks";


    // 首页广告
    static final String RAE_API_URL_LAUNCHER_AD = "/app/ad/launcher";


    // 收藏列表
    static final String API_BOOK_MARKS_LIST = "https://api.cnblogs.com/api/Bookmarks";

    // 删除收藏
    static final String API_BOOK_MARKS_ID_DELETE = "https://api.cnblogs.com/api/bookmarks";
    static final String API_BOOK_MARKS_URL_DELETE = "https://api.cnblogs.com/api/Bookmarks";
    // 新闻列表
    static final String API_NEWS_LIST = "http://wcf.open.cnblogs.com/news/recent/paged/@page/20";
    static final String API_NEWS_CONTENT = "http://wcf.open.cnblogs.com/news/item/@id";
    static final String API_NEWS_COMMENT = "https://news.cnblogs.com/CommentAjax/GetComments";

}
