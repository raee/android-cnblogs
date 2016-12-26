package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 博客实体
 * Created by ChenRui on 2016/11/28 23:45.
 */
public class Blog implements Parcelable {
    private String title;
    private String url;
    private String avatar;
    private String summary;
    private String author;
    private String authorUrl;
    private String comment;
    private String views;
    private String postDate;
    private String id;
    private String blogApp;

    public String getBlogApp() {
        return blogApp;
    }

    public void setBlogApp(String blogApp) {
        this.blogApp = blogApp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    private String likes; // 点赞

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public Blog() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Blog && !TextUtils.isEmpty(id)) {
            return TextUtils.equals(id, ((Blog) obj).getId());
        }
        return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.avatar);
        dest.writeString(this.summary);
        dest.writeString(this.author);
        dest.writeString(this.authorUrl);
        dest.writeString(this.comment);
        dest.writeString(this.views);
        dest.writeString(this.postDate);
        dest.writeString(this.id);
        dest.writeString(this.blogApp);
        dest.writeString(this.content);
        dest.writeString(this.likes);
    }

    protected Blog(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.avatar = in.readString();
        this.summary = in.readString();
        this.author = in.readString();
        this.authorUrl = in.readString();
        this.comment = in.readString();
        this.views = in.readString();
        this.postDate = in.readString();
        this.id = in.readString();
        this.blogApp = in.readString();
        this.content = in.readString();
        this.likes = in.readString();
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel source) {
            return new Blog(source);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };
}
