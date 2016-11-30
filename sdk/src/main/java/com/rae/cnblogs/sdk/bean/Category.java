package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 分类
 * Created by ChenRui on 2016/11/30 0030 17:20.
 */
public class Category implements Parcelable {
    private String categoryId;
    private String parentId;
    private String name;
    private String type;
    private  int orderNo; // 排序

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Category() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.parentId);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeInt(this.orderNo);
    }

    protected Category(Parcel in) {
        this.categoryId = in.readString();
        this.parentId = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.orderNo = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
