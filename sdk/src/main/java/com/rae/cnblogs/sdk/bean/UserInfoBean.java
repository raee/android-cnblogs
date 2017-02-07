package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户信息
 * Created by ChenRui on 2017/1/14 02:21.
 */
public class UserInfoBean implements Parcelable {

    /**
     * 用户ID，不同于blogApp
     */
    private String userId;

    private String blogApp;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 昵称
     */
    private String displayName;

    /**
     * 备注名称
     */
    private String remarkName;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.blogApp);
        dest.writeString(this.avatar);
        dest.writeString(this.displayName);
        dest.writeString(this.remarkName);
    }

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.userId = in.readString();
        this.blogApp = in.readString();
        this.avatar = in.readString();
        this.displayName = in.readString();
        this.remarkName = in.readString();
    }

    public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel source) {
            return new UserInfoBean(source);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };
}
