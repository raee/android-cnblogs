package com.rae.cnblogs.sdk.db;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.rae.cnblogs.sdk.bean.Category;

import java.util.List;


/**
 * 分类表
 * Created by ChenRui on 2016/12/1 00:24.
 */
public class DbCategory extends DbCnblogs<Category> {


    public void clear() {
        new Delete().from(Category.class).execute();
    }

    /**
     * 重置分类
     *
     * @param list 数据
     */
    public void reset(final List<Category> list) {

        executeTransaction(new Runnable() {
            @Override
            public void run() {

                for (Category category : list) {
                    category.save();
                }
            }
        });

    }

    public List<Category> list() {
        return new Select().from(Category.class).execute();
    }
}
