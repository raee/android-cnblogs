package com.rae.cnblogs.sdk.db;

import android.util.Log;

import com.activeandroid.query.Select;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

import java.util.List;

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


    public boolean exists(String blogId) {
        return new Select().from(Blog.class).where("blogId=?", blogId).exists();
    }

    public void addAll(final List<Blog> blogs) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 执行插入
                executeTransaction(new Runnable() {
                    @Override
                    public void run() {
                        for (Blog blog : blogs) {
                            // 查找是否已经有了，有了就跳过
                            if (exists(blog.getBlogId())) {
                                Log.w("rae-db", "跳过：" + blog.getBlogId() + " = " + blog.getTitle());
                                continue;
                            }

                            Log.i("rae-db", "插入数据库：" + blog.getBlogId() + " = " + blog.getTitle());
                            blog.save();
                        }
                    }
                });

            }
        }).start();
    }

    public List<Blog> findAll() {
        return new Select().from(Blog.class).execute();
    }
}
