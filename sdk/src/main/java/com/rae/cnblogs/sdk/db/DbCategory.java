package com.rae.cnblogs.sdk.db;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;


/**
 * 分类表
 * Created by ChenRui on 2016/12/1 00:24.
 */
public class DbCategory extends DbCnblogs<CategoryBean> {


    public void clear() {
        new Delete().from(CategoryBean.class).execute();
    }

    /**
     * 重置分类
     *
     * @param list 数据
     */
    public void reset(final List<CategoryBean> list) {

        executeTransaction(new Runnable() {
            @Override
            public void run() {

                for (CategoryBean category : list) {
                    category.save();
                }
            }
        });

    }

    public List<CategoryBean> list() {
        return new Select().from(CategoryBean.class).execute();
    }


    public List<CategoryBean> getUserList() {
        return new Select().from(CategoryBean.class).where("isHide=?", 0).orderBy("orderNo DESC").execute();
    }
}
