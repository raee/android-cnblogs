package com.rae.cnblogs.sdk.db;

import com.activeandroid.query.Select;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

/**
 * 博客数据库
 * Created by ChenRui on 2017/1/25 0025 16:56.
 */
public class DbBlog extends DbCnblogs<Blog> {

    public UserBlogInfo get(String blogId) {
        return new Select().from(UserBlogInfo.class).where("blogId=?", blogId).executeSingle();
    }

    public void saveBlogInfo(UserBlogInfo m) {
        m.save();
    }

}
