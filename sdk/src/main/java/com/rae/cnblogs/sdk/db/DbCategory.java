package com.rae.cnblogs.sdk.db;

import com.rae.cnblogs.sdk.bean.Category;

import java.util.List;


/**
 * 分类表
 * Created by ChenRui on 2016/12/1 00:24.
 */
public class DbCategory extends DbCnblogs<Category> {


    public void clear() {
    }

    /**
     * 重置分类
     *
     * @param list 数据
     */
    public void reset(final List<Category> list) {
        clear();

    }

    public List<Category> list() {
        return null;
    }
}
