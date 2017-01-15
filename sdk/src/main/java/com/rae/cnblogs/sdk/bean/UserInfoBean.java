package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户信息
 * Created by ChenRui on 2017/1/14 02:21.
 */
public class UserInfoBean implements Parcelable {
    /**
     * UserId : b73f02a0-081c-4127-adea-6e475340feb6
     * SpaceUserId : 2
     * BlogId : 3
     * DisplayName : sample string 4
     * Face : sample string 5
     * Avatar : sample string 6
     * Seniority : sample string 7
     * BlogApp : sample string 8
     */

    private String UserId;
    private int SpaceUserId;
    private int BlogId;
    private String DisplayName;
    private String Face;
    private String Avatar;
    private String Seniority;
    private String BlogApp;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public int getSpaceUserId() {
        return SpaceUserId;
    }

    public void setSpaceUserId(int SpaceUserId) {
        this.SpaceUserId = SpaceUserId;
    }

    public int getBlogId() {
        return BlogId;
    }

    public void setBlogId(int BlogId) {
        this.BlogId = BlogId;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String DisplayName) {
        this.DisplayName = DisplayName;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String Face) {
        this.Face = Face;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String Avatar) {
        this.Avatar = Avatar;
    }

    public String getSeniority() {
        return Seniority;
    }

    public void setSeniority(String Seniority) {
        this.Seniority = Seniority;
    }

    public String getBlogApp() {
        return BlogApp;
    }

    public void setBlogApp(String BlogApp) {
        this.BlogApp = BlogApp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UserId);
        dest.writeInt(this.SpaceUserId);
        dest.writeInt(this.BlogId);
        dest.writeString(this.DisplayName);
        dest.writeString(this.Face);
        dest.writeString(this.Avatar);
        dest.writeString(this.Seniority);
        dest.writeString(this.BlogApp);
    }

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.UserId = in.readString();
        this.SpaceUserId = in.readInt();
        this.BlogId = in.readInt();
        this.DisplayName = in.readString();
        this.Face = in.readString();
        this.Avatar = in.readString();
        this.Seniority = in.readString();
        this.BlogApp = in.readString();
    }

    public static final Parcelable.Creator<UserInfoBean> CREATOR = new Parcelable.Creator<UserInfoBean>() {
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
