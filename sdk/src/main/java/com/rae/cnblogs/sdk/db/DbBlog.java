package com.rae.cnblogs.sdk.db;

import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.query.Select;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

import java.util.List;

/**
 * 博客数据库
 * Created by ChenRui on 2017/1/25 0025 16:56.
 */
public class DbBlog extends DbCnblogs {

    DbBlog() {
    }

    public UserBlogInfo get(String blogId) {
        if (TextUtils.isEmpty(blogId)) return null;
        return new Select().from(UserBlogInfo.class).where("blogId=?", blogId).executeSingle();
    }

    public BlogBean getBlog(String blogId) {
        return new Select().from(BlogBean.class).where("blogId=?", blogId).executeSingle();
    }

    public void saveBlogInfo(UserBlogInfo m) {
        m.save();
    }


    public boolean exists(String blogId) {
        return new Select().from(BlogBean.class).where("blogId=?", blogId).exists();
    }

    public void addAll(final List<BlogBean> blogs) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 执行插入
                executeTransaction(new Runnable() {
                    @Override
                    public void run() {
                        for (BlogBean blog : blogs) {
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

    public List<BlogBean> findAll() {
        return new Select().from(BlogBean.class).execute();
    }

    /**
     * 获取没有内容的列表
     *
     * @return
     */
    public List<BlogBean> findAllWithoutBlogContnet() {
        return new Select().from(BlogBean.class).as("blog").leftJoin(UserBlogInfo.class).as("info").on("blog.blogId=info.blogId").where("info.content is NULL").execute();
    }

    /**
     * 删除收藏
     *
     * @param url 路径
     */
    public void removeBookmarks(final String url) {
        // 找到已经收藏的
        new Thread(new Runnable() {
            @Override
            public void run() {

                UserBlogInfo model = new Select()
                        .from(UserBlogInfo.class).as("a")
                        .leftJoin(BlogBean.class).as("b")
                        .on("a.blogId=b.blogId")
                        .where("a.isBookmarks=?", 1)
                        .and("b.url=?", url)
                        .executeSingle();


                if (model == null || TextUtils.isEmpty(model.getBlogId())) return;
                model.setBookmarks(false);
                model.save();
            }
        }).start();
    }


    public void updateBlog(BlogBean m) {
        m.save();
    }
}
