package com.rae.cnblogs.sdk.bean;

import java.util.List;

/**
 * 闪存
 * Created by ChenRui on 2017/9/25 0025 17:03.
 */
public class MomentBean {

    // 标志
    private String id;

    // 作者昵称
    private String authorName;

    // 作者头像
    private String avatar;

    private String blogApp;

    // 发布时间
    private String postTime;

    // 回复作者名称
    private String replyName;

    // 回复作者blogApp
    private String replyBlogApp;

    private String content;

    // 回复的评论
    private List<MomentBean> comments;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBlogApp() {
        return blogApp;
    }

    public void setBlogApp(String blogApp) {
        this.blogApp = blogApp;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public String getReplyBlogApp() {
        return replyBlogApp;
    }

    public void setReplyBlogApp(String replyBlogApp) {
        this.replyBlogApp = replyBlogApp;
    }


    public List<MomentBean> getComments() {
        return comments;
    }

    public void setComments(List<MomentBean> comments) {
        this.comments = comments;
    }
}
