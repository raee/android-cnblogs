package com.rae.cnblogs.sdk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.rae.cnblogs.sdk.bean.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类表
 * Created by ChenRui on 2016/12/1 00:24.
 */
public class DbCategory extends DbCnblogs {
    private final static String sTbName = "categories";

    public DbCategory(Context context) {
        super(context);
    }

    public void clear() {
        db().delete(sTbName, "categoryId > 0", null);
    }

    /**
     * 重置分类
     *
     * @param list 数据
     */
    public void reset(List<Category> list) {
        clear();
        for (Category data : list) {
            ContentValues values = new ContentValues();
            values.put("categoryId", data.getCategoryId());
            values.put("parentId", data.getParentId());
            values.put("NAME", data.getName());
            values.put("C_TYPE", data.getType());
            values.put("ORDER_NO", data.getOrderNo());
            db().insert(sTbName, null, values);
        }
    }

    public List<Category> list() {
        List<Category> list = new ArrayList<>();
        Cursor cursor = db().rawQuery(String.format("select * from %s", sTbName), null);
        while (cursor.moveToNext()) {
            Category m = new Category();
            m.setCategoryId(cursor.getString(cursor.getColumnIndex("categoryId")));
            m.setParentId(cursor.getString(cursor.getColumnIndex("parentId")));
            m.setName(cursor.getString(cursor.getColumnIndex("NAME")));
            m.setType(cursor.getString(cursor.getColumnIndex("C_TYPE")));
            m.setOrderNo(cursor.getInt(cursor.getColumnIndex("ORDER_NO")));
            list.add(m);
        }
        cursor.close();
        return list;
    }
}
