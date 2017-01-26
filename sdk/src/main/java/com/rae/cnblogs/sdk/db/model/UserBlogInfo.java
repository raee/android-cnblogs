package com.rae.cnblogs.sdk.db.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 用户的博客信息
 * Created by ChenRui on 2017/1/25 0025 17:04.
 */
@Table(name = "blog_info", id = "blogId")
public class UserBlogInfo extends Model {


    @Column
    private String blogId;

    @Column
    private boolean isRead;
    @Column
    private boolean isLiked;
    @Column
    private boolean isBookmarks;
    @Column
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public boolean isBookmarks() {
        return isBookmarks;
    }

    public void setBookmarks(boolean bookmarks) {
        isBookmarks = bookmarks;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
