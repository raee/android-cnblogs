package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.parser.BlogCommentParser;
import com.rae.cnblogs.sdk.parser.BlogContentParser;
import com.rae.cnblogs.sdk.parser.BlogListParser;
import com.rae.cnblogs.sdk.parser.CnBlogsWebApiResponse;
import com.rae.cnblogs.sdk.parser.KBContentParser;
import com.rae.cnblogs.sdk.parser.KBListParser;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;

import java.util.HashMap;

/**
 * 博客接口
 * Created by ChenRui on 2016/11/30 00:06.
 */
public class BlogApiImpl extends CnblogsBaseApi implements IBlogApi {


    public BlogApiImpl(Context context) {
        super(context);
    }

    @Override
    protected boolean enablePerCache(String url, HashMap<String, String> params) {

        // 博客列表
        if (ApiUrls.API_BLOG_LIST.equals(url) && params.get("PageIndex").equals("1")) {
            return true;
        }

        // 博客评论列表
        if (ApiUrls.API_BLOG_COMMENT_LIST.equals(url) && params.get("pageIndex").equals("1")) {
            return true;
        }

        // 知识库
        if (ApiUrls.API_KB_LIST.equals(url) && params.get("PageIndex").equals("1")) {
            return true;
        }

        return super.enablePerCache(url, params); // 允许首次从缓存加载
    }

    @Override
    public void getBlogList(int page, String type, String parentId, String categoryId, ApiUiArrayListener<BlogBean> listener) {
        post(ApiUrls.API_BLOG_LIST,
                newParams().add("CategoryType", type)
                        .add("ParentCategoryId", parentId)
                        .add("CategoryId", categoryId)
                        .add("PageIndex", page)
                        .add("ItemListActionName", "PostList"),
                new BlogListParser(listener)
        );
    }

    @Override
    public void getBlogContent(String id, ApiUiListener<String> listener) {
        get(ApiUrls.API_BLOG_CONTENT + id, null, new BlogContentParser(listener));
    }

    @Override
    public void getBlogComments(int page, String id, String blogApp, ApiUiArrayListener<BlogCommentBean> listener) {
        get(ApiUrls.API_BLOG_COMMENT_LIST, newParams().add("postId", id).add("blogApp", blogApp).add("pageIndex", page), new BlogCommentParser(listener));
    }

    @Override
    public void getKbArticles(int page, ApiUiArrayListener<BlogBean> listener) {
        post(ApiUrls.API_KB_LIST.replace("@page", String.valueOf(page)), newParams().add("PageIndex", page), new KBListParser(listener));
    }

    @Override
    public void getKbContent(String id, ApiUiListener<String> listener) {
        get(ApiUrls.API_KB_CONTENT.replace("@id", id), null, new KBContentParser(listener));
    }

    @Override
    public void likeBlog(String id, String blogApp, ApiUiListener<Void> listener) {

        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }

        postWithJsonBody(ApiUrls.API_BLOG_LIKE,
                newParams()
                        .add("postId", id)
                        .add("blogApp", blogApp)
                        .add("voteType", "Digg")
                        .add("isAbandoned", "false"),
                new CnBlogsWebApiResponse<>(Void.class, listener));
    }

    @Override
    public void unLikeBlog(String id, String blogApp, ApiUiListener<Void> listener) {

        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }

        postWithJsonBody(ApiUrls.API_BLOG_LIKE,
                newParams()
                        .add("postId", id)
                        .add("blogApp", blogApp)
                        .add("voteType", "Digg")
                        .add("isAbandoned", "true"),
                new CnBlogsWebApiResponse<>(Void.class, listener));
    }

    @Override
    public void likeKb(String id, ApiUiListener<Void> listener) {
        xmlHttpRequestWithJsonBody(ApiUrls.API_KB_LIKE, newParams().add("contentId", id).add("voteType", "Digg"), new CnBlogsWebApiResponse<>(Void.class, listener));
    }

    @Override
    public void addBlogComment(String id, String blogApp, String parentCommentId, String content, ApiUiListener<Void> listener) {

        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }

        postWithJsonBody(ApiUrls.API_BLOG_COMMENT_ADD,
                newParams()
                        .add("postId", id)
                        .add("blogApp", blogApp)
                        .add("body", content)
                        .add("parentCommentId", parentCommentId),
                new CnBlogsWebApiResponse<>(Void.class, listener));
    }

    @Override
    public void addBlogComment(String id, String blogApp, BlogCommentBean comment, String content, ApiUiListener<Void> listener) {

        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }

        // {"blogApp":"silenttiger","postId":6323406,"body":"@TCG2008\n[quote]网页应用都差不多，什么QQ上的应用宝，空间的应用啊，百度轻应用...主要都是为了引流，你一个小公司当然要从微信百度上引导别人使用你的产品啦。[/quote]\naa","parentCommentId":"3608347"}
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(comment.getBlogApp());
        sb.append("\n");
        sb.append("[quote]");
        sb.append(comment.getBody());
        sb.append("[/quote]");
        sb.append("\n");
        sb.append(content);
        content = sb.toString();
        addBlogComment(id, blogApp, comment.getId(), content, listener);
    }

    @Override
    public void deleteBlogComment(String commentId, ApiUiListener<Void> listener) {

        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }

        postWithJsonBody(ApiUrls.API_BLOG_COMMENT_DELETE,
                newParams()
                        .add("commentId", commentId)
                        .add("pageIndex", "0"),
                new CnBlogsWebApiResponse<>(Void.class, listener));
    }


}
