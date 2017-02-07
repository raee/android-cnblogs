package com.rae.cnblogs.sdk.impl;

/**
 * 接口地址
 * Created by ChenRui on 2016/11/30 0030 16:46.
 */
final class ApiUrls {


    // 博客列表
    static final String API_BLOG_LIST = "http://www.cnblogs.com/mvc/AggSite/PostList.aspx";

    // 博文
    static final String API_BLOG_CONTENT = "http://wcf.open.cnblogs.com/blog/post/body/";

    // 评论列表
    static final String API_BLOG_COMMENT_LIST = "http://www.cnblogs.com/mvc/blog/GetComments.aspx";

    // 登录接口
//    static final String API_LOGIN_TOKEN = "https://api.cnblogs.com/token";

    // 用户信息
    static final String API_USER_INFO = "https://home.cnblogs.com/ajax/user/CurrentIngUserInfo";

    // 添加收藏
//    static final String OFFICIAL_API_BOOK_MARKS_ADD = "https://api.cnblogs.com/api/Bookmarks";

    // 收藏列表
//    static final String OFFICIAL_API_BOOK_MARKS_LIST = "https://api.cnblogs.com/api/Bookmarks";

    // 删除收藏
//    static final String OFFICIAL_API_BOOK_MARKS_ID_DELETE = "https://api.cnblogs.com/api/bookmarks";
//    static final String OFFICIAL_API_BOOK_MARKS_URL_DELETE = "https://api.cnblogs.com/api/Bookmarks";

    // 新闻列表
    static final String API_NEWS_LIST = "http://wcf.open.cnblogs.com/news/recent/paged/@page/20";
    static final String API_NEWS_CONTENT = "http://wcf.open.cnblogs.com/news/item/@id";
    static final String API_NEWS_COMMENT = "https://news.cnblogs.com/CommentAjax/GetComments";

    // 知识库
    static final String API_KB_LIST = "https://home.cnblogs.com/kb/page/@page/";
    static final String API_KB_LIKE = "https://kb.cnblogs.com/mvcajax/vote/VoteArticle";

    // 推荐博客
    static final String API_BLOG_LIKE = "http://www.cnblogs.com/mvc/vote/VoteBlogPost.aspx";

    // 发布博客评论
    static final String API_BLOG_COMMENT_ADD = "http://www.cnblogs.com/mvc/PostComment/Add.aspx";
    // 删除博客评论
    static final String API_BLOG_COMMENT_DELETE = "http://www.cnblogs.com/mvc/comment/DeleteComment.aspx";

    // 添加收藏
    static final String API_BOOK_MARKS_ADD = "http://wz.cnblogs.com/ajax/wz/AddWzlink";

    // 删除收藏
    static final String API_BOOK_MARKS_DELETE = "http://wz.cnblogs.com/ajax/wz/DeleteWzlink";

    // 获取收藏列表
    static final String API_BOOK_MARKS_LIST = "http://wz.cnblogs.com/my/@page.html";

    // 知识库内容
    static final String API_KB_CONTENT = "https://kb.cnblogs.com/page/@id/";

    // 新闻评论
    static final String API_NEWS_COMMENT_ADD = "https://news.cnblogs.com/Comment/InsertComment";

    // 删除新闻评论
    static final String API_NEWS_COMMENT_DELETE = "https://news.cnblogs.com/Comment/DelComment";

    // 登录
    static final String API_SIGN_IN = "https://passport.cnblogs.com/user/signin?AspxAutoDetectCookieSupport=1";

    /**
     * 新闻点赞
     */
    static final String API_NEWS_LIKE = "https://news.cnblogs.com/News/VoteNews";

    // 用户中心
    static final String API_USER_CENTER = "https://home.cnblogs.com/u/@blogApp/";

    // 关注博主
    public static final String API_FRIENDS_FOLLOW = "https://home.cnblogs.com/ajax/follow/FollowUser";

    // 取消关注博主
    public static final String API_FRIENDS_UN_FOLLOW = "https://home.cnblogs.com/ajax/follow/RemoveFollow";

    // 博主的博客列表
    public static final String API_FRIENDS_BLOG_LIST = "http://wcf.open.cnblogs.com/blog/u/@blogApp/posts/@page/20";

    // 关注列表
    public static final String API_FRIENDS_FOLLOW_LIST = "https://home.cnblogs.com/relation_users";

    // 粉丝列表
    public static final String API_FRIENDS_FANS_LIST = "https://home.cnblogs.com/relation_users";
}
