package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * 博客实体
 * Created by ChenRui on 2016/11/28 23:45.
 */
@Table(name = "blogs")
public class Blog extends Model implements Parcelable {
    @Column
    private String title;
    @Column
    private String url;
    @Column
    private String avatar;
    @Column
    private String summary;
    @Column
    private String author;
    @Column
    private String authorUrl;
    @Column
    private String comment;
    @Column
    private String views;
    @Column
    private String postDate;
    @Column
    private String blogId;
    @Column
    private String blogApp;
    @Column
    private String tag; // 标签

    @Column
    private String thumbUrls; // 预览小图,JSON 格式，比如：["http://img.cnblogs.com/a.jpg","http://img.cnblogs.com/b.jpg"]

    /**
     * 博客类型，参考取值{@link BlogType#getTypeName()}
     */
    @Column
    private String blogType;
    private List<String> mThumbList;

    public String getBlogType() {
        return blogType;
    }

    public void setBlogType(String blogType) {
        this.blogType = blogType;
    }

    public String getBlogApp() {
        return blogApp;
    }

    public String getThumbUrls() {
        return thumbUrls;
    }

    public void setThumbUrls(String thumbUrls) {
        this.thumbUrls = thumbUrls;
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
        super();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getBlogId() {
        return blogId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Blog && !TextUtils.isEmpty(blogId)) {
            return TextUtils.equals(blogId, ((Blog) obj).getBlogId());
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
        dest.writeString(this.blogId);
        dest.writeString(this.blogApp);
        dest.writeString(this.tag);
        dest.writeString(this.thumbUrls);
        dest.writeString(this.blogType);
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
        this.blogId = in.readString();
        this.blogApp = in.readString();
        this.tag = in.readString();
        this.thumbUrls = in.readString();
        this.blogType = in.readString();
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


    /**
     * 获取小图
     */
    @Nullable
    public List<String> getThumbs() {
        if (TextUtils.isEmpty(thumbUrls)) return null;
        try {
            if (mThumbList == null) {
                mThumbList = JSON.parseArray(thumbUrls, String.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mThumbList;
    }
}
