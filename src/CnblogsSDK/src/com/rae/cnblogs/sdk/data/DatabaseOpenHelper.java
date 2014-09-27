package com.rae.cnblogs.sdk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.rae.cnblogs.sdk.model.Constant;

/**
 * 数据库帮助类
 * 
 * @author ChenRui
 * 
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
	private Context mContext;

	public DatabaseOpenHelper(Context context) {
		super(context, "cnblogs.db", null, 1);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.beginTransaction(); // 开始事务
			String[] group = Constant.getInitSql(mContext).split(";");// 根据分号进行分割
			for (String strSql : group) {
				if (TextUtils.isEmpty(strSql.trim()))
					continue;
				try {
					db.execSQL(strSql);
					Log.i("cnblogs", "执行->" + strSql);
				} catch (Exception e) {
					Log.e("sql",
							"数据库初始化失败：" + strSql + ";\r\n" + e.getMessage());
					continue;
				}
			}
			db.setTransactionSuccessful(); // 提交事务

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		drop(db, "blogs");
		drop(db, "categroys");
		drop(db, "options");
	}

	private void drop(SQLiteDatabase db, String table) {
		try {
			db.execSQL("drop table " + table);
		} catch (Exception e) {
			Log.e("sql", "删除数据库出错：" + e.getMessage());
		}
	}

}
