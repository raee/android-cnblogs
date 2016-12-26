package com.rae.cnblogs.sdk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 博客园本地数据库
 * Created by ChenRui on 2016/12/1 00:24.
 */
public abstract class DbCnblogs extends SQLiteOpenHelper {
    protected Context mContext;

    public DbCnblogs(Context context) {
        super(context, "cnblogs.db", null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.beginTransaction();
        try {

            // 分类表
            db.execSQL("create table categories(categoryId VARCHAR(128) PRIMARY KEY, parentId VARCHAR(128), NAME VARCHAR(128), C_TYPE VARCHAR(18), ORDER_NO INT)");

            // 广告表
            db.execSQL("create table ads(ad_id VARCHAR(128) PRIMARY KEY,ad_name VARCHAR(1024),ad_type VARCHAR(128),jump_type VARCHAR(128),ad_url VARCHAR(128),image_url VARCHAR(4000),ad_end_date VARCHAR(128))");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int version) {

    }

    protected SQLiteDatabase db() {
        return getWritableDatabase();
    }

    protected String readString(Cursor cursor, String name) {
        int index = cursor.getColumnIndex(name);
        return cursor.getString(index);
    }
}
